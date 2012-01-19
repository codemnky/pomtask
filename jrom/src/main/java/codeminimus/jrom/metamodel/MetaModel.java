package codeminimus.jrom.metamodel;


import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.annotations.VisibleForTesting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetaModel {
    @VisibleForTesting
    String modelName;
    @VisibleForTesting
    KeyModel key;
    @VisibleForTesting
    List<FieldModel> fields = new ArrayList<FieldModel>();

    public MetaModel() {
    }

    public void addField(Field field) {
        if (field.isAnnotationPresent(Key.class)) {
            key = buildKey(modelName, field);
        } else if (field.isAnnotationPresent(Unmapped.class)) {
            //ignored
        } else {
            fields.add(buildAttribute(field));
        }
    }

    private AttributeModel buildAttribute(Field field) {
        return new AttributeModel(this, field);
    }

    private KeyModel buildKey(String modelName, Field field) {
        if (key != null) {
            throw new KeyValueMappingException("Model class may only contain one field annotated with @Key.");
        }
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
}
