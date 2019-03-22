package tech.hongjian.funny_server.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author xiahongjian 
 * @time   2019-03-04 16:11:56
 *
 */
public class DoubleKeyMap<K1, K2, V> {
	private Map<K1, Map<K2, V>> map = new HashMap<>();
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public int size() {
		int count = 0;
		for (Entry<K1, Map<K2, V>> entry : map.entrySet()) {
			Map<K2, V> value = entry.getValue();
			if (value != null)
				count += value.size();
		}
		return count;
	}
	
	public boolean contains(K1 key1, K2 key2) {
		if (!map.containsKey(key1))
			return false;
		Map<K2, V> value = map.get(key1);
		return value != null && value.containsKey(key2);
	}
	
	public V put(K1 key1, K2 key2, V value) {
		Map<K2, V> vMap;
		if (map.containsKey(key1)) {
			vMap = map.get(key1);
		} else {
			vMap = new HashMap<>();
			map.put(key1, vMap);
		}
		return vMap.put(key2, value);
	}
	
	public V get(K1 key1, K2 key2) {
		if (!map.containsKey(key1))
			return null;
		return map.get(key1).get(key2);
	}
	
	public Map<K2, V> get(K1 key1) {
		return map.get(key1);
	}
	
	public Set<K1> getKeySet() {
		Set<K1> keys = new HashSet<>();
		map.entrySet().forEach(e -> {
			keys.add(e.getKey());
		});
		return keys;
	}
	
	public V remove(K1 key1, K2 key2) {
		if (!map.containsKey(key1))
			return null;
		Map<K2, V> vMap = map.get(key1);
		if (vMap == null || vMap.isEmpty())
			return null;
		if (!vMap.containsKey(key2))
			return null;
		if (vMap.size() == 1) {
			V value = vMap.get(key2);
			map.remove(key1);
			return value;
		} else {
			return vMap.remove(key2);
		}
	}
	
	
}
