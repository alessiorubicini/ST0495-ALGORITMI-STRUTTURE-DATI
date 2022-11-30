/**
 * 
 */
package it.unicam.cs.asdl2223.es9;

import java.util.Collections;
import java.util.List;
import java.util.Random;

//TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();

    private int countCompare = 0;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) throw new NullPointerException("Lista nulla");
        if(l.size() <= 1) {
            return new SortingAlgorithmResult<E>(l, 0);
        }
        quickSortRandom(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<E>(l, this.countCompare);
    }

    /**
     * Implementazione principale del Quick Sort
     * @param l             la sequenza da ordinare
     * @param startIndex    l'indice di inizio
     * @param lastIndex     l'indice di fine
     */
    private void quickSortRandom(List<E> l, int startIndex, int lastIndex) {
        if(startIndex < lastIndex) {
            // Effettua prima partizione dell'array
            int pivot = this.partitionRandom(l, startIndex, lastIndex);
            // Ripartizione ricorsiva sulla parte sinistra
            quickSortRandom(l, startIndex, pivot-1);
            // Ripartizione ricorsiva sulla parte destra
            quickSortRandom(l, pivot+1, lastIndex);
        }
    }

    private int partitionRandom(List<E> l, int startIndex, int lastIndex) {
        // Consideriamo come pivot uno elemento a caso tra l'inizio e la fine
        int random = randomGenerator.nextInt(lastIndex - startIndex + 1);
        // sposto l'elemento in posizione p + shift con l'elemento in posizione
        // r, in modo che diventi lui il pivot; a meno che p+shift non sia già r
        if (startIndex + random != lastIndex) {
            // Scambio
            Collections.swap(l, startIndex+random, lastIndex);
        }
        // Chiamo la partizione normale
        return partition(l, startIndex, lastIndex);
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
        return "QuickSortRandom";
    }

}
