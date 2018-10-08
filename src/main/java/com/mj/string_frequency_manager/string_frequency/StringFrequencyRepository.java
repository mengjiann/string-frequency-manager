package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;

import java.util.Optional;
import java.util.Set;

interface StringFrequencyRepository {

    void incrementStringFrequency(StringFrequency stringFrequency);

    void decrementStringFrequency(StringFrequency stringFrequency);

    void deleteStringFrequency(StringFrequency stringFrequency);

    Optional<StringFrequency> getByTypeAndStringId(String type, String stringId);

    Set<String> getAllKeysFromNamespace(String namespace);

}
