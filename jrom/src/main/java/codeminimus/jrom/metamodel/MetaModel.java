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
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class MetaModel<T> {
    @VisibleForTesting
    final String modelName;
    @VisibleForTesting
    final KeyModel keyModel;
    @VisibleForTesting
    final Map<String, FieldModel> fields;

    @VisibleForTesting
    MetaModel(String modelName, KeyModel keyModel, Map<String, FieldModel> fieldModelList) {
        this.modelName = modelName;
        this.keyModel = keyModel;
        this.fields = ImmutableMap.copyOf(fieldModelList);
    }

    private MetaModel(Class<T> modelClass) {
        if (!modelClass.isAnnotationPresent(KeyValueModel.class)) {
            throw new KeyValueMappingException(String.format("Not a model object: No %s annotation present on class.", KeyValueModel.class.getName()));
        }

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

    public String getKey(Object obj) {
        String keyValue = ConvertUtils.convert(obj);
        return String.format("%s:%s", getModelName(), keyValue);
    }

    private String findModelName(Class<?> modelClass) {
        KeyValueModel annotation = modelClass.getAnnotation(KeyValueModel.class);
        return annotation.name().isEmpty() ? StringUtils.uncapitalize(modelClass.getSimpleName()) : annotation.name();
    }

    public T create(T object, StringJedisConnection connection) {
        Map<String, Object> fieldValues = Maps.newHashMap();

        Object keyValue = keyModel.create(null, object, connection);
        String key = getKey(keyValue);
        for (FieldModel field : fields.values()) {
            Object setValue = field.create(key, object, connection);
            fieldValues.put(field.fieldName(), setValue);
        }
        return createNewObject(object, fieldValues, keyValue);
    }

    @SuppressWarnings("unchecked")
    private T createNewObject(T object, Map<String, Object> fieldValues, Object keyValue) {
        try {
            T createdObject = (T) object.getClass().newInstance();
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
}
