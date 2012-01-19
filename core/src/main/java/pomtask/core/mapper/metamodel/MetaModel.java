package pomtask.core.mapper.metamodel;


import com.google.common.annotations.VisibleForTesting;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.Sequence;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;
import java.util.List;

public class MetaModel {
    @VisibleForTesting
    String modelName;
    @VisibleForTesting
    KeyModel key;
    @VisibleForTesting
    List<FieldModel> fields;

    public MetaModel() {
    }

    public void addField(Field field) {
        if (field.isAnnotationPresent(Key.class)) {
            key = buildKey(modelName, field);
        }
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
}
