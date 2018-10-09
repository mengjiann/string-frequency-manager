package com.mj.string_frequency_manager.string_frequency.web;

import com.mj.string_frequency_manager.ApplicationInitializationUtil;
import com.mj.string_frequency_manager.config.RedisServerConfig;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyGetter;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import redis.embedded.RedisServer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = StringFrequencyRestController.class)
public class StringFrequencyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisServer redisServer;

    @MockBean
    private ApplicationInitializationUtil applicationInitializationUtil;

    @MockBean
    private StringFrequencyGetter stringFrequencyGetter;

    @Test
    public void isStringValid_stringFrequencyMoreThan5_shouldReturnTrue() throws Exception {

        Past24HourStringFrequency stringFrequency = new Past24HourStringFrequency("abc");

        given(stringFrequencyGetter.getByStringFrequency(stringFrequency))
                .willReturn(Optional.of(new Past24HourStringFrequency("abc",10)));

        mockMvc.perform(get("/isStringValid?string=abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"));
    }

    @Test
    public void isStringValid_stringFrequencyLessThan5_shouldReturnFalse() throws Exception {

        Past24HourStringFrequency stringFrequency = new Past24HourStringFrequency("abc");

        given(stringFrequencyGetter.getByStringFrequency(stringFrequency))
                .willReturn(Optional.of(new Past24HourStringFrequency("abc",3)));

        mockMvc.perform(get("/isStringValid?string=abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("false"));
    }

    @Test
    public void isStringValid_invalidStringRequestParam_returnBadRequest() throws Exception {
        mockMvc.perform(get("/isStringValid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void isStringValid_emptyStringRequestParam_returnBadRequest() throws Exception {
        mockMvc.perform(get("/isStringValid?string="))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void isStringValid_exceedStringMaxCharacter_returnBadRequest() throws Exception {
        mockMvc.perform(get("/isStringValid?string=abcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh"))
                .andExpect(status().isBadRequest());
    }
}