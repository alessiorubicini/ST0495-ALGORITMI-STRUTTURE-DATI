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

    int countCompare = 0;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        List<E> sortedList = this.mergeSort(l);
        return new SortingAlgorithmResult<E>(sortedList, this.countCompare);
    }

    private List<E> mergeSort(List<E> l) {
        // Se la lista ha zero o uno elementi, ritorna la lista stessa
        if (l.size() <= 1) return l;
        // Divide la lista in due parti
        List<E> left = l.subList(0, l.size()/2);
        List<E> right = l.subList(l.size()/2, l.size());
        // Richiama ricorsivamente il merge sort sulle due parti
        left = mergeSort(left);
        right = mergeSort(right);
        // Riunisce le parti ordinate
        return merge(left, right);
    }

    private List<E> merge(List<E> left, List<E> right) {
        // Lista ordinata risultante dall'unione delle due liste
        ArrayList<E> sortedList = new ArrayList<E>();
        // Itera finché le due sotto-liste non sono vuote
        while (!left.isEmpty() && !right.isEmpty()) {
            // Se il primo elemento della lista di sinistra è minore del primo di quella di destra
            if (left.get(0).compareTo(right.get(0)) < 0) {
                // Aggiunge l'elemento e lo rimuove dall'array di sinistra
                sortedList.add(left.remove(0));
            } else {
                // Altrimenti, // Aggiunge l'elemento e lo rimuove dall'array di sinistra
                sortedList.add(right.remove(0));
            }
            // Incrementa numero di comparazioni
            this.countCompare++;
        }

        while (!left.isEmpty()) {
            sortedList.add(left.remove(0));
        }

        while (!right.isEmpty()) {
            sortedList.add(right.remove(0));
        }

        return sortedList;
    }

    public String getName() {
        return "MergeSort";
    }
}
