package com.mj.string_frequency_manager.string_frequency.web;

import com.mj.string_frequency_manager.string_frequency.StringFrequencyApiResponseMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyGetter;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.api.StringFrequencyApiResponse;
import com.mj.string_frequency_manager.string_frequency.exception.InvalidStringFrequencyException;
import com.mj.string_frequency_manager.web.exception.AppErrorCode;
import com.mj.string_frequency_manager.web.exception.AppErrorResponse;
import com.mj.string_frequency_manager.web.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Slf4j
@Validated
@RestController
public class StringFrequencyRestController {

    private StringFrequencyGetter stringFrequencyGetter;

    private StringFrequencyApiResponseMapper stringFrequencyApiResponseMapper;

    public StringFrequencyRestController(StringFrequencyGetter stringFrequencyGetter, StringFrequencyApiResponseMapper stringFrequencyApiResponseMapper) {
        this.stringFrequencyGetter = stringFrequencyGetter;
        this.stringFrequencyApiResponseMapper = stringFrequencyApiResponseMapper;
    }

    @GetMapping("/isStringValid")
    public StringFrequencyApiResponse isStringValid(
            @NotNull(message = "Required string parameter 'string' is invalid.")
            @Size( min = 1, max = 40, message = "String exceeds allowable range: 1 - 40 characters")
            @RequestParam("string") String stringId){

        try{
            log.debug("Getting string frequency for text: {}", stringId);

            StringFrequency stringFrequency =
                    stringFrequencyGetter.getByStringFrequency(new Past24HourStringFrequency(stringId))
                            .orElseThrow(() -> new InvalidStringFrequencyException("Unable to get string frequency by stringId:"+stringId));

            return stringFrequencyApiResponseMapper.map(stringFrequency);
        }catch (InvalidStringFrequencyException e){
            throw AppException.builder()
                    .errorResponse(AppErrorResponse.builder()
                            .errorCode(AppErrorCode.STRING_FREQUENCY_RETRIEVAL_ERROR)
                            .error("Unable to retrieve string by stringId: "+stringId+" with error: "+e.getMessage())
                            .build())
                    .build();
        }

    }


}
