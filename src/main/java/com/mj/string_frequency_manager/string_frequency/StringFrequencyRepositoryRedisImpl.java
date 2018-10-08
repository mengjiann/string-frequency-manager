package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class StringFrequencyRepositoryRedisImpl implements StringFrequencyRepository {

    private RedisTemplate<String, Integer> redisTemplate;
    private ValueOperations<String,Integer> valueOperations;

    public StringFrequencyRepositoryRedisImpl(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        this.valueOperations = redisTemplate.opsForValue();
    }

    private String getKeyWithNamespace(String type, String stringId){
        return new StringBuilder()
                .append(AppConstant.REDIS_STORE_NAMESPACE).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append(type).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append(stringId)
                .toString();
    }

    @Override
    public void incrementStringFrequency(StringFrequency stringFrequency) {
        valueOperations.increment(
                this.getKeyWithNamespace(stringFrequency.getType(),
                        stringFrequency.getStringId()),
                InputValidator.isInvalid(stringFrequency.getCounter()) ? 1L : stringFrequency.getCounter().longValue());
    }

    @Override
    public void decrementStringFrequency(StringFrequency stringFrequency) {
        valueOperations.increment(
                this.getKeyWithNamespace(stringFrequency.getType(),
                        stringFrequency.getStringId()),
                InputValidator.isInvalid(stringFrequency.getCounter()) ? -1L : -1 * stringFrequency.getCounter().longValue());
    }

    @Override
    public void deleteStringFrequency(StringFrequency stringFrequency) {
        redisTemplate.delete(this.getKeyWithNamespace(stringFrequency.getType(),
                stringFrequency.getStringId()));
    }

    @Override
    public Optional<StringFrequency> getByTypeAndStringId(String type, String stringId) {
        Preconditions.checkArgument(!InputValidator.isInvalid(type),"Invalid stringId for retrieval.");

        Integer counter = valueOperations.get(this.getKeyWithNamespace(type,stringId));
        if (InputValidator.isInvalid(counter)) {
            return Optional.empty();
        }
        return Optional.of(new StringFrequency(type, stringId,counter));
    }

    public Set<String> getAllKeysFromNamespace(String namespace) {
        String keyPattern = new StringBuilder()
                .append(AppConstant.REDIS_STORE_NAMESPACE).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append(namespace).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append("*").toString();

        return redisTemplate.keys(keyPattern);
    }


}
