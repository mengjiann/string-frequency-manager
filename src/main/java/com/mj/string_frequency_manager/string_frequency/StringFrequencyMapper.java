package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.exception.StringFrequencyMapException;
import com.mj.string_frequency_manager.util.InputValidator;
import org.springframework.stereotype.Service;

@Service
public class StringFrequencyMapper {

    public StringFrequency map(String redisKey) throws StringFrequencyMapException {
        Preconditions.checkArgument(!InputValidator.isInvalid(redisKey),"Invalid redisKey for mapper.");

        String[] redisKeyArray = redisKey.split(AppConstant.REDIS_NAMESPACE_SEPARATOR);
        if(InputValidator.isInvalid(redisKeyArray) || redisKeyArray.length != 3){
            throw new StringFrequencyMapException("Unable construct string frequency from redisKey: "+redisKey);
        }

        return new StringFrequency(redisKeyArray[1],redisKeyArray[2]);
    }
}
