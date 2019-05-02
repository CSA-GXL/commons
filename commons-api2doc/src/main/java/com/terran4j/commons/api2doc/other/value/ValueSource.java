package com.terran4j.commons.api2doc.other.value;

public interface ValueSource<K, V> {

	V get(K key);
	
}
