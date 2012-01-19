package codeminimus.jrom.metamodel;


import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class MetaModel {
    @VisibleForTesting
    final String modelName;
    @VisibleForTesting
    final KeyModel key;
    @VisibleForTesting
    final List<FieldModel> fields;

    @VisibleForTesting
    MetaModel(String modelName, KeyModel keyModel, List<FieldModel> fieldModelList) {
        this.modelName = modelName;
        this.key = keyModel;
        this.fields = ImmutableList.copyOf(fieldModelList);
    }

    public MetaModel(Class<?> modelClass) {
        if (!modelClass.isAnnotationPresent(KeyValueModel.class)) {
            throw new KeyValueMappingException(String.format("Not a model object: No %s annotation present on class.", KeyValueModel.class.getName()));
        }

        modelName = findModelName(modelClass);

        List<FieldModel> fieldModels = buildFields(modelClass);

        key = findKey(fieldModels);

        fieldModels.remove(key);

        fields = ImmutableList.copyOf(fieldModels);

    }

    private KeyModel findKey(List<FieldModel> fieldModels) {
        Iterable<KeyModel> keys = Iterables.filter(fieldModels, KeyModel.class);
        if (Iterables.size(keys) > 1) {
            throw new KeyValueMappingException("Model class may only contain one field annotated with @Key.");
        }
        if (Iterables.isEmpty(keys)) {
            throw new KeyValueMappingException("Model class does not contain a field annotated with @Key.");
        }
        return keys.iterator().next();
    }

    private List<FieldModel> buildFields(Class<?> modelClass) {
        List<FieldModel> fieldModelList = Lists.newArrayList();
        for (Field field : modelClass.getDeclaredFields()) {
            FieldModel fieldModel = buildFieldModel(field);
            if (fieldModel != null) {
                fieldModelList.add(fieldModel);
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
        return key.key(obj);
    }

    private String findModelName(Class<?> modelClass) {
        KeyValueModel annotation = modelClass.getAnnotation(KeyValueModel.class);
        return annotation.name().isEmpty() ? StringUtils.uncapitalize(modelClass.getSimpleName()) : annotation.name();
    }
}
