package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import org.springframework.stereotype.Service;

@Service
public class StringFrequencyUpdater {

    private StringFrequencyRepository repository;

    public StringFrequencyUpdater(StringFrequencyRepository repository) {
        this.repository = repository;
    }
    
    public void incrementCountForStringId(StringFrequency stringFrequency){
        repository.incrementStringFrequency(stringFrequency);
    }

    public void decrementCountForStringId(StringFrequency stringFrequency){
        repository.decrementStringFrequency(stringFrequency);
    }

    public void removeStringId(StringFrequency stringFrequency){
        repository.deleteStringFrequency(stringFrequency);
    }

}
