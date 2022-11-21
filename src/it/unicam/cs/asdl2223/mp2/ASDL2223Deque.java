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
     * Numero di modifiche effettuato
     * Utile all'Iterator per verificare modifiche concorrenti
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
        // Crea un array della stessa dimensione della coda
        Object[] array = new Object[size];
        // Itera sugli elementi della coda inserendoli nell'array
        Iterator<E> thisIterator = this.iterator();
        for (int i = 0; i < size; i++) {
            array[i] = thisIterator.next();
        }
        // Ritorna l'array costruito
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Itera sugli elementi della collezione data
        Iterator<?> thisIterator = c.iterator();
        for (int i = 0; i < c.size(); i++) {
            // Controlla se l'elemento della collezione è contenuto nella coda
            if(!this.contains(thisIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Controlla se la collezione è nulla o contiene elementi nulli
        if(c == null || c.contains(null)) throw new NullPointerException("Collezione nulla");
        // Memorizza la dimensione attuale della coda per la successiva verifica
        int oldSize = this.size;
        // Itera sugli elementi della collezione data
        Iterator<?> thisIterator = c.iterator();
        for (int i = 0; i < c.size(); i++) {
            // Aggiunge ogni elemento in coda
            this.add((E) thisIterator.next());
        }
        if(oldSize != this.size) return true;
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
        // Imposta a null il primo e l'ultimo elemento della coda
        this.first = null;
        this.last = null;
        // Incrementa il numero di modifiche
        this.changesCounter++;
        // Re-inizializza la dimensione della coda
        this.size = 0;
    }

    @Override
    public void addFirst(E e) {
        // Controlla se l'elemento dato è nullo
        if(e == null) throw new NullPointerException("L'elemento è nullo");
        // Se la coda è vuota
        if(this.isEmpty()) {
            // Crea nodo con l'elemento dato e lo aggiunge come primo e ultimo
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Crea il nuovo nodo con l'elemento dato
            Node<E> newNode = new Node<E>(null, e, first);
            // Il vecchio primo nodo diventa il secondo
            newNode.next.prev = newNode;
            // Il nuovo nodo diventa il primo nodo
            this.first = newNode;
        }
        // Incrementa dimensione e numero di modifiche
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public void addLast(E e) {
        // Controlla se l'elemento dato è nullo
        if(e == null) throw new NullPointerException();
        // Se la coda è vuota
        if(this.isEmpty()) {
            // Crea nodo con l'elemento dato e lo aggiunge come primo e ultimo
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Crea nuovo nodo con l'elemento dato e lo inserisce dopo l'ultimo
            Node<E> newNode = new Node<E>(last, e, null);
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione e numero di modifiche
        this.size++;
        this.changesCounter++;
        return;
    }

    @Override
    public boolean offerFirst(E e) {
        // La coda non ha restrizioni di capacità
        // Quindi il metodo è equivalente ad addFirst
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
        // La coda non ha restrizioni di capacità
        // Quindi il metodo è equivalente ad addLast
        if(e == null) throw new NullPointerException("Elemento nullo");
        // Se la coda è vuota, aggiunge l'elemento come primo e ultimo
        if(this.isEmpty()) {
            this.first = new Node<E>(null, e, null);
            this.last = first;
        } else {
            // Crea nuovo nodo con l'elemento dato e lo inserisce dopo l'ultimo
            Node<E> newNode = new Node<E>(last, e, null);
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione e numero di modifiche
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E removeFirst() {
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Salva il primo elemento
        E retrievedElement = first.item;
        // Se la coda ha un solo elemento, lo elimina direttamente
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            // Altrimenti imposta il secondo elemento come primo
            this.first = first.next;
            first.prev = null;
        }
        // Decrementa dimensione e Incrementa numero di modifiche
        this.size--;
        this.changesCounter++;
        // Ritorna l'elemento rimosso
        return retrievedElement;
    }

    @Override
    public E removeLast() {
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Salva il primo elemento
        E retrievedElement = last.item;
        // Se la coda ha un solo elemento, lo elimina direttamente
        if(this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            // Altrimenti imposta il penultimo elemento come ultimo
            this.last = this.last.prev;
            this.last.next = null;
        }
        // Decrementa dimensione e Incrementa numero di modifiche
        this.size--;
        this.changesCounter++;
        // Ritorna l'elemento rimosso
        return retrievedElement;
    }

    @Override
    public E pollFirst() {
        // Il metodo è equivalente ad removeFirst
        // L'unica differenza risiede nel controllo iniziale che ritorna null in caso di coda vuota
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
        // Il metodo è equivalente ad removeLast
        // L'unica differenza risiede nel controllo iniziale che ritorna null in caso di coda vuota
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
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Ritorna il primo elemento della coda
        return this.first.item;
    }

    @Override
    public E getLast() {
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Ritorna l'ultimo elemento della coda
        return this.last.item;
    }

    @Override
    public E peekFirst() {
        // Il metodo è equivalente ad getFirst
        // L'unica differenza risiede nel controllo iniziale che ritorna null in caso di coda vuota
        if(isEmpty()) return null;
        return this.first.item;
    }

    @Override
    public E peekLast() {
        // Il metodo è equivalente ad getLast
        // L'unica differenza risiede nel controllo iniziale che ritorna null in caso di coda vuota
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
        // Controlla se l'elemento dato è nullo
        if(e == null) throw new NullPointerException();
        // Crea un nuovo nodo con l'elemento dato
        Node<E> newNode = new Node<E>(this.last, e, null);
        // Se la coda è vuota, aggiunge nodo come primo e ultimo
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Altrimenti inserisce come ultimo
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione della coda e numero di modifiche
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        // Controlla se l'elemento dato è nullo
        if(e == null) throw new NullPointerException();
        // Crea un nuovo nodo con l'elemento dato
        Node<E> newNode = new Node<E>(this.last, e, null);
        // Se la coda è vuota, aggiunge nodo come primo e ultimo
        if(this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        } else {
            // Altrimenti inserisce come ultimo
            this.last.next = newNode;
            this.last = newNode;
        }
        // Incrementa dimensione e numero di modifiche
        this.size++;
        this.changesCounter++;
        return true;
    }

    @Override
    public E remove() {
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Salva il primo elemento
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
        // Decrementa dimensione e Incrementa numero modifiche
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E poll() {
        // Controlla se la lista è vuota
        if(isEmpty()) return null;
        // Salva il primo elemento
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
        // Decrementa dimensione e Incrementa numero modifiche
        this.size--;
        this.changesCounter++;
        return retrievedElement;
    }

    @Override
    public E element() {
        // Controlla se la coda è vuota
        if(isEmpty()) throw new NoSuchElementException("Coda vuota");
        // Ritorna l'item del primo nodo
        return this.first.item;
    }

    @Override
    public E peek() {
        // Controlla se la coda è vuota
        if(isEmpty()) return null;
        // Ritorna l'item del primo nodo
        return this.first.item;
    }

    @Override
    public void push(E e) {
        // Il metodo è equivalente a addFirst
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
        // Il metodo è equivalente a removeFirst
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
        // Controlla se l'elemento dato è nullo
        if(o == null) throw new NullPointerException("Elemento nullo");
        // Controlla se la lista è vuota e se contiene l'elemento dato
        if(isEmpty() || !this.contains(o)) return false;
        // Se il primo elemento è uguale all'ultimo
        if(first == last) {
            // L'unico elemento presente è quello cercato, quindi lo rimuove
            this.clear();
            return true;
        }
        // Controlla se l'elemento da eliminare è il primo
        if(first.item.equals(o)) return this.removeFirst().equals(o);
        // Itera sui nodi della coda partendo dal primo
        Node<E> node = this.first;
        while (node != null) {
            // Se trova un elemento uguale a quello dato, lo rimuove e ritorna
            if (o.equals(node.item)) {
                // Controlla se l'elemento da eliminare è l'ultimo
                if(node.equals(this.last)) {
                    return this.removeLast().equals(o);
                } else {
                    // Cambia puntatori in modo da eliminare il nodo
                    node.next.prev = node.prev;
                    node.prev.next = node.next;
                    // Decrementa dimensione e Incrementa numero di modifiche
                    this.size--;
                    this.changesCounter++;
                    return true;
                }
            }
            // Passa al prossimo nodo lungo l'iterazione
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // Controlla se l'oggetto dato è nullo
        if(o == null) throw new NullPointerException();
        // Itera sulla coda
        Iterator<E> thisIterator = this.iterator();
        while (thisIterator.hasNext()) {
            // Se l'elemento dato è uguale al prossimo elemento, la coda lo contiene
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
            // Se l'ultimo elemento ritornato è nullo
            if(this.lastReturned == null) {
                // È all'inizio della lista, quindi controlla se il primo nodo esiste
                return ASDL2223Deque.this.first != null;
            } else {
                // Altrimenti è avanzato di almeno un nodo, quindi controlla il successivo
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
            // Se l'ultimo elemento ritornato è nullo
            if(this.lastReturned == null) {
                // Restituisce il primo elemento
                this.lastReturned = ASDL2223Deque.this.first;
                return ASDL2223Deque.this.first.item;
            } else {
                // Altrimenti restituisce il successivo dell'ultimo ritornato
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
            // Se l'ultimo elemento ritornato è nullo
            if(this.lastReturned == null) {
                // È alla fine della lista, quindi controlla se l'ultimo nodo esiste
                return ASDL2223Deque.this.last != null;
            } else {
                // Altrimenti è avanzato di almeno un nodo, quindi controlla il precedente
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
            // Se l'ultimo elemento ritornato è nullo
            if(this.lastReturned == null) {
                // Restituisce l'ultimo elemento
                this.lastReturned = ASDL2223Deque.this.last;
                return ASDL2223Deque.this.last.item;
            } else {
                // Altrimenti restituisce il precedente dell'ultimo ritornato
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
