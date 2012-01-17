package pomtask.core.mapper.metamodel;


import com.google.common.annotations.VisibleForTesting;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.Sequence;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;

public class MetaModelBuilder {
    @VisibleForTesting
    String modelName;
    @VisibleForTesting
    KeyModel key;

    public MetaModelBuilder() {
    }

    public void addProperty(Field field) {
        if (field.isAnnotationPresent(Key.class)) {
            key = buildKey(modelName, field);
        }
    }

    private KeyModel buildKey(String modelName, Field field) {
        if (key != null) {
            throw new KeyValueMappingException("Model class may only contain one field annotated with @Key.");
        }
        KeyModel keyModel = new KeyModel(modelName, field);
        SequenceModel sequenceModel;
        if (field.isAnnotationPresent(Sequence.class)) {
            sequenceModel = new SequenceModel(modelName, field);
            keyModel = new SequencedKeyModel(keyModel, sequenceModel);
        }
        return keyModel;
    }

}
