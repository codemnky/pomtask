package codeminimus.jrom;

import codeminimus.jrom.metamodel.MetaModel;
import com.google.common.collect.Maps;

import java.util.Map;

public class RedisMapper {
    private final Map<Class, MetaModel> modelMap = Maps.newHashMap();

    public <T> T save(T object) {
        MetaModel model = getMetaModel(object.getClass());
        return null;
    }

    private MetaModel getMetaModel(Class<?> modelClass) {
        MetaModel model;
        if (modelMap.containsKey(modelClass)) {
            model = modelMap.get(modelClass);
        } else {
            model = new MetaModel(modelClass);
            modelMap.put(modelClass, model);
        }
        return model;
    }
}
