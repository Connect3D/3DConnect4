package util.container;

import com.google.common.collect.HashBiMap;



public class DoubleKeyHashMap<KEY, VAL> {

	private HashBiMap<KEY, VAL> map1 = HashBiMap.create();			// TODO look if hashmap is possible instead of HashBiMap
	private HashBiMap<KEY, VAL> map2 = HashBiMap.create();
	
	
	public void put(KEY key1, KEY key2, VAL val) {
		map1.put(key1, val);
		map2.put(key2, val);
	}
	
	
	public VAL getValue(KEY key) {
		if (map1.containsKey(key)) return map1.get(key);
		if (map2.containsKey(key)) return map2.get(key);
		return null;
	}
	
	
	public Pair<KEY, KEY> getKeys(VAL val) {
		return new Pair<KEY, KEY>(map1.inverse().get(val), map2.inverse().get(val));
	}
	
	
	public KEY getOtherKey(KEY key) {
		Pair<KEY, KEY> keys = getKeys(getValue(key));
		if (key == keys.first) return keys.second;
		if (key == keys.second) return keys.first;
		return null;
	}
	
	
	public void remove(VAL val) {
		Pair<KEY, KEY> keys = getKeys(val);
		map1.remove(keys.first);
		map2.remove(keys.second);
	}
	
	public boolean hasKey(KEY key) {
		return map1.containsKey(key) || map2.containsKey(key);
	}
	
	public boolean hasValue(VAL val) {
		return map1.containsValue(val) || map2.containsValue(val);
	}
	
}
