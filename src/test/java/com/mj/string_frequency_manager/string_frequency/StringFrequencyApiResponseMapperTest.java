package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.api.StringFrequencyApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class StringFrequencyApiResponseMapperTest {

    private StringFrequencyApiResponseMapper stringFrequencyApiResponseMapper;

    @Before
    public void setUp() throws Exception {
        stringFrequencyApiResponseMapper = new StringFrequencyApiResponseMapper();
    }

    @Test
    public void map_withStringFrequencyCounterMoreThan5_returnTrue() {

        StringFrequencyApiResponse response =stringFrequencyApiResponseMapper.map(
                new StringFrequency(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR,"abc",6));

        assertThat(response.getResponse(),is("true"));

    }

    @Test
    public void map_withStringFrequencyCounterLessThan5_returnFalse() {

        StringFrequencyApiResponse response =stringFrequencyApiResponseMapper.map(
                new StringFrequency(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR,"abc",3));

        assertThat(response.getResponse(),is("false"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void map_invalidInput_throwIllegalArgumentException() {
        stringFrequencyApiResponseMapper.map(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void map_invalidStringFrequencyCounter_throwIllegalArgumentException() {
        stringFrequencyApiResponseMapper.map(new StringFrequency());
    }
}