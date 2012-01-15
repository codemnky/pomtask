package pomtask.core.mapper;

import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.AutoIncrement;
import pomtask.core.mapper.annotation.KeyValueModel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RedisAutoIncrementerTest {
    private JedisConnection connection = mock(JedisConnection.class);
    private AnnotationHelper helper = spy(AnnotationHelper.HELPER);

    @Test
    public void incrementKeyName() throws NoSuchFieldException {
        when(helper.findModelName(IncrementingModel.class)).thenReturn("modelName");

        RedisAutoIncrementer incrementer = new RedisAutoIncrementer(IncrementingModel.class.getDeclaredField("incrementField"), helper);

        assertThat(incrementer.incrementKeyName, is("modelName:incrementField"));
    }

    @Test
    public void increment_OnSave() throws NoSuchFieldException {
        when(helper.findModelName(IncrementingModel.class)).thenReturn("modelName");
        when(connection.incr("modelName:incrementField".getBytes())).thenReturn(1L);

        RedisAutoIncrementer incrementer = new RedisAutoIncrementer(IncrementingModel.class.getDeclaredField("incrementField"), helper);

        IncrementingModel incrementingModel = new IncrementingModel();

        incrementer.increment(MappingLifecycle.BEFORE_SAVE, connection, incrementingModel);

        assertThat(incrementingModel.incrementField, is(1L));
    }

    @KeyValueModel
    public class IncrementingModel {
        @AutoIncrement
        private long incrementField;
    }
}
