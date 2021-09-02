package com.trgr.berkeleydb.testcases;

import java.util.Map;

public interface DBRepository<K, V> {
	Map<K, V> getSValues(final Iterable<K> keys);
}
