/**
 * Copyright 2011 Locomatix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locomatix.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Multimap<K, V> {
    // Query Operations

    /** Returns the number of key-value pairs in the multimap. */
    int size();

    /** Returns {@code true} if the multimap contains no key-value pairs. */
    boolean isEmpty();

    /**
     * Returns {@code true} if the multimap contains any values for the specified
     * key.
     *
     * @param key key to search for in multimap
     */
    boolean containsKey(Object key);

    /**
     * Returns {@code true} if the multimap contains the specified value for any
     * key.
     *
     * @param value value to search for in multimap
     */
    boolean containsValue(Object value);

    /**
     * Returns {@code true} if the multimap contains the specified key-value pair.
     *
     * @param key key to search for in multimap
     * @param value value to search for in multimap
     */
    boolean containsEntry(Object key, Object value);

    // Modification Operations

    /**
     * Stores a key-value pair in the multimap.
     *
     * <p>Some multimap implementations allow duplicate key-value pairs, in which
     * case {@code put} always adds a new key-value pair and increases the
     * multimap size by 1. Other implementations prohibit duplicates, and storing
     * a key-value pair that's already in the multimap has no effect.
     *
     * @param key key to store in the multimap
     * @param value value to store in the multimap
     * @return {@code true} if the method increased the size of the multimap, or
     *     {@code false} if the multimap already contained the key-value pair and
     *     doesn't allow duplicates
     */
    boolean put(K key, V value);

    /**
     * Removes a key-value pair from the multimap.
     *
     * @param key key of entry to remove from the multimap
     * @param value value of entry to remove the multimap
     * @return {@code true} if the multimap changed
     */
    boolean remove(Object key, Object value);

    // Bulk Operations

    /**
     * Stores a collection of values with the same key.
     *
     * @param key key to store in the multimap
     * @param values values to store in the multimap
     * @return {@code true} if the multimap changed
     */
    boolean putAll(K key, Iterable<? extends V> values);

    
    /**
     * Removes all values associated with a given key.
     *
     * @param key key of entries to remove from the multimap
     * @return the collection of removed values, or an empty collection if no
     *     values were associated with the provided key. The collection
     *     <i>may</i> be modifiable, but updating it will have no effect on the
     *     multimap.
     */
    Collection<V> removeAll(Object key);

    /**
     * Removes all key-value pairs from the multimap.
     */
    void clear();

    
    
    // Views

    /**
     * Returns the first value associated with a key.
     * 
     * @param key key to search for in mulitmap
     * @return the first value of the collection
     */
    V getFirst(K key);
    
    
    /**
     * Returns a collection view of all values associated with a key. If no
     * mappings in the multimap have the provided key, an empty collection is
     * returned.
     *
     * <p>Changes to the returned collection will update the underlying multimap,
     * and vice versa.
     *
     * @param key key to search for in multimap
     * @return the collection of values that the key maps to
     */
    Collection<V> get(K key);

    /**
     * Returns the set of all keys, each appearing once in the returned set.
     * Changes to the returned set will update the underlying multimap, and vice
     * versa.
     *
     * @return the collection of distinct keys
     */
    Set<K> keySet();

   
    /**
     * Returns a collection of all values in the multimap. Changes to the returned
     * collection will update the underlying multimap, and vice versa.
     *
     * @return collection of values, which may include the same value multiple
     *     times if it occurs in multiple mappings
     */
    Collection<V> values();

    /**
     * Returns a collection of all key-value pairs. Changes to the returned
     * collection will update the underlying multimap, and vice versa. The entries
     * collection does not support the {@code add} or {@code addAll} operations.
     *
     * @return collection of map entries consisting of key-value pairs
     */
    Collection<Map.Entry<K, V>> entries();

    /**
     * Returns a map view that associates each key with the corresponding values
     * in the multimap. Changes to the returned map, such as element removal, will
     * update the underlying multimap. The map does not support {@code setValue()}
     * on its entries, {@code put}, or {@code putAll}.
     *
     * <p>When passed a key that is present in the map, {@code
     * asMap().get(Object)} has the same behavior as {@link #get}, returning a
     * live collection. When passed a key that is not present, however, {@code
     * asMap().get(Object)} returns {@code null} instead of an empty collection.
     *
     * @return a map view from a key to its collection of values
     */
    Map<K, Collection<V>> asMap();

    // Comparison and hashing

    /**
     * Compares the specified object with this multimap for equality. Two
     * multimaps are equal when their map views, as returned by {@link #asMap},
     * are also equal.
     *
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code for this multimap.
     *
     * <p>The hash code of a multimap is defined as the hash code of the map view,
     * as returned by {@link Multimap#asMap}.
     */
    @Override
    int hashCode();
 
}
