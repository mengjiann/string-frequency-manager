package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StringFrequencyGetterTest {

    @Mock
    private StringFrequencyRepository stringFrequencyRepository;

    @Mock
    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    private StringFrequencyGetter stringFrequencyGetter;

    @Before
    public void setUp() throws Exception {
        stringFrequencyGetter = new StringFrequencyGetter(stringFrequencyRepository,hourlyStringFrequencyMapper);
    }

    @Test
    public void getByStringFrequency_validInput_validOutput() {
        StringFrequency stringFrequency = new StringFrequency(
                AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR,"abc");

        given(stringFrequencyRepository.getByTypeAndStringId(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR,"abc"))
                .willReturn(Optional.of(new StringFrequency(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR,"abc",3)));

        StringFrequency output = stringFrequencyGetter.getByStringFrequency(stringFrequency).get();

        assertThat(output.getCounter(),is(3));
        assertThat(output.getStringId(),is("abc"));
        assertThat(output.getType(),is(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByStringFrequency_invalidInput_throwIllegalArgumentException() {
        stringFrequencyGetter.getByStringFrequency(null).get();
    }

    @Test
    public void getRedisKeysForHour() {
        Set<String> redisKeys = new HashSet<String>(){{
            add("abc");
            add("cbd");
        }};

        LocalDateTime now = LocalDateTime.now();

        now = now.withYear(2018).withMonth(10).withDayOfMonth(7).withHour(22);

        given(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(now))
                .willReturn("abc");

        given(stringFrequencyRepository.getAllKeysFromNamespace("abc"))
                .willReturn(redisKeys);

        Set<String> outputRedisKeys = stringFrequencyGetter.getRedisKeysForHour(now);

        assertThat(outputRedisKeys,hasItem("abc"));
        assertThat(outputRedisKeys,hasItem("cbd"));
    }
}