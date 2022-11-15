/**
 * 
 */
package it.unicam.cs.asdl2223.es8;

import java.util.Collections;
import java.util.List;

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        int countCompare = 0; // numero di operazioni di confronto effettuate
        // Costruisce l'heap a partire dalla lista
        for (int i = (l.size() / 2) - 1; i >= 0; i--) {
            heapify(l, l.size(), i);
        }
        // Itera dalla fine dell'heap alla radice
        for (int i = l.size()-1 ; i > 0; i--) {
            // Scambia la radice con l'ultimo elemento dell'heap
            Collections.swap(l, 0, i);
            // Incrementa numero di operazioni di confronto effettuate
            countCompare++;
            // Richiama heapify sull'heap ridotto
            heapify(l, i, 0);
        }
        return new SortingAlgorithmResult<E>(l, countCompare);
    }

    @Override
    public String getName() {
        return "HeapSort";
    }

    // Heapify del sottoalbero radicato con il nodo i che è
    // un indice in arr []. n è la dimensione dell'heap
    private void heapify(List<E> list, int heapSize, int index) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        // Se il figlio di sinistra è più grande della radice, lo imposta come più grande
        if (left < heapSize && list.get(left).compareTo(list.get(largest)) > 0) {
            largest = left;
        }
        // Se il figlio di destra è più grande dell'attuale più grande, lo imposta come più grande
        if (right < heapSize && list.get(right).compareTo(list.get(largest)) > 0) {
            largest = right;
        }
        // Se il più grande non corrisponde alla radice, effettua lo scambio
        if (largest != index) {
            Collections.swap(list, index, largest);
            // Chiama ricorsivamente heapify sul sottoalbero
            heapify(list, heapSize, largest);
        }
    }

}
