package io.jutil.jdo.core.convert;

import io.jutil.jdo.core.collection.Singleton;
import io.jutil.jdo.internal.core.convert.DefaultConvertService;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class ConvertServiceFactory {

	private ConvertServiceFactory() {
	}

	public static ConvertService getConvertService() {
		return Singleton.get(ConvertService.class, k -> new DefaultConvertService());
	}

}
