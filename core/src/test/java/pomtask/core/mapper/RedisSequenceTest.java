package pomtask.core.mapper;

import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.KeyValueModel;
import pomtask.core.mapper.annotation.Sequence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RedisSequenceTest {
    private JedisConnection connection = mock(JedisConnection.class);
    private AnnotationHelper helper = spy(AnnotationHelper.HELPER);

    @Test
    public void incrementKeyName() throws NoSuchFieldException {
        when(helper.findModelName(SequenceModel.class)).thenReturn("modelName");

        RedisSequence incrementer = new RedisSequence(SequenceModel.class.getDeclaredField("sequenceField"), helper);

        assertThat(incrementer.sequenceKeyName, is("modelName:sequenceField"));
    }

    @Test
    public void sequenceKeyName_Named() throws NoSuchFieldException {
        when(helper.findModelName(SequenceModel.class)).thenReturn("modelName");

        RedisSequence incrementer = new RedisSequence(SequenceModel.class.getDeclaredField("namedSequenceField"), helper);

        assertThat(incrementer.sequenceKeyName, is("sequence:name"));
    }

    @Test
    public void increment_OnSave() throws NoSuchFieldException {
        when(helper.findModelName(SequenceModel.class)).thenReturn("modelName");
        when(connection.incr("modelName:sequenceField".getBytes())).thenReturn(1L);

        RedisSequence sequence = new RedisSequence(SequenceModel.class.getDeclaredField("sequenceField"), helper);

        SequenceModel sequenceModel = new SequenceModel();

        sequence.increment(MappingLifecycle.BEFORE_SAVE, connection, sequenceModel);

        assertThat(sequenceModel.sequenceField, is(1L));
    }

    @Test
    public void increment_OnSave_CastToInteger() throws NoSuchFieldException {
        when(helper.findModelName(SequenceModel.class)).thenReturn("modelName");
        when(connection.incr("sequence:name".getBytes())).thenReturn(1L);

        RedisSequence sequence = new RedisSequence(SequenceModel.class.getDeclaredField("namedSequenceField"), helper);

        SequenceModel sequenceModel = new SequenceModel();

        sequence.increment(MappingLifecycle.BEFORE_SAVE, connection, sequenceModel);

        assertThat(sequenceModel.namedSequenceField, is(1));
    }

    @KeyValueModel
    public class SequenceModel {
        @Sequence(when = MappingLifecycle.BEFORE_SAVE)
        private long sequenceField;

        @Sequence(when = MappingLifecycle.BEFORE_SAVE, name = "sequence:name")
        private int namedSequenceField;
    }
}
