package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StringFrequencyUpdaterTest {

    @Autowired
    private StringFrequencyRepository stringFrequencyRepository;

    @Autowired
    private StringFrequencyGetter stringFrequencyGetter;

    @Autowired
    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    private StringFrequencyUpdater stringFrequencyUpdater;


    @Before
    public void setUp() throws Exception {

        stringFrequencyUpdater = new StringFrequencyUpdater(stringFrequencyRepository);
        stringFrequencyGetter = new StringFrequencyGetter(stringFrequencyRepository,hourlyStringFrequencyMapper);
    }

    @Test
    public void incrementCountForStringId() {

        StringFrequency stringFrequency = new Past24HourStringFrequency(UUID.randomUUID().toString(),2);

        stringFrequencyUpdater.incrementCountForStringId(stringFrequency);

        StringFrequency output = stringFrequencyGetter.getByStringFrequency(stringFrequency).get();

        assertThat(output.getCounter(),is(2));
    }

    @Test
    public void decrementCountForStringId() {

        StringFrequency stringFrequency = new Past24HourStringFrequency(UUID.randomUUID().toString(),2);

        stringFrequencyUpdater.incrementCountForStringId(stringFrequency);

        stringFrequencyUpdater.decrementCountForStringId(new Past24HourStringFrequency(stringFrequency.getStringId(),1));

        StringFrequency output = stringFrequencyGetter.getByStringFrequency(stringFrequency).get();

        assertThat(output.getCounter(),is(1));

    }

    @Test
    public void removeStringId() {

        StringFrequency stringFrequency = new Past24HourStringFrequency(UUID.randomUUID().toString(),2);

        stringFrequencyUpdater.incrementCountForStringId(stringFrequency);

        stringFrequencyUpdater.removeStringId(stringFrequency);

        Optional<StringFrequency> output = stringFrequencyGetter.getByStringFrequency(stringFrequency);

        assertThat(output,is(Optional.empty()));

    }
}