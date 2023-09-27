/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.util.*;

/**
 * This is an implementation for an Entry in a map which represents a mapping 
 * for a specified key and which is backed by the map. The {@code getMap} method 
 * can be used to get the backing map for this entry. The implementation for the 
 * {@code getValue} and {@code setValue} methods delegates to the backing map's 
 * {@link Map#get get} and {@link Map#put put} methods, respectively. If the 
 * backing map does not support the {@code put} operation, then the entry will 
 * be immutable. Both the {@code getValue} and {@code setValue} methods will 
 * throw an {@code IllegalStateException} if the entry is removed from the 
 * backing map, as determined by whether the backing map contains a mapping for 
 * the entry's key. However, the {@code getKey} method does not throw an 
 * exception if the entry is removed. In other words, if the backing map's 
 * {@link Map#containsKey containsKey} method would return {@code false} for the 
 * entry's key, then the {@code getValue} and {@code setValue} methods will 
 * throw an {@code IllegalStateException} while the {@code getKey} method will 
 * remain unaffected. <p>
 * 
 * This Entry is meant for and should only be used with Maps that do not use 
 * entries for their implementation of the {@code get}, {@code put}, and {@code 
 * containsKey} methods. That is to say, the backing map's {@code get}, {@code 
 * put}, and {@code containsKey} methods should not make use of the {@code 
 * getValue} and {@code setValue} methods of a given entry, as doing so will 
 * typically result in an infinite loop of recursion. As such, this class is 
 * incompatible with the default implementation for {@code get}, {@code put}, 
 * and {@code containsKey} in {@link AbstractMap}.
 * 
 * @author Milo Steier
 * @param <K> The type of the key for this entry.
 * @param <V> The type of the mapped value.
 * @since 1.1.0
 * @see Map
 * @see Map.Entry
 * @see AbstractMap.SimpleEntry
 * @see AbstractMap.SimpleImmutableEntry
 * @see AbstractMapEntry
 */
public class MapBackedEntry<K,V> extends AbstractMapEntry<K,V>{
    /**
     * This is the map in which this entry resides in and is backed by.
     */
    private final Map<K,V> map;
    /**
     * This constructs a MapBackedEntry representing a mapping with the given 
     * key and which is backed by the given map.
     * @param key The key for this entry.
     * @param map The map backing this entry (cannot be null).
     * @throws NullPointerException If the backing map is null.
     */
    public MapBackedEntry(K key, Map<K,V> map) {
        super(key);
        this.map = Objects.requireNonNull(map);
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec This implementation is equivalent to, for this {@code entry}:
     * 
     * <pre> {@code 
     * if (entry.getMap().containsKey(entry.getKey()))
     *     return entry.getMap().get(entry.getKey());
     * else
     *     throw new IllegalStateException();
     * }</pre>
     * 
     * This implementation makes no guarantees about this method not entering an 
     * infinite recursive loop if the backing map uses this class for its 
     * entries and uses its entries to check if it contains a given key and/or 
     * to retrieve the value mapped to that key. 
     * 
     * @throws IllegalStateException If this entry has been removed from the 
     * backing map, as determined by whether the backing map contains the key 
     * for this entry.
     * @see #getMap 
     * @see #getKey 
     * @see Map#containsKey 
     * @see Map#get 
     */
    @Override
    public V getValue() {
        checkIfRemoved();       // Check if this entry has been removed
        return getMap().get(getKey());
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec This implementation is equivalent to, for this {@code entry}:
     * 
     * <pre> {@code 
     * if (entry.getMap().containsKey(entry.getKey()))
     *     return entry.getMap().put(entry.getKey(), value);
     * else
     *     throw new IllegalStateException();
     * }</pre>
     * 
     * This implementation makes no guarantees about this method not entering an 
     * infinite recursive loop if the backing map uses this class for its 
     * entries and uses its entries to check if it contains a given key, to 
     * retrieve the value mapped to that key, and/or to map a value to that key.
     * 
     * @throws IllegalStateException If this entry has been removed from the 
     * backing map, as determined by whether the backing map contains the key 
     * for this entry.
     * @throws ClassCastException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     * @throws IllegalArgumentException {@inheritDoc }
     * @throws UnsupportedOperationException If the {@code put} operation is not 
     * supported by the backing map.
     * @see #getMap 
     * @see #getKey 
     * @see Map#containsKey 
     * @see Map#put 
     */
    @Override
    public V setValue(V value) {
        checkIfRemoved();       // Check if this entry has been removed
        return getMap().put(getKey(), value);
    }
    /**
     * This returns the map in which this entry resides in and is backed by.
     * @return The map containing this entry.
     */
    public Map<K,V> getMap(){
        return map;
    }
    /**
     * This checks to see if this entry has been removed from the {@link 
     * #getMap backing map}, and if so, throws an {@code 
     * IllegalStateException}. 
     * 
     * @implSpec The default implementation is is equivalent to:
     * 
     * <pre> {@code 
     * if (!getMap().containsKey(getKey()))
     *     throw new IllegalStateException();
     * }</pre>
     * 
     * Subclasses that are interested in changing the way entries check as to 
     * whether they have been removed or not, or that are interested in removing 
     * the check altogether should override this method.
     * 
     * @throws IllegalStateException If this entry has been removed from the 
     * backing map.
     * @see #getKey 
     * @see #getMap 
     * @see Map#containsKey 
     */
    protected void checkIfRemoved(){
            // If the backing map does not contain a mapping for the key
        if (!getMap().containsKey(key))
            throw new IllegalStateException("Entry for key (" + key + 
                    ") has been removed from the map.");
    }
}
