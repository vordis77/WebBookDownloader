package app.tools;

import java.util.HashMap;

/**
 * Bi directional hash map - allow to easily get key for value. IMPORTANT NOTE:
 * It is fast, but holds double the size. TODO: maybe it could be done with some
 * hacking on entries (if they allow for fetching over indexes).
 */
public class BiHashMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 1L;

	HashMap<V, K> reverseMap = new HashMap<>();

	@Override
	public V put(K key, V value) {
		reverseMap.put(value, key);
		return super.put(key, value);
	}

	public K getKey(V value) {
		return reverseMap.get(value);
	}

}