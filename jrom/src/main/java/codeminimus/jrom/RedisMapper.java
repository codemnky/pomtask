package codeminimus.jrom;

import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.exception.KeyValueMappingException;

public class RedisMapper {
    public <T> T save(T object) {
        if (!object.getClass().isAnnotationPresent(KeyValueModel.class)) {
            throw new KeyValueMappingException(String.format("Attempting to save object: No %s annotation present on class.", KeyValueModel.class.getName()));
        }
        return null;
    }
}
