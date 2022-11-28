/**
 * 
 */
package it.unicam.cs.asdl2223.es9;

import java.util.Collections;
import java.util.List;

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    int countCompare = 0;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        quickSort(l, 0, l.size()-1);
        return new SortingAlgorithmResult<E>(l, this.countCompare);
    }

    /**
     * Implementazione principale del Quick Sort
     * @param l             la sequenza da ordinare
     * @param startIndex    l'indice di inizio
     * @param lastIndex     l'indice di fine
     */
    private void quickSort(List<E> l, int startIndex, int lastIndex) {
        if(startIndex < lastIndex) {
            // Effettua prima partizione dell'array
            int pivot = this.partition(l, startIndex, lastIndex);
            // Ripartizione ricorsiva sulla parte sinistra
            quickSort(l, startIndex, pivot-1);
            // Ripartizione ricorsiva sulla parte destra
            quickSort(l, pivot+1, lastIndex);
        }
    }

    /**
     * Ritorna la posizione del pivot dopo aver riordinato l'array
     * con gli elementi minori del pivot a sinistra
     * e quelli maggiori del pivot a destra
     *
     * @param l             la sequenza da ordinare
     * @param startIndex    l'indice di inizio
     * @param lastIndex     l'indice di fine
     * @return              posizione del pivot
     */
    private int partition(List<E> l, int startIndex, int lastIndex) {
        E pivot = l.get(lastIndex);         // Sceglie ultimo elemento come pivot
        int i = (startIndex-1);             // Indice dell'elemento più grande (parte dal precedente al primo)
        // Itera dall'inizio alla fine
        for(int j = startIndex; j < lastIndex; j++) {
            // Se l'elemento corrente è minore del pivot
            if(l.get(j).compareTo(pivot) < 0) {
                i++;                            // Incrementa indice dell'elemento più grande
                Collections.swap(l, i, j);      // Scambia elementi
                this.countCompare++;            // Aumenta numero di confronti
            }
        }
        Collections.swap(l, i+1, lastIndex);    // Scambia elemento più grande con pivot
        return (i+1);                             // Ritorna posizione del pivot
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

}
