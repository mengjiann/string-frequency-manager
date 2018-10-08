package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.util.InputValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


@Service
public class StringFrequencyGetter {

    private StringFrequencyRepository stringFrequencyRepository;

    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    public StringFrequencyGetter(StringFrequencyRepository stringFrequencyRepository, HourlyStringFrequencyMapper hourlyStringFrequencyMapper) {
        this.stringFrequencyRepository = stringFrequencyRepository;
        this.hourlyStringFrequencyMapper = hourlyStringFrequencyMapper;
    }

    public Optional<StringFrequency> getByStringFrequency(StringFrequency stringFrequency) {
        Preconditions.checkArgument(!InputValidator.isInvalid(stringFrequency),"Invalid stringFrequency.");

        return stringFrequencyRepository.getByTypeAndStringId(stringFrequency.getType(), stringFrequency.getStringId());
    }

    public Set<String> getRedisKeysForHour(LocalDateTime localDateTime){
        return stringFrequencyRepository.getAllKeysFromNamespace(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(localDateTime));
    }

    public Set<String> getRedisKeysForPast24Hour(){
        return stringFrequencyRepository.getAllKeysFromNamespace(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR);
    }

}
