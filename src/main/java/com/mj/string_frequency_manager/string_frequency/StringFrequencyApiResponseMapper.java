package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.api.StringFrequencyApiResponse;
import com.mj.string_frequency_manager.util.InputValidator;
import org.springframework.stereotype.Service;

@Service
public class StringFrequencyApiResponseMapper {

    public StringFrequencyApiResponse map(StringFrequency stringFrequency){
        Preconditions.checkArgument(!InputValidator.isInvalid(stringFrequency),"Invalid string frequency");
        Preconditions.checkArgument(!InputValidator.isInvalid(stringFrequency.getCounter()),"Invalid string frequency countr");

        return StringFrequencyApiResponse.builder()
                .response(Boolean.toString(stringFrequency.getCounter() > AppConstant.SUCCESS_API_RESPONSE_COUNT))
                .build();
    }

}
