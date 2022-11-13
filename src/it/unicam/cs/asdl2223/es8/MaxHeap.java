package it.unicam.cs.asdl2223.es8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        if(list == null) throw new NullPointerException("Lista nulla");
        this.heap = new ArrayList<E>();
        for (E element: list) {
            this.heap.add(element);
        }
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        if(el == null) throw new NullPointerException();
        // Aggiunge l'elemento nella prima posizione libera
        this.heap.add(el);
        // Controlla se il nuovo elemento è la radice
        if(this.size() == 1) return;
        // Cicla sull'heap dall'ultimo elemento alla radice
        E support;
        for (int i = this.size(); i > 1; i = parentIndex(i)) {
            // Controlla se il parent esiste
            if(parentIndex(i) > 0) {
                // Controlla se il nuovo elemento è maggiore del parent
                if(el.compareTo(heap.get(parentIndex(i)-1)) == 1) {
                    // Scambia parent e nuovo elemento
                    support = heap.get(parentIndex(i)-1);
                    heap.set(parentIndex(i)-1, el);
                    if(heap.get(leftIndex(parentIndex(i))-1).compareTo(el) == 0) {
                        heap.set(leftIndex(parentIndex(i))-1, support);
                    } else {
                        heap.set(rightIndex(parentIndex(i))-1, support);
                    }
                }
            }
        }
        return;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return (2 * i);
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return (2 * i + 1);
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return i/2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if(heap.size() == 0) {
            return null;
        } else {
            return heap.get(0);
        }
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        // TODO implementare
        this.heapify(0);
        return null;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        if(i >= heap.size()) throw new IllegalArgumentException();
        // Ottiene sottoalberi figli
        int leftNode = this.leftIndex(i);
        int rightNode = this.rightIndex(i);
        int maximum = 0;
        // Confronta nodo attuale con figlio sinistro
        if(leftNode < heap.size() && heap.get(leftNode).compareTo(heap.get(i)) > 0) {
            maximum = leftNode;
        } else {
            maximum = rightNode;
        }
        // Confronta massimo attuale con figlio destro
        if(rightNode < heap.size() && heap.get(rightNode).compareTo(heap.get(maximum)) > 0) {
            maximum = rightNode;
        }
        // Se il massimo è diverso dal nodo attuale, c'è da scambiare
        if(maximum != i) {
            Collections.swap(heap, i, maximum);
            // Richiama ricorsivamente heapify sul livello sottostante
            heapify(maximum);
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }

}
