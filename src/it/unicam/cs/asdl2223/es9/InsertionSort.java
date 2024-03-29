package it.unicam.cs.asdl2223.es9;

import java.util.Collections;
import java.util.List;

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // Controlla se la lista è valida
        if(l == null) throw new NullPointerException("Lista nulla");
        if (l.size() <=1) return new SortingAlgorithmResult<E>(l, 0);
        // Inizializza il numero di confronti
        int countCompare = 0;
        // Itera sulla lista partendo dal primo elemento
        for (int i = 1; i < l.size(); i++) {
            E value = l.get(i);     // Ottiene valore
            int j = i-1;            // Ottiene indice del valore precedente
            // Se il valore precedente è maggiore dell'attuale
            while(j >= 0 && l.get(j).compareTo(value) > 0) {
                l.set(j+1, l.get(j));       // Imposta valore attuale come precedente
                j--;                        // Decrementa contatore
                countCompare++;             // Aumenta numero di confronti
            }
            if (j != i - 1) {
                // Imposta come successivo il valore attuale
                l.set(j + 1, value);
            }
        }
        return new SortingAlgorithmResult<E>(l, countCompare);
    }

    public String getName() {
        return "InsertionSort";
    }
}
