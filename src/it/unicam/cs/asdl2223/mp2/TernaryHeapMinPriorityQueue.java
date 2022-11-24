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
        // Get the index of the new element
        int index = this.heap.size() - 1;
        // Set the correct handle to the new element
        this.heap.get(index).setHandle(index);
        // Move the element up to the correct position
        this.moveElementUp(element);
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
        // Heapify at the top level of the heap
        this.heapify(0);
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
        // Check if the new priority is valid
        if(newPriority >= element.getPriority()) {
            throw new IllegalArgumentException("Priorità non valida");
        }
        // Get the element from the heap
        PriorityQueueElement elem =  heap.get(element.getHandle());
        // Change its priority
        elem.setPriority(newPriority);
        // Move the element up to the correct position
        this.moveElementUp(elem);
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    /*
     * Convenience function for calculating the index of the parent of the node in position i.
     */
    private int parentIndex(int i) { return (i-1)/3; }

    /*
     * Convenience function to calculate the index of the left child of the node in position i.
     */
    private int leftIndex(int i) {
        return (3*i)+1;
    }

    /*
     * Convenience function to calculate the index of the mid child of the node at position i.
     */
    private int centerIndex(int i) {
        return (3*i)+2;
    }

    /*
     * Convenience function to calculate the index of the right child of the node in position i.
     */
    private int rightIndex(int i) {
        return (3*i)+3;
    }

    /**
     * Rebuild a heap starting from node in position i
     * assuming that its subtrees are heaps.
     * @param i             the node to rebuild from
     */
    private void heapify(int i) {
        // Get indexes of child nodes
        int left = this.leftIndex(i);
        int center = this.centerIndex(i);
        int right = this.rightIndex(i);
        // Initialize minimal node index
        int minimum = i;
        // Determine the index of the minimum node between the current node and its children
        // Compare current node with left child
        if(left < heap.size() && heap.get(left).getPriority() < heap.get(i).getPriority()) {
            minimum = left;
        }
        // Compare minimum found with middle child
        if(center < heap.size() && heap.get(center).getPriority() < heap.get(minimum).getPriority()) {
            minimum = center;
        }
        // Compare minimum found with right child
        if(right < heap.size() && heap.get(right).getPriority() < heap.get(minimum).getPriority()) {
            minimum = right;
        }
        // If the minimum is different from the current node
        if(minimum != i) {
            // Swap the nodes
            Collections.swap(heap, i, minimum);
            // Update the handles
            this.heap.get(i).setHandle(i);
            this.heap.get(minimum).setHandle(minimum);
            // Recursive heapify on the minimum
            this.heapify(minimum);
        }
    }

    /**
     * Moves an element up in the heap to its correct position
     * It's a simplified and reversed version of heapify
     * @param element           the element to be moved
     */
    private void moveElementUp(PriorityQueueElement element) {
        // Get the element's handle
        int handle = element.getHandle();
        // While the parent node has a higher priority than the current node
        while(handle > 0 && heap.get(parentIndex(handle)).getPriority() > heap.get(handle).getPriority()) {
            // Swap the element with its parent node
            Collections.swap(heap, parentIndex(handle), handle);
            // Set the correct handles
            heap.get(parentIndex(handle)).setHandle(parentIndex(handle));
            heap.get(handle).setHandle(handle);
            // Go to the next level
            handle = parentIndex(handle);
        }
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }

}
