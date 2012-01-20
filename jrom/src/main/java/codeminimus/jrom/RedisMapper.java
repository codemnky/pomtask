package codeminimus.jrom;

import codeminimus.jrom.metamodel.MetaModel;
import com.google.common.collect.Maps;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.util.Map;

public class RedisMapper {
    private final Map<Class, MetaModel> modelMap = Maps.newHashMap();
    private final StringJedisConnection connection;

    public RedisMapper(JedisConnection connection) {
        this.connection = new StringJedisConnection(connection);
    }

    public <T> T save(T object) {
        MetaModel model = getMetaModel(object.getClass());
        model.create(object, connection);
        return null;
    }

    private MetaModel getMetaModel(Class<?> modelClass) {
        MetaModel model;
        if (modelMap.containsKey(modelClass)) {
            model = modelMap.get(modelClass);
        } else {
            model = MetaModel.newMetaModel(modelClass);
            modelMap.put(modelClass, model);
        }
        return model;
    }
}
