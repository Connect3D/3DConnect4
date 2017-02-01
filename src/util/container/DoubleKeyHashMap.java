package util.container;

import com.google.common.collect.HashBiMap;



public class DoubleKeyHashMap<K, V> {

	private HashBiMap<K, V> map1 = HashBiMap.create();			
	private HashBiMap<K, V> map2 = HashBiMap.create();
	
	
	public void put(K key1, K key2, V val) {
		map1.put(key1, val);
		map2.put(key2, val);
	}
	
	
	public V getValue(K key) {
		if (map1.containsKey(key)) {
			return map1.get(key);
		}
		if (map2.containsKey(key)) {
			return map2.get(key);
		}
		return null;
	}
	
	
	public Pair<K, K> getKeys(V val) {
		return new Pair<K, K>(map1.inverse().get(val), map2.inverse().get(val));
	}
	
	
	public K getOtherKey(K key) {
		Pair<K, K> keys = getKeys(getValue(key));
		if (key == keys.first) {
			return keys.second;
		}
		if (key == keys.second) {
			return keys.first;
		}
		return null;
	}
	
	
	public void remove(V val) {
		Pair<K, K> keys = getKeys(val);
		map1.remove(keys.first);
		map2.remove(keys.second);
	}
	
	
	public boolean hasKey(K key) {
		return map1.containsKey(key) || map2.containsKey(key);
	}
	
	
	public boolean hasValue(V val) {
		return map1.containsValue(val) || map2.containsValue(val);
	}
	
}
