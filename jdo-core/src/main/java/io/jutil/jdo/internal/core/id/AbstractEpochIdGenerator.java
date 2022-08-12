package io.jutil.jdo.internal.core.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public abstract class AbstractEpochIdGenerator<T> implements EpochIdGenerator<T> {
    private static Logger logger = LoggerFactory.getLogger(AbstractEpochIdGenerator.class);

    protected long epoch;

    protected AbstractEpochIdGenerator() {
        var date = LocalDate.of(2022, 1, 1);
        this.setEpochDate(date);
    }

    @Override
    public void setEpochDate(LocalDate date) {
        this.epoch = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
