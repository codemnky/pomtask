package pomtask.core.mapper.metamodel;


import org.springframework.data.redis.connection.jedis.JedisConnection;

public interface Property {
    String fieldName();

    Object valueForUpdate(Object obj, JedisConnection connection);

    Object valueForCreate(Object obj, JedisConnection connection);
}
