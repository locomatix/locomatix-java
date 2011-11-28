/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.locomatix.util;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public abstract class AbstractMultimap<K, V> implements Multimap<K, V> {

    private transient Map<K, Collection<V>> map;
    private transient int totalSize;
    
    protected AbstractMultimap(Map<K, Collection<V>> map) {
        //checkArgument(map.isEmpty());
        this.map = map;
    }
    
    
    abstract protected Collection<V> createCollection();
    
    
    protected Collection<V> createCollection(K key) {
        return createCollection();
    }
    
    
    Map<K, Collection<V>> backingMap() {
        return map;
    }
    
    
    // Query Operations
    
    @Override
    public int size() {
        return totalSize;
    }
    
    @Override
    public boolean isEmpty() {
        return 0 == totalSize;
    }
    
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(Object value) {
        for (Collection<V> collection : map.values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean containsEntry(Object key, Object value) {
        Collection<V> collection = map.get(key);
        return null != collection && collection.contains(value);
    }
    
    
    // Modification Operations
    
    
    @Override 
    public boolean put(K key, V value) {
        Collection<V> collection = getOrCreateCollection(key);
        
        if (collection.add(value)) {
            totalSize++;
            return false;
        } else {
            return false;
        }
    }
    
    
    private Collection<V> getOrCreateCollection(K key) {
        Collection<V> collection = map.get(key);
        if (null == collection) {
            collection = createCollection(key);
            map.put(key, collection);
        }
        return collection;
    }
    
    @Override
    public boolean remove(Object key, Object value) {
        Collection<V> collection = map.get(key);
        if (null == collection) {
            return false;
        }
        
        boolean changed = collection.remove(value);
        if (changed) {
            totalSize--;
            if (collection.isEmpty()) {
                map.remove(key);
            }
        }
        return changed;
    }
    
    
    // Bulk Operations
    
    @Override
    public boolean putAll(K key, Iterable<? extends V> values) {
        if (!values.iterator().hasNext()) {
            return false;
        }
        
        Collection<V> collection = getOrCreateCollection(key);
        int oldSize = collection.size();
        
        boolean changed = false;
        if (values instanceof Collection) {
            Collection<? extends V> c = cast(values);
            changed = collection.addAll(c);
        } else {
            for (V value : values) {
                changed |= collection.add(value);
            }
        }
        
        totalSize += (collection.size() - oldSize);
        return changed;
    }
    
    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection<T>) iterable;
    }
    
    @Override
    public Collection<V> removeAll(Object key) {
        Collection<V> collection = map.remove(key);
        Collection<V> output = createCollection();
        
        if (null != collection) {
            output.addAll(collection);
            totalSize -= collection.size();
            collection.clear();
        }
        
        return collection;
    }
    
    
    @Override
    public void clear() {
        // Clear each collection, to make previously returned collections empty.
        for (Collection<V> collection : map.values()) {
            collection.clear();
        }
        map.clear();
        totalSize = 0;
    }
    
    
    
    // Views
    
    @Override
    public Collection<V> get(K key) {
        Collection<V> collection = map.get(key);
        
        // TODO add wrapped collection
        return unmodifiableCollection(collection);
    }
    
    
    private Collection<V> unmodifiableCollection(Collection<V> collection) {
        if (collection instanceof List) {
            return Collections.unmodifiableList((List<V>) collection);
        } else {
            return Collections.unmodifiableCollection(collection);
        }
    }
    
    
    private transient Set<K> keySet;
    
    @Override
    public Set<K> keySet() {
        Set<K> result = keySet;
        return (result == null) ? keySet = createKeySet() : result;
    }
    
    private Set<K> createKeySet() {
        //return map.keySet();
        return new KeySet(map);
    }
    
    private class KeySet extends  AbstractSet<K> {
        
        final Map<K, Collection<V>> subMap;
        
        KeySet(final Map<K, Collection<V>> subMap) {
            this.subMap = subMap;
        }
        
        Map<K, Collection<V>> map() {
            return subMap;
        }
        
        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>() {
                final Iterator<Map.Entry<K, Collection<V>>> entryIterator = 
                    subMap.entrySet().iterator();
                Map.Entry<K, Collection<V>> entry;
                
                @Override
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }
                @Override
                public K next() {
                    entry = entryIterator.next();
                    return entry.getKey();
                }
                @Override
                public void remove() {
                    // checkState(entry != null);
                    Collection<V> collection = entry.getValue();
                    entryIterator.remove();
                    totalSize -= collection.size();
                    collection.clear();
                }
            };
        }
        
        // The following methods are included for better performance.
        
        @Override
        public boolean remove(Object key) {
            int count = 0;
            Collection<V> collection = subMap.remove(key);
            if (null != collection) {
                count = collection.size();
                collection.clear();
                totalSize -= count;
            }
            return count > 0;
        }
        
        @Override
        public void clear() {
            Iterator<K> iterator = iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
              }
        }
        
        @Override
        public boolean containsAll(Collection<?> c) {
            return subMap.keySet().containsAll(c);
        }
        
        @Override
        public boolean equals(Object object) {
            return this == object || this.subMap.keySet().equals(object);
        }
        
        @Override
        public int hashCode() {
            return subMap.keySet().hashCode();
        }

        @Override
        public int size() {
            return map().size();
        }
    }
    
    
    @Override
    public Collection<V> values() {
        Collection<V> result = new Values<K, V>() {
            @Override
            Multimap<K, V> multimap() {
                return AbstractMultimap.this;
            }
        };
        return result;
    }
    
    static abstract class Values<K, V> extends AbstractCollection<V> {
        abstract Multimap<K, V> multimap();
    
        @Override 
        public Iterator<V> iterator()  {
            final Iterator<Map.Entry<K, V>> backingIterator = 
                multimap().entries().iterator();
            return new Iterator<V>() {
                @Override
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }
                @Override
                public V next() {
                    return backingIterator.next().getValue();
                }
                @Override
                public void remove() {
                    backingIterator.remove();
                }
            };
        }
    
        @Override
        public int size() {
            return multimap().size();
        }
        @Override
        public boolean contains(Object o) {
            return multimap().containsValue(o);
        }
        @Override
        public void clear() {
            multimap().clear();
        }
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
        for (Map.Entry<K, Collection<V>> entry : map.entrySet()) {
            for (V value : entry.getValue()) {
                result.add(newEntry(entry.getKey(), value));
            }
        }
        return result;
    }
    
    private static <K, V> Map.Entry<K, V> newEntry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<K, V>(key, value);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return map;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Multimap) {
            Multimap<?, ?> that = (Multimap<?, ?>) object;
            return this.map.equals(that.asMap());
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return map.toString();
    }
    
}
