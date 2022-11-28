/**
 * 
 */
package it.unicam.cs.asdl2223.mp2;

import it.unicam.cs.asdl2223.es7.SingleLinkedList;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the Java SE Double-ended Queue (Deque) interface
 * (<code>java.util.Deque</code>) based on a double linked list. This deque does
 * not have capacity restrictions, i.e., it is always possible to insert new
 * elements and the number of elements is unbound. Duplicated elements are
 * permitted while <code>null</code> elements are not permitted. Being
 * <code>Deque</code> a sub-interface of
 * <code>Queue<code>, this class can be used also as an implementaion of a <code>Queue</code>
 * and of a <code>Stack</code>.
 * 
 * The following operations are not supported:
 * <ul>
 * <li><code>public <T> T[] toArray(T[] a)</code></li>
 * <li><code>public boolean removeAll(Collection<?> c)</code></li>
 * <li><code>public boolean retainAll(Collection<?> c)</code></li>
 * <li><code>public boolean removeFirstOccurrence(Object o)</code></li>
 * <li><code>public boolean removeLastOccurrence(Object o)</code></li>
 * </ul>
 * 
 * @author Template: Luca Tesei, Implementation: Alessio Rubicini alessio.rubicini@studenti.unicam.it
 *
 */
public class ASDL2223Deque<E> implements Deque<E> {

    /*
     * Current number of elements in this deque
     */
    private int size;

    /*
     * Pointer to the first element of the double-linked list used to implement
     * this deque
     */
    private Node<E> first;

    /*
     * Pointer to the last element of the double-linked list used to implement
     * this deque
     */
    private Node<E> last;

    /*
     * Number of changes made
     * Useful for Iterator to check concurrent modifications
     */
    private int changesCounter;

    /**
     * Constructs an empty deque.
     */
    public ASDL2223Deque() {
        this.size = 0;
        this.first = null;
        this.last = null;
        this.changesCounter = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Object[] toArray() {
        // Create an array of the same size as the deque
        Object[] array = new Object[size];
        // Iterate the elements of the queue by inserting them into the array
        Iterator<E> thisIterator = this.iterator();
        for (int i = 0; i < size; i++) {
            array[i] = thisIterator.next();
        }
        // Returns the built array
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Iterate over the elements of the given collection
        Iterator<?> iter = c.iterator();
        while(iter.hasNext()) {
            E item = (E) iter.next();
            if(!this.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Check if the collection is null or contains null elements
        if(c == null || c.contains(null)) throw new NullPointerException("Collezione nulla");
        // The deque hasn't changed yet
        boolean changed = false;
        // Iterate over the elements of the given collection
        Iterator<?> iter = c.iterator();
        while(iter.hasNext()) {
            E item = (E)iter.next();
            changed = changed | this.add(item);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public void clear() {
        // Set the first and last items in the deque to null
        this.first = null;
        this.last = null;
        // Increase the number of changes
        this.changesCounter++;
        // Re-initialize the deque size
        this.size = 0;
    }

    @Override
    public void addFirst(E e) {
        // Check if the given element is null
        if(e == null) throw new NullPointerException("L'elemento è nullo");
        // If the deque is empty
        if(this.isEmpty()) {
            // Create a node with the given element and add it as first and last
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Create the new node with the given element
            Node<E> newNode = new Node<E>(null, e, first);
            // The old first node becomes the second
            newNode.next.prev = newNode;
            // The new node becomes the first one
            this.first = newNode;
        }
        // Increase size and number of changes
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public void addLast(E e) {
        // Check if the given element is null
        if(e == null) throw new NullPointerException();
        // If the deque is empty
        if(this.isEmpty()) {
            // Create node with the given element and add it as first and last
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Create new node with the given element and insert it after the last one
            Node<E> newNode = new Node<E>(last, e, null);
            this.last.next = newNode;
            this.last = newNode;
        }
        // Increase size and number of changes
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public boolean offerFirst(E e) {
        // The queue has no capacity restrictions
        // So the method is equivalent to addFirst
        if(e == null) throw new NullPointerException("Elemento nullo");
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            Node<E> newNode = new Node<E>(null, e, first);
            newNode.next.prev = newNode;
            this.first = newNode;
        }
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        // The queue has no capacity restrictions
        // So the method is equivalent to addLast
        if(e == null) throw new NullPointerException("Elemento nullo");
        // If the deque is empty, add the element as first and last
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Create new node with the given element and insert it after the last one
            Node<E> newNode = new Node<E>(last, e, null);
            this.last.next = newNode;
            this.last = newNode;
        }
        // Increase size and number of changes
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E removeFirst() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Stores the first item
        E retrievedElement = first.item;
        // If the deque has only one item, delete it directly
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            // Otherwise set the second element as the first
            this.first = first.next;
            first.prev = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        // Return the removed item
        return retrievedElement;
    }

    @Override
    public E removeLast() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Store the first item
        E retrievedElement = last.item;
        // If the deque has only one item, delete it directly
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            // Otherwise set the penultimate element as last
            this.last = this.last.prev;
            this.last.next = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        // Return the removed item
        return retrievedElement;
    }

    @Override
    public E pollFirst() {
        // The method is equivalent to removeFirst
        // The only difference is in the initial check which returns null in case of an empty deque
        if(isEmpty()) return null;
        E retrievedElement = first.item;
        // If the deque has only one item, delete it directly
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.first = first.next;
            first.prev = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        // Return the removed item
        return retrievedElement;
    }

    @Override
    public E pollLast() {
        // The method is equivalent to removeLast
        // The only difference is in the initial check which returns null in case of an empty queue
        if(isEmpty()) return null;
        E retrievedElement = last.item;
        // If the deque has only one item, delete it directly
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            // Decrease size and Increase number of changes
            this.last = this.last.prev;
            this.last.next = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        // Return the removed item
        return retrievedElement;
    }

    @Override
    public E getFirst() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Return the first element of the deque
        return this.first.item;
    }

    @Override
    public E getLast() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Return the last element of the deque
        return this.last.item;
    }

    @Override
    public E peekFirst() {
        // The method is equivalent to getFirst
        // The only difference is in the initial check which returns null in case of an empty queue
        if(isEmpty()) return null;
        return this.first.item;
    }

    @Override
    public E peekLast() {
        // The method is equivalent to getLast
        // The only difference is in the initial check which returns null in case of an empty queue
        if(isEmpty()) return null;
        return this.last.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean add(E e) {
        // Check if the given element is null
        if(e == null) throw new NullPointerException();
        // Create a new node with the given element
        Node<E> newNode = new Node<E>(this.last, e, null);
        // If the deque is empty, add node as first and last
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Otherwise insert as last
            this.last.next = newNode;
            this.last = newNode;
        }
        // Increase queue size and number of changes
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        // Check if the given element is null
        if(e == null) throw new NullPointerException();
        // Create a new node with the given element
        Node<E> newNode = new Node<E>(this.last, e, null);
        // If the queue is empty, add node as first and last
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Otherwise insert as last
            this.last.next = newNode;
            this.last = newNode;
        }
        // Increase size and number of changes
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E remove() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Save the first item
        E retrievedElement = first.item;
        // If first and last are the same, delete them thus emptying the deque
        if(first == last) {
            this.first = null;
            this.last = null;
        } else {
            // Otherwise remove the first element
            this.first = first.next;
            first.prev = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E poll() {
        // Check if the list is empty
        if(isEmpty()) return null;
        // Save the first item
        E retrievedElement = first.item;
        // If first and last are the same, delete them thus emptying the queue
        if(first == last) {
            this.first = null;
            this.last = null;
        } else {
            // Otherwise remove the first element
            this.first = first.next;
            first.prev = null;
        }
        // Decrease size and Increase number of changes
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E element() {
        // Check if the deque is empty
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        // Return the item of the first node
        return this.first.item;
    }

    @Override
    public E peek() {
        // Check if the deque is empty
        if(isEmpty()) return null;
        // Return the item of the first node
        return this.first.item;
    }

    @Override
    public void push(E e) {
        // The method is equivalent to addFirst
        if(e == null) throw new NullPointerException();
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            Node<E> newNode = new Node<E>(null, e, first);
            newNode.next.prev = newNode;
            this.first = newNode;
        }
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public E pop() {
        // The method is equivalent to removeFirst
        if(isEmpty()) throw new NoSuchElementException("Empty deque");
        E retrievedElement = first.item;
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.first = first.next;
            first.prev = null;
        }
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public boolean remove(Object o) {
        // Check if the given element is null
        if(o == null) throw new NullPointerException("Elemento nullo");
        // Check if the deque is empty and if it contains the given element
        if(isEmpty() || !this.contains(o)) return false;
        // If the first element is the same as the last
        if(first == last) {
            // The only element present is the one we are looking for, so it clears the deque
            this.clear();
            return true;
        }
        // Check if the element to delete is the first
        if(first.item.equals(o)) return this.removeFirst().equals(o);
        // General case
        // Iterate over the queue nodes starting from the first one
        Node<E> node = this.first;
        while (node != null) {
            // If it finds an element equal to the one given, it removes it and returns
            if (o.equals(node.item)) {
                // Check if the item to delete is the last one
                if(node.equals(this.last)) {
                    // Remove the last one
                    return this.removeLast().equals(o);
                } else {
                    // Change pointers to delete the node
                    node.next.prev = node.prev;
                    node.prev.next = node.next;
                    // Decrease size and Increase number of changes
                    this.size--;
                    this.changesCounter++;
                    return true;
                }
            }
            // Move to the next node along the iteration
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // Check if the given object is null
        if(o == null) throw new NullPointerException();
        // Iterate on the deque
        Iterator<E> thisIterator = this.iterator();
        while (thisIterator.hasNext()) {
            // If the given element is equal to the next element, the deque contains it
            if (o.equals(thisIterator.next())) return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    /*
     * Class for representing the nodes of the double-linked list used to
     * implement this deque. The class and its members/methods are protected
     * instead of private only for JUnit testing purposes.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        protected Node<E> prev;

        protected Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /*
     * Class for implementing an iterator for this deque. The iterator is
     * fail-fast: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class Itr implements Iterator<E> {
        private Node<E> lastReturned;           // Last node returned
        private int expectedChangesNumber;      // Expected number of changes to the deque

        Itr() {
            this.lastReturned = null;
            this.expectedChangesNumber = ASDL2223Deque.this.changesCounter;
        }

        public boolean hasNext() {
            // If the last element returned is null
            if(this.lastReturned == null) {
                // We're at the top of the list, so check if the first node exists
                return ASDL2223Deque.this.first != null;
            } else {
                // Otherwise it has advanced at least one node, so check the next one
                return lastReturned.next != null;
            }
        }

        public E next() {
            // Check for concurrent modifications
            if(this.expectedChangesNumber != ASDL2223Deque.this.changesCounter) {
                throw new ConcurrentModificationException("Deque modificata durante l'iterazione");
            }
            // Check if there is a next node
            if(!hasNext()) {
                throw new NoSuchElementException("Non c'è un elemento successivo");
            }
            // If the last element returned is null
            if(this.lastReturned == null) {
                // Return the first element
                this.lastReturned = ASDL2223Deque.this.first;
                return ASDL2223Deque.this.first.item;
            } else {
                // Otherwise, return the next of the last returned
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescItr();
    }

    /*
     * Class for implementing a descendign iterator for this deque. The iterator
     * is fail-fast: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class DescItr implements Iterator<E> {
        private Node<E> lastReturned;           // Last node returned
        private int expectedChangesNumber;      // Expected number of changes to the deque

        DescItr() {
            this.lastReturned = null;
            this.expectedChangesNumber = ASDL2223Deque.this.changesCounter;
        }

        public boolean hasNext() {
            // If the last element returned is null
            if(this.lastReturned == null) {
                // We're at the end of the list, so check if the last node exists
                return ASDL2223Deque.this.last != null;
            } else {
                // Otherwise it has advanced by at least one node, so check the previous one
                return lastReturned.prev != null;
            }
        }

        public E next() {
            // Check for concurrent modifications
            if(this.expectedChangesNumber != ASDL2223Deque.this.changesCounter) {
                throw new ConcurrentModificationException("Deque modificata durante l'iterazione");
            }
            // Check if there is a previous element
            if(!hasNext()) {
                throw new NoSuchElementException("Non c'è un elemento precedente");
            }
            // If the last element returned is null
            if(this.lastReturned == null) {
                // Return the last element
                this.lastReturned = ASDL2223Deque.this.last;
                return ASDL2223Deque.this.last.item;
            } else {
                // Otherwise it returns the previous of the last returned
                lastReturned = lastReturned.prev;
                return lastReturned.item;
            }
        }

    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getFirstNode() {
        return this.first;
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getLastNode() {
        return this.last;
    }

}
