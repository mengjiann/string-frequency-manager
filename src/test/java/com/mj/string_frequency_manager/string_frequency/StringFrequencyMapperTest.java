package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.exception.StringFrequencyMapException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class StringFrequencyMapperTest {

    private StringFrequencyMapper stringFrequencyMapper;

    @Before
    public void setUp() throws Exception {
        stringFrequencyMapper = new StringFrequencyMapper();
    }

    @Test
    public void map_validInput_validOutput() throws StringFrequencyMapException {

        String redisKey = new StringBuilder()
                .append(AppConstant.REDIS_STORE_NAMESPACE).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append("abc")
                .toString();

        StringFrequency stringFrequency = stringFrequencyMapper.map(redisKey);

        assertThat(stringFrequency.getType(),is(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR));
        assertThat(stringFrequency.getStringId(),is("abc"));
    }

    @Test(expected = StringFrequencyMapException.class)
    public void map_incompleteRedisKey_throwStringFrequencyMapException() throws StringFrequencyMapException {

        String redisKey = new StringBuilder()
                .append(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR).append(AppConstant.REDIS_NAMESPACE_SEPARATOR)
                .append("abc")
                .toString();

        StringFrequency stringFrequency = stringFrequencyMapper.map(redisKey);
    }

    @Test(expected = StringFrequencyMapException.class)
    public void map_incorrectRedisNamespaceSeparator_throwStringFrequencyMapException() throws StringFrequencyMapException {

        String redisKey = new StringBuilder()
                .append(AppConstant.REDIS_STORE_NAMESPACE).append(";")
                .append(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR).append(";")
                .append("abc")
                .toString();

        StringFrequency stringFrequency = stringFrequencyMapper.map(redisKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void map_invalidInput_illegalArguemtnException() throws StringFrequencyMapException {
        stringFrequencyMapper.map("");
    }
}