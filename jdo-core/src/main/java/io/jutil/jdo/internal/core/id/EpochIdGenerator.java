package io.jutil.jdo.internal.core.id;

import java.time.LocalDate;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public interface EpochIdGenerator<T> extends IdGenerator<T> {

	void setEpochDate(LocalDate date);

}
