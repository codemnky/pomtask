package pomtask.core.mapper.metamodel;

import com.google.common.annotations.VisibleForTesting;
import pomtask.core.mapper.StringJedisConnection;

public class SequencedKeyModel extends KeyModel {
    @VisibleForTesting
    final SequenceModel sequenceModel;

    public SequencedKeyModel(KeyModel keyModel, SequenceModel sequenceModel) {
        super(keyModel);
        this.sequenceModel = sequenceModel;
    }

    @Override
    public Object create(Object obj, StringJedisConnection connection) {
        return sequenceModel.next(connection);
    }
}
