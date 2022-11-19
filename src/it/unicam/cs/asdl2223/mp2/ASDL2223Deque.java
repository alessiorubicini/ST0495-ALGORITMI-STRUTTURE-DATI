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
        Object[] array = new Object[size];
        Iterator<E> thisIterator = this.iterator();
        for (int i = 0; i < size; i++) {
            array[i] = thisIterator.next();
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> thisIterator = c.iterator();
        for (int i = 0; i < c.size(); i++) {
            if(!this.contains(thisIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int oldSize = this.size;
        Iterator<?> thisIterator = c.iterator();
        for (int i = 0; i < c.size(); i++) {
            this.add(c.iterator().next());
        }
        if(oldSize != this.size) {
            return true;
        }
        return false;
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
        this.first = null;
        this.last = null;
        this.changesCounter++;
        this.size = 0;
    }

    @Override
    public void addFirst(E e) {
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
    public void addLast(E e) {
        if(e == null) throw new NullPointerException();
        // Se la coda è vuota, aggiunge l'elemento come primo e ultimo
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Se il primo elemento coincide con l'ultimo (cioè la coda ha dimensione 1)
            if(first == last) {
                // Crea nuovo nodo e lo inserisce dopo il primo
                Node<E> newNode = new Node<E>(first, e, null);
                this.first.next = newNode;
                this.last = newNode;
            } else {
                // Altrimenti, crea nuovo nodo e lo inserisce come ultimo
                Node<E> newNode = new Node<E>(last, e, null);
                this.last.next = newNode;
                this.last = newNode;
            }
        }
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public boolean offerFirst(E e) {
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
        if(e == null) throw new NullPointerException("Elemento nullo");
        // Se la coda è vuota, aggiunge l'elemento come primo e ultimo
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Se il primo elemento coincide con l'ultimo (cioè la coda ha dimensione 1)
            if(first == last) {
                // Crea nuovo nodo e lo inserisce dopo il primo
                Node<E> newNode = new Node<E>(first, e, null);
                this.first.next = newNode;
                this.last = newNode;
            } else {
                // Altrimenti, crea nuovo nodo e lo inserisce come ultimo
                Node<E> newNode = new Node<E>(last, e, null);
                this.last.next = newNode;
                this.last = newNode;
            }
        }
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E removeFirst() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
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
    public E removeLast() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        E retrievedElement = last.item;
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E pollFirst() {
        if(isEmpty()) return null;
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
    public E pollLast() {
        if(isEmpty()) return null;
        E retrievedElement = last.item;
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E getFirst() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        return this.first.item;
    }

    @Override
    public E getLast() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        return this.last.item;
    }

    @Override
    public E peekFirst() {
        if(isEmpty()) return null;
        return this.first.item;
    }

    @Override
    public E peekLast() {
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
        // Controlla che l'elemento non sia nullo
        if(e == null) throw new NullPointerException();
        // Crea un nuovo nodo con l'elemento dato
        Node<E> newNode = new Node<E>(null, e, null);
        // Se la coda è vuota, aggiunge elemento come primo e ultimo
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Altrimenti inserisce come ultimo
            newNode.prev = this.last;
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione della coda e numero di modifiche effettuato
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        // Controlla che l'elemento non sia nullo
        if(e == null) throw new NullPointerException();
        // Crea un nuovo nodo con l'elemento dato
        Node<E> newNode = new Node<E>(null, e, null);
        // Se la coda è vuota, aggiunge elemento come primo e ultimo
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Altrimenti inserisce come ultimo
            newNode.prev = this.last;
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione della coda e numero di modifiche effettuato
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E remove() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        E retrievedElement = first.item;
        // Se primo e ultimo coincidono, li cancella svuotando così la coda
        if(first == last) {
            this.first = null;
            this.last = null;
        } else {
            // Altrimenti rimuove il primo elemento
            this.first = first.next;
            first.prev = null;
        }
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E poll() {
        if(isEmpty()) return null;
        E retrievedElement = first.item;
        // Se primo e ultimo coincidono, li cancella svuotando così la coda
        if(first == last) {
            this.first = null;
            this.last = null;
        } else {
            // Altrimenti rimuove il primo elemento
            this.first = first.next;
            first.prev = null;
        }
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E element() {
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        return this.first.item;
    }

    @Override
    public E peek() {
        if(isEmpty()) return null;
        return this.first.item;
    }

    @Override
    public void push(E e) {
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
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
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
        // TODO implement
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // Controlla che l'oggetto non sia nullo
        if(o == null) throw new NullPointerException();
        // Itera sulla coda utilizzando l'iterator
        Iterator<E> thisIterator = this.iterator();
        while (thisIterator.hasNext()) {
            // Se l'elemento corrente è uguale all'elemento dato, la coda lo contiene
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
        private Node<E> lastReturned;           // Ultimo elemento restituito
        private int expectedChangesNumber;      // Numero di modifiche atteso

        Itr() {
            this.lastReturned = null;
            this.expectedChangesNumber = ASDL2223Deque.this.changesCounter;
        }

        public boolean hasNext() {
            if(this.lastReturned == null) {
                // È all'inizio della lista, controlla se il primo nodo esiste
                return ASDL2223Deque.this.first != null;
            } else {
                // È avanzato di almeno un nodo, controlla il successivo
                return lastReturned.next != null;
            }
        }

        public E next() {
            // Controlla modifiche concorrenti
            if(this.expectedChangesNumber != ASDL2223Deque.this.changesCounter) {
                throw new ConcurrentModificationException("Deque modificata durante l'iterazione");
            }
            // Controlla se c'è un elemento successivo
            if(!hasNext()) {
                throw new NoSuchElementException("Non c'è un elemento successivo");
            }
            // Restituisce l'elemento successivo
            // Il primo nel caso in cui l'ultimo restituito sia null
            if(this.lastReturned == null) {
                this.lastReturned = ASDL2223Deque.this.first;
                return ASDL2223Deque.this.first.item;
            } else {
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
        private Node<E> lastReturned;           // Ultimo elemento ritornato
        private int expectedChangesNumber;      // Numero di modifiche atteso

        DescItr() {
            this.lastReturned = null;
            this.expectedChangesNumber = ASDL2223Deque.this.changesCounter;
        }

        public boolean hasNext() {
            if(this.lastReturned == null) {
                return ASDL2223Deque.this.last != null;
            } else {
                return lastReturned.prev != null;
            }
        }

        public E next() {
            // Controlla modifiche concorrenti
            if(this.expectedChangesNumber != ASDL2223Deque.this.changesCounter) {
                throw new ConcurrentModificationException("Deque modificata durante l'iterazione");
            }
            // Controlla se c'è un elemento precedente
            if(!hasNext()) {
                throw new NoSuchElementException("Non c'è un elemento precedente");
            }
            // Restituisce l'elemento precedente
            // Il primo nel caso in cui l'ultimo restituito sia null
            if(this.lastReturned == null) {
                this.lastReturned = ASDL2223Deque.this.last;
                return ASDL2223Deque.this.last.item;
            } else {
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

    private void debugQueue() {
        Iterator<E> thisIterator = this.iterator();
        for (int i = 0; i < size; i++) {
            E item = thisIterator.next();
            System.out.print(item + "->");
        }
        System.out.println("----");
    }

}
