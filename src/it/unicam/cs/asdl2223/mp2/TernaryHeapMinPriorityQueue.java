package it.unicam.cs.asdl2223.mp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Class that provides an implementation of a "dynamic" min-priority queue based
 * on a ternary heap. "Dynamic" means that the priority of an element already
 * present in the queue may be decreased, so possibly this element may become
 * the new minumum element. The elements that can be inserted may be of any
 * class implementing the interface <code>PriorityQueueElement</code>. This
 * min-priority queue does not have capacity restrictions, i.e., it is always
 * possible to insert new elements and the number of elements is unbound.
 * Duplicated elements are permitted while <code>null</code> elements are not
 * permitted.
 * 
 * @author Template: Luca Tesei, Implementation: Alessio Rubicini alessio.rubicini@studenti.unicam.it
 *
 */
public class TernaryHeapMinPriorityQueue {

    /*
     * ArrayList for representing the ternary heap. Use all positions, including
     * position 0 (the JUnit tests will assume so). You have to adapt
     * child/parent indexing formulas consequently.
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Create an empty queue.
     */
    public TernaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Return the current size of this queue.
     * 
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the ternary heap. The handle of the element will also be set
     * accordingly.
     * 
     * @param element
     *                    the new element to add
     * @throws NullPointerException
     *                                  if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        if(element == null) throw new NullPointerException("Elemento nullo");
        // Aggiunge l'elemento come foglia
        this.heap.add(element);
        // Ottiene l'indice dell'ultimo elemento
        int index = this.heap.size()-1;
        // Imposta l'handle corretto all'elemento appena aggiunto
        this.heap.get(index).setHandle(index);
        // Sposta l'elemento in alto finché la sua priorità è minore del genitore
        while (index > 0 && this.heap.get(index).getPriority() < heap.get(parentIndex(index)).getPriority()) {
            // Scambia gli elementi
            Collections.swap(this.heap, index, parentIndex(index));
            // Aggiorna gli handle
            this.heap.get(index).setHandle(index);
            this.heap.get(parentIndex(index)).setHandle(parentIndex(index));
            // Passa al nodo superiore
            index = parentIndex(index);
        }
    }

    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the ternary heap.
     * 
     * @return the current minimum element of this min-priority queue
     * 
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        if(this.size() == 0) {
            throw new NoSuchElementException();
        } else {
            return this.heap.get(0);
        }
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     * 
     * @return the current minimum element
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        if(this.size() == 0) throw new NoSuchElementException();
        // Ottiene il primo elemento (il minimo)
        PriorityQueueElement minimum = this.minimum();
        // Sostituisce il primo elemento con l'ultimo e aggiorna l'handle
        heap.set(0, heap.get(heap.size()-1));
        heap.get(0).setHandle(0);
        // Rimuove l'ultimo elemento
        heap.remove(heap.size()-1);
        // Se l'heap è vuoto, vuol dire che c'era solo un elemento
        if(heap.size() == 0) return minimum;
        // Esegue heapify a partire dal primo livello dell'heap
        this.heapify(0);
        return minimum;
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the ternary heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     * 
     * @param element
     *                        the element whose priority will be decreased, it
     *                        must currently be inside this min-priority queue
     * @param newPriority
     *                        the new priority to assign to the element
     * 
     * @throws NoSuchElementException
     *                                      if the element is not currently
     *                                      present in this min-priority queue
     * @throws IllegalArgumentException
     *                                      if the specified newPriority is not
     *                                      strictly less than the current
     *                                      priority of the element
     */
    public void decreasePriority(PriorityQueueElement element, double newPriority) {
        // Controlla se l'elemento è contenuto nell'heap
        if(!this.heap.contains(element)) {
            throw new NoSuchElementException("L'elemento non è contenuto nell'heap");
        }
        // Cerca l'elemento nell'heap
        for(PriorityQueueElement elem: this.heap) {
            if(elem == element) {
                // Controlla se la nuova priorità è valida
                if (newPriority >= elem.getPriority()) {
                    throw new IllegalArgumentException("Priorità non valida");
                } else {
                    // Cambia la priorità
                    elem.setPriority(newPriority);
                    // Esegue heapify sul sotto-albero dell'elemento
                    this.heapify(elem.getHandle());
                }
            }
        }
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    private int parentIndex(int i) { return (i-1)/3; }

    private int leftIndex(int i) {
        return (3*i)+1;
    }

    private int centerIndex(int i) {
        return (3*i)+2;
    }

    private int rightIndex(int i) {
        return (3*i)+3;
    }

    private void heapify(int i) {
        // Ottiene indici dei nodi figli
        int left = this.leftIndex(i);
        int center = this.centerIndex(i);
        int right = this.rightIndex(i);
        // Inizializza indice nodo minimo
        int minimum = i;
        // Determina l'indice del nodo minimo tra il nodo attuale e i suoi figli
        // Confronta nodo attuale con figlio sinistro
        if(left < heap.size() && heap.get(left).getPriority() < heap.get(i).getPriority()) {
            minimum = left;
        } else {
            minimum = i;
        }
        // Confronta minimo trovato con figlio centrale
        if(center < heap.size() && heap.get(center).getPriority() < heap.get(minimum).getPriority()) {
            minimum = center;
        }
        // Confronta minimo trovato con figlio destro
        if(right < heap.size() && heap.get(right).getPriority() < heap.get(minimum).getPriority()) {
            minimum = right;
        }
        // Se il minimo è diverso dal nodo attuale
        if(minimum != i) {
            // Scambia i nodi
            Collections.swap(heap, i, minimum);
            // Aggiorna gli handle
            this.heap.get(i).setHandle(i);
            this.heap.get(minimum).setHandle(minimum);
            // Richiama ricorsivamente heapify sul nodo appena scambiato
            this.heapify(minimum);
        }
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }

}
