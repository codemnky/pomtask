package pomtask.core.mapper;

import pomtask.core.mapper.annotation.KeyValueModel;
import pomtask.core.mapper.exception.KeyValueMappingException;

public class RedisMapper {
    public <T> T save(T object) {
        if (!object.getClass().isAnnotationPresent(KeyValueModel.class)) {
            throw new KeyValueMappingException(String.format("Attempting to save object: No %s annotation present on class.", KeyValueModel.class.getName()));
        }
        return null;
    }
}
