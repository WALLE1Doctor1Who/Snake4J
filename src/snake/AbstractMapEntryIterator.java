/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.util.*;

/**
 * This is an abstract implementation for an Iterator that goes through the 
 * entries in a map. This provides default implementations for the {@link #next 
 * next} and {@link #remove remove} methods, so as to minimize the effort 
 * required to implement an Iterator for the {@code Set} returned by a map's 
 * {@code entrySet} method when using the keys in the map to iterate through the 
 * map's entries. <p>
 * 
 * To implement the iterator for an unmodifiable map, the programmer needs only 
 * to provide an implementation for the {@code hasNext}, {@code getNextKey}, and 
 * {@code getNextEntry} methods. The {@code getNextKey} method is used by the 
 * {@code next} method to get the key to pass to the {@code getNextEntry} 
 * method, which returns the next entry in the backing map to return. <p>
 * 
 * To implement the iterator for a modifiable map, the the programmer must 
 * additionally override the {@code removeEntry} method (which otherwise throws 
 * an {@code UnsupportedOperationException}). The {@code removeEntry} method is 
 * used by the {@code remove} method to remove the last entry returned by the 
 * {@code next} method from the backing map. <p>
 * 
 * To implement a fail-fast iterator (i.e. an iterator for which if the backing 
 * map is structurally modified in any way at any time after the iterator is 
 * created except via the iterator's own {@code remove} method, then the 
 * iterator will generally throw a {@link ConcurrentModificationException 
 * ConcurrentModificationException}), the programmer should override the 
 * protected {@code checkForConcurrentModification} method to throw a {@code 
 * ConcurrentModificationException} if the backing map has been modified 
 * externally. This way, when faced with concurrent modification of the backing 
 * map, the iterator will fail quickly and cleanly instead of risking arbitrary, 
 * non-deterministic behavior. Note that the fail-fast behavior of an iterator 
 * cannot be guaranteed, especially when dealing with unsynchronized concurrent 
 * modifications. Fail-fast iterators throw {@code 
 * ConcurrentModificationException}s on a best-effort basis. As such the 
 * fail-fast behavior should not be depended on for its correctness and should 
 * only be used to detect bugs.
 * 
 * @author Milo Steier
 * @param <K> The type of the key for the entries to iterate over.
 * @param <V> The type of the mapped values for the entries to iterate over.
 * @since 1.1.0
 * @see Map
 * @see Map.Entry
 * @see Map#entrySet 
 */
public abstract class AbstractMapEntryIterator<K,V> implements 
        Iterator<Map.Entry<K,V>>{
    /**
     * This stores the map entry that was most recently returned by {@link next 
     * next}. This will be null if either {@code next} has not been called or if 
     * {@link #remove remove} was just called.
     */
    private Map.Entry<K,V> current = null;
    /**
     * This returns {@code true} if the iteration has more elements. (In other 
     * words, this returns {@code true} if {@link #next() next} will return an 
     * element instead of throwing an exception.)
     * @return {@code true} if the iteration has more elements.
     */
    @Override
    public abstract boolean hasNext();
    /**
     * This returns the key for the next entry for the {@link #next next} method 
     * to return. This method is called by {@code next} to get the key to pass 
     * to {@link #getNextEntry getNextEntry} to get the next entry to return. 
     * This method can assume that there are still elements in the iteration. 
     * @return The key for the entry for the {@code next} method to return.
     * @see #next 
     * @see #getNextEntry 
     */
    protected abstract K getNextKey();
    /**
     * This returns the next entry for the {@link #next next} method to return. 
     * This method is called by {@code next} to get the next entry to return. 
     * This method can assume that there are still elements in the iteration and 
     * that the map has not been structurally modified. 
     * @param key The key for the entry to return,
     * @return The entry for the {@code next} method to return.
     * @see #next 
     * @see #getNextKey 
     */
    protected abstract Map.Entry<K,V> getNextEntry(K key);
    /**
     * This returns the next element in the iteration.
     * @return The next element in the iteration.
     * @throws NoSuchElementException If the iteration has no more elements.
     * @throws ConcurrentModificationException If the map has been structurally 
     * modified while the iteration is in progress in any way other than via the 
     * {@code remove} method of this iterator.
     */
    @Override
    public Map.Entry<K, V> next() {
        if (!hasNext())             // If there are no more entries left
            throw new NoSuchElementException();
        K key = getNextKey();       // Get the key for the next entry
            // Check for any concurrent modication of the map
        checkForConcurrentModification(key);
        current = getNextEntry(key);// Get the next entry to return
        return current;
    }
    /**
     * This removes the given entry from the map. This is called by {@link 
     * #remove remove} to remove the last entry returned by this iterator. This 
     * method can assume that {@code next} has been called before {@code 
     * remove}, and that the map has not been structurally modified. 
     * 
     * @implSpec The default implementation does nothing and throws an {@code 
     * UnsupportedOperationException}.
     * 
     * @param entry The entry to remove from the map. 
     * @throws UnsupportedOperationException If the removal of entries is not 
     * supported by this iterator.
     * @see #remove 
     */
    protected void removeEntry(Map.Entry<K,V> entry){
        Iterator.super.remove();
    }
    /**
     * This removes the last element returned by this iterator from the 
     * underlying map. This method can be called only once per call to {@link 
     * #next next}. <p>
     * 
     * If the underlying map is structurally modified while the iteration is in 
     * progress in any way other than calling this method will result in this 
     * iterator throwing a {@code ConcurrentModificationException}. <p>
     * 
     * The behavior of this iterator is unspecified if this method is called 
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     * 
     * @throws IllegalStateException If either the {@code next} method has not 
     * been called yet or this method has already been called since the last 
     * call to the {@code next} method.
     * @throws ConcurrentModificationException If the map has been structurally 
     * modified while the iteration is in progress in any way other than via 
     * this method.
     * @throws UnsupportedOperationException If the {@code remove} operation is 
     * not supported by this iterator.
     */
    @Override
    public void remove() {
            // If the current map entry is null (this is the case if either next 
            // has not been called yet, or if this was just called)
        if (current == null)    
            throw new IllegalStateException();
            // Check for any concurrent modication of the map
        checkForConcurrentModification(current.getKey());
        removeEntry(current);   // Remove the entry
            // Set current to null to indicate the entry was removed
        current = null;         
    }
    /**
     * This checks to see if this map has been modified externally since this 
     * iterator was constructed, and if so, throws a {@code 
     * ConcurrentModificationException}.
     * 
     * @implSpec The default implementation does nothing.
     * 
     * @param key The key for the entry to return (if {@code next} was called) 
     * or remove from the map (if {@code remove} was called).
     * @throws ConcurrentModificationException If the map was structurally 
     * modified since this iterator was constructed.
     * @see #next 
     * @see #remove 
     */
    protected void checkForConcurrentModification(K key){ }
}
