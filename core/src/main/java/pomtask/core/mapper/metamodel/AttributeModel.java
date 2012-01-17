package pomtask.core.mapper.metamodel;

import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.lang.reflect.Field;

public class AttributeModel extends Property {
    public AttributeModel(MetaModel model, Field field) {
        super(model, field);
    }

    @Override
    public Object update(Object obj, JedisConnection connection) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object create(Object obj, JedisConnection connection) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
