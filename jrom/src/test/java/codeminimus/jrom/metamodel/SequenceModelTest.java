package codeminimus.jrom.metamodel;

import codeminimus.jrom.MappingLifecycle;
import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;

public class SequenceModelTest {
    private StringJedisConnection connection = Mockito.mock(StringJedisConnection.class);

    @Test
    public void incrementKeyName() throws NoSuchFieldException {
        SequenceModel sequence = new SequenceModel("modelName", DummySequenceModel.class.getDeclaredField("sequenceField"));

        assertThat(sequence.sequenceKeyName, CoreMatchers.is("modelName:sequenceField"));
    }

    @Test
    public void sequenceKeyName_Named() throws NoSuchFieldException {
        SequenceModel sequence = new SequenceModel("modelName", DummySequenceModel.class.getDeclaredField("namedSequenceField"));

        assertThat(sequence.sequenceKeyName, CoreMatchers.is("sequence:modelName"));
    }

    @Test
    public void next() throws NoSuchFieldException {
        Mockito.when(connection.incr("modelName:sequenceField")).thenReturn(1L);

        SequenceModel sequence = new SequenceModel("modelName", DummySequenceModel.class.getDeclaredField("sequenceField"));

        long result = sequence.next(connection);

        assertThat(result, CoreMatchers.is(1L));
    }

    @KeyValueModel
    public class DummySequenceModel {
        @Sequence(when = MappingLifecycle.BEFORE_SAVE)
        private long sequenceField;

        @Sequence(when = MappingLifecycle.BEFORE_SAVE, name = "sequence:modelName")
        private int namedSequenceField;
    }
}
