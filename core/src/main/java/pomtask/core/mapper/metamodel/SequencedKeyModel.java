package pomtask.core.mapper.metamodel;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.redis.connection.jedis.JedisConnection;

public class SequencedKeyModel extends KeyModel {
    @VisibleForTesting
    final SequenceModel sequenceModel;

    public SequencedKeyModel(KeyModel keyModel, SequenceModel sequenceModel) {
        super(keyModel);
        this.sequenceModel = sequenceModel;
    }

    @Override
    public Object create(Object obj, JedisConnection connection) {
        return sequenceModel.next(connection);
    }
}
