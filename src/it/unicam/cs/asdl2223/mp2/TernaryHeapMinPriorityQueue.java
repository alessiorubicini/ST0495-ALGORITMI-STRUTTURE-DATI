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
        // Check if the given element is null
        if(element == null) throw new NullPointerException("Elemento nullo");
        // Add the element as last
        this.heap.add(element);
        // Get the index of the newly added item in the heap
        int index = this.heap.size() - 1;
        // Set the correct handle to the newly added element
        this.heap.get(index).setHandle(index);
        // Heapify on the parent node of the newly added node
        this.heapify(parentIndex(index), true);
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
        // Check if the heap is empty
        if(this.size() == 0) {
            throw new NoSuchElementException();
        } else {
            // Return the root (minimum) of the heap
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
        // Check if the heap is empty
        if(heap.isEmpty()) throw new NoSuchElementException();
        // Get the first element (the least)
        PriorityQueueElement minimum = this.minimum();
        // Replace the first element with the last and update the handle
        heap.set(0, heap.get(heap.size() - 1));
        heap.get(0).setHandle(0);
        // Remove the last element
        heap.remove(heap.size() - 1);
        // If the heap is empty, there was only one element
        if(heap.size() == 0) return minimum;
        // Heapify starting at the top level of the heap
        this.heapify(0, false);
        // Return the minimum
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
        // Check if the item is contained in the heap
        if(!this.heap.contains(element)) {
            throw new NoSuchElementException("L'elemento non è contenuto nell'heap");
        }
        // Check if the new priority is correct
        if(newPriority >= element.getPriority()) {
            throw new IllegalArgumentException("Priorità non valida");
        }
        // Get the element from the heap
        PriorityQueueElement elem =  heap.get(element.getHandle());
        // Change its priority
        elem.setPriority(newPriority);
        // Heapify on the parent node
        this.heapify(parentIndex(elem.getHandle()), true);
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in posizione i.
     */
    private int parentIndex(int i) { return (i-1)/3; }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in posizione i.
     */
    private int leftIndex(int i) {
        return (3*i)+1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio centrale del nodo in posizione i.
     */
    private int centerIndex(int i) {
        return (3*i)+2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro  del nodo in posizione i.
     */
    private int rightIndex(int i) {
        return (3*i)+3;
    }

    /**
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi siano heap.
     * @param i             la posizione da cui ricostruire
     * @param reversed      true se l'heapify deve essere svolto verso l'alto (decreasePriority),
     *                      false altrimenti
     */
    private void heapify(int i, boolean reversed) {
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
            // Richiama ricorsivamente heapify sul nodo minimo o sul nodo genitore a seconda della richiesta
            if(reversed) {
                this.heapify(parentIndex(i), true);
            } else {
                this.heapify(minimum, false);
            }
        }
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }

}
