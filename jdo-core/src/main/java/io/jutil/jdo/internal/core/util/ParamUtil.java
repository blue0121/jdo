package io.jutil.jdo.internal.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class ParamUtil {
	private ParamUtil() {
	}

	public static List<Object> toParamList(Map<String, ?> map, List<String> nameList, boolean dynamic) {
		List<Object> valueList = new ArrayList<>();
		for (var name : nameList) {
			var value = ObjectUtil.convert(map.get(name));
			if (dynamic && value == null) {
				continue;
			}
			valueList.add(value);
		}
		return valueList;
	}

	public static List<Object> convert(List<?> list) {
		List<Object> valueList = new ArrayList<>();
		for (var obj : list) {
			valueList.add(ObjectUtil.convert(obj));
		}
		return valueList;
	}

}
