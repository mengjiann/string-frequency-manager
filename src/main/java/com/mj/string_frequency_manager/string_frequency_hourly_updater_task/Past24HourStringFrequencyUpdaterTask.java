package com.mj.string_frequency_manager.string_frequency_hourly_updater_task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class Past24HourStringFrequencyUpdaterTask {

    Past24HourStringFrequencyUpdater past24HourStringFrequencyUpdater;

    public Past24HourStringFrequencyUpdaterTask(Past24HourStringFrequencyUpdater past24HourStringFrequencyUpdater) {
        this.past24HourStringFrequencyUpdater = past24HourStringFrequencyUpdater;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void execute(){

        LocalDateTime hourToRemove = LocalDateTime.now().minusHours(24);

        log.info("Past24HourStringFrequencyUpdaterTask executed at: {} to remove: {}.",LocalDateTime.now(),hourToRemove);

        past24HourStringFrequencyUpdater.removeHourlyStringFrequencyForHour(hourToRemove);

        log.info("Past24HourStringFrequencyUpdaterTask executed at: {} to remove: {} completed.",LocalDateTime.now(),hourToRemove);

    }
}
