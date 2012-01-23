package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.convert.StringConvert;

import java.lang.reflect.Field;
import java.util.Map;

public class MetaModel<T> {
    @VisibleForTesting
    final String modelName;
    @VisibleForTesting
    final KeyModel keyModel;
    @VisibleForTesting
    final Map<String, FieldModel> fields;
    private final Class<T> modelClass;

    @VisibleForTesting
    MetaModel(String modelName, KeyModel keyModel, Map<String, FieldModel> fieldModelList) {
        this.modelName = modelName;
        this.keyModel = keyModel;
        this.fields = ImmutableMap.copyOf(fieldModelList);
        this.modelClass = null;
    }

    private MetaModel(Class<T> modelClass) {
        if (!modelClass.isAnnotationPresent(KeyValueModel.class)) {
            throw new KeyValueMappingException(String.format("Not a model object: No %s annotation present on class.", KeyValueModel.class.getName()));
        }

        this.modelClass = modelClass;

        modelName = findModelName(modelClass);

        Map<String, FieldModel> fieldModels = buildFields(modelClass);

        keyModel = findKey(fieldModels);

        fieldModels.remove(keyModel.fieldName());

        fields = ImmutableMap.copyOf(fieldModels);

    }

    public static <T> MetaModel<T> newMetaModel(Class<T> modelClass) {
        return new MetaModel<T>(modelClass);
    }

    private KeyModel findKey(Map<String, FieldModel> fieldModels) {
        Iterable<KeyModel> keys = Iterables.filter(fieldModels.values(), KeyModel.class);
        if (Iterables.size(keys) > 1) {
            throw new KeyValueMappingException("Model class may only contain one field annotated with @Key.");
        }
        if (Iterables.isEmpty(keys)) {
            throw new KeyValueMappingException("Model class does not contain a field annotated with @Key.");
        }
        return keys.iterator().next();
    }

    private Map<String, FieldModel> buildFields(Class<?> modelClass) {
        Map<String, FieldModel> fieldModelList = Maps.newHashMap();
        for (Field field : modelClass.getDeclaredFields()) {
            FieldModel fieldModel = buildFieldModel(field);
            if (fieldModel != null) {
                fieldModelList.put(fieldModel.fieldName(), fieldModel);
            }
        }
        return fieldModelList;
    }

    public FieldModel buildFieldModel(Field field) {
        FieldModel fieldModel;
        if (field.isAnnotationPresent(Key.class)) {
            fieldModel = buildKey(modelName, field);
        } else if (field.isAnnotationPresent(Unmapped.class)) {
            fieldModel = null;
        } else {
            fieldModel = buildAttribute(field);
        }
        return fieldModel;
    }

    private AttributeModel buildAttribute(Field field) {
        return new AttributeModel(this, field);
    }

    private KeyModel buildKey(String modelName, Field field) {
        KeyModel keyModel = new KeyModel(this, field);
        SequenceModel sequenceModel;
        if (field.isAnnotationPresent(Sequence.class)) {
            sequenceModel = new SequenceModel(modelName, field);
            keyModel = new SequencedKeyModel(keyModel, sequenceModel);
        }
        return keyModel;
    }

    public String getModelName() {
        return modelName;
    }

    public String buildKey(Object keyObject) {
        String keyValue = StringConvert.INSTANCE.convertToString(keyObject);
        return String.format("%s:%s", getModelName(), keyValue);
    }

    public T create(T object, StringJedisConnection connection) {
        Object keyValue = keyModel.create("", object, connection);
        String key = buildKey(keyValue);

        Map<String, Object> fieldValues = Maps.newHashMap();
        for (FieldModel field : fields.values()) {
            Object setValue = field.create(key, object, connection);
            fieldValues.put(field.fieldName(), setValue);
        }
        return createNewObject(fieldValues, keyValue, modelClass);
    }

    public T update(T object, StringJedisConnection connection) {
        Object keyValue = keyModel.update("", object, connection);
        String key = buildKey(keyValue);

        Map<String, Object> fieldValues = Maps.newHashMap();
        for (FieldModel field : fields.values()) {
            Object setValue = field.update(key, object, connection);
            fieldValues.put(field.fieldName(), setValue);
        }
        return createNewObject(fieldValues, keyValue, modelClass);
    }

    public T read(Object keyValue, StringJedisConnection connection) {
        String key = buildKey(keyValue);

        Map<String, Object> fieldValues = Maps.newHashMap();
        for (FieldModel field : fields.values()) {
            Object setValue = field.read(key, connection);
            fieldValues.put(field.fieldName(), setValue);
        }
        return createNewObject(fieldValues, keyValue, modelClass);
    }

    public boolean delete(T object, StringJedisConnection connection) {
        return connection.del(key(object)) == 1;
    }

    public String key(T object) {
        Object keyValue = keyModel.value(object);
        return buildKey(keyValue);
    }

    @SuppressWarnings("unchecked")
    private T createNewObject(Map<String, Object> fieldValues, Object keyValue, Class<T> aClass) {
        try {
            T createdObject = aClass.newInstance();
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                fields.get(entry.getKey()).set(createdObject, entry.getValue());
            }
            keyModel.set(createdObject, keyValue);
            return createdObject;
        } catch (InstantiationException e) {
            throw Throwables.propagate(e);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }


    private String findModelName(Class<?> modelClass) {
        KeyValueModel annotation = modelClass.getAnnotation(KeyValueModel.class);
        return annotation.name().isEmpty() ? StringUtils.uncapitalize(modelClass.getSimpleName()) : annotation.name();
    }
}
