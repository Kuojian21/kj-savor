package com.kj.savor.base.helper;

import java.util.Map;
import com.google.common.collect.Maps;

/**
 * 
 * @author kuojian21
 *
 */
public class MapHelper {

	public static Map<String, Object> newHashMap(Object... objs) {
		Map<String, Object> result = Maps.newHashMap();
		for (int i = 0, len = objs.length; i < len; i += 2) {
			result.put(objs[i].toString(), objs[i + 1]);
		}
		return result;
	}

}
