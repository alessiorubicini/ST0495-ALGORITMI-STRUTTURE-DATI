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
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    int countCompare = 0;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null) throw new NullPointerException("Lista nulla");
        if (l.size() <= 1) {
            return new SortingAlgorithmResult<E>(l, 0);
        }
        // Inizializzo il contatore dei confronti
        this.countCompare = 0;
        // Richiama mergeSort sull'intero array
        mergeSort(l, 0, l.size() - 1);
        // Ritorna il risultato dell'ordinamento
        return new SortingAlgorithmResult<E>(l, this.countCompare);
    }

    private void mergeSort(List<E> l, int start, int stop) {
        if (start == stop) return;
        int nElements = stop - start + 1;
        int middle = start + (nElements / 2) - 1;
        // Chiama ricorsivamente merge sort sulla parte di sinistra
        this.mergeSort(l, start, middle);
        // Chiama ricorsivamente merge sort sulla parte di destra
        this.mergeSort(l, middle + 1, stop);
        // Merge delle due parti ordinate
        this.merge(l, start, middle, stop);
    }

    private void merge(List<E> l, int start, int middle, int stop) {
        // Divide la lista in due sotto-liste
        List<E> left = new ArrayList<E>();
        for (int i = start; i <= middle; i++) {
            left.add(l.get(i));
        }
        List<E> right = new ArrayList<E>();
        for (int i = middle + 1; i <= stop; i++) {
            right.add(l.get(i));
        }
        // i scorre su l da start a stop
        int i = start;
        // j scorre su left da 0
        int j = 0;
        // k scorre su right da 0
        int k = 0;
        // Scorro le due liste
        while (j < left.size() && k < right.size()) {
            // Se l'elemento di sinistra è minore del corrispettivo di destra
            if (left.get(j).compareTo(right.get(k)) < 0) {
                // Metto in posizione i l'elemento di sinistra
                l.set(i, left.get(j));
                i++;
                j++;
            } else {
                // Altrimenti, metto in posizione i l'elemento di destra
                l.set(i, right.get(k));
                i++;
                k++;
            }
            // Aumento numero di confronti
            this.countCompare++;
        }
        // Controllo se una delle due sottoliste è stata riempita
        if (j >= left.size()) {
            // Metto nella lista finale tutti gli elementi rimasti in quella di destra
            for (; k < right.size(); k++) {
                l.set(i, right.get(k));
                i++;
            }
        } else {
            // Metto nella lista finale tutti gli elementi rimasti in quella di sinistra
            for (; j < left.size(); j++) {
                l.set(i, left.get(j));
                i++;
            }
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
