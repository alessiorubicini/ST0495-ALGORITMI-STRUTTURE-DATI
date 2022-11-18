/**
 * 
 */
package it.unicam.cs.asdl2223.es9;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        return null;
    }

    private void merge(List<E> list, int leftIndex, int midIndex, int rightIndex) {
        int n1 = midIndex - leftIndex + 1;
        int n2 = rightIndex - midIndex;
        for (int i = 0; i < n1; i++) {
        }
        for (int i = 0; i < n2; i++) {
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
