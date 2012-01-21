package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import com.google.common.annotations.VisibleForTesting;

public class SequencedKeyModel extends KeyModel {
    @VisibleForTesting
    final SequenceModel sequenceModel;

    public SequencedKeyModel(KeyModel keyModel, SequenceModel sequenceModel) {
        super(keyModel);
        this.sequenceModel = sequenceModel;
    }

    @Override
    public Object create(String key, Object obj, StringJedisConnection connection) {
        long next = sequenceModel.next(connection);
        connection.hSet(model.getKey(next), fieldName(), Long.toString(next));
        return next;
    }
}
