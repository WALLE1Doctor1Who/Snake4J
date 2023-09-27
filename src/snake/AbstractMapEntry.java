/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.util.*;

/**
 * This is an abstract implementation for an Entry in a map which represents a 
 * mapping for a specified key. To implement an immutable entry, the programmer 
 * needs only to provide an implementation for the {@code getValue} method. To 
 * implement a modifiable entry, the programmer must additionally override the 
 * {@code setValue} method, which otherwise throws an {@code 
 * UnsupportedOperationException}.
 * @author Milo Steier
 * @param <K> The type of the key for this entry.
 * @param <V> The type of the mapped value.
 * @since 1.1.0
 * @see Map
 * @see Map.Entry
 * @see AbstractMap.SimpleEntry
 * @see AbstractMap.SimpleImmutableEntry
 * @see MapBackedEntry
 */
public abstract class AbstractMapEntry<K,V> implements Map.Entry<K,V>{
    /**
     * This stores the key for this entry.
     */
    protected final K key;
    /**
     * This constructs an AbstractMapEntry representing a mapping with the given 
     * key. 
     * @param key The key for this entry.
     */
    public AbstractMapEntry(K key){
        this.key = key;
    }
    /**
     * This returns the key that corresponds to this entry.
     * @return The key corresponding to this entry.
     */
    @Override
    public K getKey() {
        return key;
    }
    /**
     * This returns the value corresponding to this entry. If this mapping has 
     * been removed from the backing map, then the results of this call are 
     * undefined.
     * @return The value corresponding to this entry.
     * @throws IllegalStateException Implementations may, but are not required 
     * to, throw this exception if this entry has been removed from the backing 
     * map.
     */
    @Override
    public abstract V getValue();
    /**
     * This replaces the value corresponding to this entry with the specified 
     * value (optional operation). This writes through to the map. The behavior 
     * of this call is undefined if this mapping has already been removed from 
     * the map.
     * 
     * @implSpec The default implementation always throws an {@code 
     * UnsupportedOperationException}.
     * 
     * @param value The new value to be stored in this entry.
     * @return The old value corresponding to this entry.
     * @throws UnsupportedOperationException If either the {@code put} operation 
     * is not supported by the backing map or if the {@code setValue} operation 
     * is not supported by this Entry.
     * @throws ClassCastException If the class of the given value prevents it 
     * from being stored in the backing map.
     * @throws NullPointerException If the given value is null and the backing 
     * map does not permit null values.
     * @throws IllegalArgumentException If some property of the value prevents 
     * it from being stored in the backing map.
     * @throws IllegalStateException Implementations may, but are not required 
     * to, throw this exception if this entry has been removed from the backing 
     * map.
     */
    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("setValue");
    }
    /**
     * This compares the specified object with this map entry for equality. 
     * This will return {@code true} if the given object is also a map entry 
     * and the two entries represent the same mapping. More formally, two 
     * entries {@code e1} and {@code e2} represent the same mapping if: 
     * <pre> {@code 
     *      Objects.equals(e1.getKey(),e2.getKey()) && 
     *        Objects.equals(e1.getValue(),e2.getValue())}</pre>
     * @param o The object to be compared for equality with this map entry. 
     * @return Whether the specified object is equal to this map entry.
     */
    @Override
    public boolean equals(Object o){
            // If the given object is a map entry
        if (o instanceof Map.Entry){
                // Get the object as a map entry
            Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
                // Check to see if its key is the same as this entry's key and 
                // that its value is the same as this entry's value
            return Objects.equals(getKey(), entry.getKey()) && 
                    Objects.equals(getValue(), entry.getValue());
        }
        return false;
    }
    /**
     * This returns the hash code for this map entry. The hash code of a map 
     * entry is defined to be: 
     * <pre> {@code 
     *      Objects.hashCode(getKey()) ^ Objects.hashCode(getValue())}</pre>
     * 
     * @return The hash code for this entry.
     */
    @Override
    public int hashCode(){
        return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
    }
    /**
     * This returns a String representation of this map entry.
     * @return A String representation of this map entry.
     */
    @Override
    public String toString(){
        return getKey() + "=" + getValue();
    }
}
