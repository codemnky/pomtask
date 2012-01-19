package pomtask.core.mapper.metamodel;

import org.hamcrest.core.IsSame;
import org.junit.Before;
import org.junit.Test;
import pomtask.core.mapper.StringJedisConnection;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.Sequence;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SequencedKeyModelTest {
    private StringJedisConnection mockConnection = mock(StringJedisConnection.class);
    private Field field;
    private MetaModel model = new MetaModel();

    @Before
    public void setUp() throws Exception {
        field = DummyModel.class.getDeclaredField("sequenceKey");
        model.modelName = "modelName";
    }

    @Test
    public void construct() throws NoSuchFieldException {
        KeyModel keyModel = new KeyModel(model, field);
        SequenceModel sequenceModel = new SequenceModel("modelName", field);

        SequencedKeyModel sequencedKeyModel = new SequencedKeyModel(keyModel, sequenceModel);

        assertThat(sequencedKeyModel.sequenceModel, IsSame.sameInstance(sequenceModel));
        assertThat(sequencedKeyModel.model, sameInstance(model));
        assertThat(sequencedKeyModel.field, is(field));
    }

    @Test
    public void valueForCreate() {
        when(mockConnection.incr("modelName:sequenceKey")).thenReturn(1L);

        KeyModel keyModel = new KeyModel(model, field);
        SequenceModel sequenceModel = new SequenceModel("modelName", field);

        SequencedKeyModel sequencedKeyModel = new SequencedKeyModel(keyModel, sequenceModel);

        assertThat((Long) sequencedKeyModel.create(new DummyModel(), mockConnection), is(1L));
    }

    @Test
    public void key_WhenSet() {
        DummyModel model = new DummyModel();
        model.sequenceKey = 1;
    }

    public class DummyModel {
        @Key
        @Sequence
        long sequenceKey;
    }
}
