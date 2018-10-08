package com.mj.string_frequency_manager.string_frequency_hourly_updater_task;

import com.mj.string_frequency_manager.string_frequency.StringFrequencyGetter;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyUpdater;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.exception.InvalidStringFrequencyException;
import com.mj.string_frequency_manager.string_frequency.exception.StringFrequencyMapException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
public class Past24HourStringFrequencyUpdater {

    private StringFrequencyGetter stringFrequencyGetter;

    private StringFrequencyUpdater stringFrequencyUpdater;

    private StringFrequencyMapper stringFrequencyMapper;

    public Past24HourStringFrequencyUpdater(StringFrequencyGetter stringFrequencyGetter,
                                            StringFrequencyUpdater stringFrequencyUpdater, StringFrequencyMapper stringFrequencyMapper) {
        this.stringFrequencyGetter = stringFrequencyGetter;
        this.stringFrequencyUpdater = stringFrequencyUpdater;
        this.stringFrequencyMapper = stringFrequencyMapper;
    }

    public void removeHourlyStringFrequencyForHour(LocalDateTime hourForRemoval){

        Set<String> redisKeysForHour = stringFrequencyGetter.getRedisKeysForHour(hourForRemoval);
        if(redisKeysForHour.isEmpty()){
            return;
        }

        log.info("To remove string frequencies: {}",redisKeysForHour);

        for (String redisKey : redisKeysForHour) {
            try{
                StringFrequency stringFrequency = stringFrequencyGetter
                        .getByStringFrequency(stringFrequencyMapper.map(redisKey))
                        .orElseThrow(() -> new InvalidStringFrequencyException("Unable to get stringFrequency by redisKey: "+redisKey));

                log.debug("To remove string frequency: {}",stringFrequency);

                Past24HourStringFrequency toUpdate = new Past24HourStringFrequency(stringFrequency.getStringId()
                        ,stringFrequency.getCounter());

                stringFrequencyUpdater.decrementCountForStringId(toUpdate);
            }catch (StringFrequencyMapException | InvalidStringFrequencyException e){
                log.error("Unable to remove hourly string frequency for redisKey: {} with err: {}",redisKey,e.getMessage(),e);
            }
        }
    }
}
