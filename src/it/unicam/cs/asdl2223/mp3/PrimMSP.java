package it.unicam.cs.asdl2223.mp3;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

import java.util.ArrayList;
import java.util.List;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author Luca Tesei (template) Alessio Rubicini alessio.rubicini@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMSP<L> {

    // Coda di priorità per l'algoritmo di Prim
    private List<GraphNode<L>> priorityQueue;

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        // Inizializza la coda di priorità come lista vuota
        this.priorityQueue = new ArrayList<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        // Controlla se il grafo e il nodo sorgente sono nulli
        if (g == null || s == null) {
            throw new NullPointerException("Grafo o nodo sorgente nulli");
        }
        // Controlla se il nodo sorgente esiste nel grafo
        if (g.getNode(s) == null) {
            throw new IllegalArgumentException("Nodo sorgente non esistente nel grafo");
        }
        // Controlla se il grafo è orientato, non pesato o con pesi negativi
        if(g.isDirected()) {
            throw new IllegalArgumentException("Grafo orienttato");
        }
        // Controlla se il grafo non è pesato o ha pesi negativi
        for(GraphEdge<L> edge: g.getEdges()) {
            if(Double.isNaN(edge.getWeight())) {
                throw new IllegalArgumentException("Grafo non pesato");
            } else if(edge.getWeight() < 0) {
                throw new IllegalArgumentException("Grafo pesato con pesi negativi");
            } else {
                continue;
            }
        }

        // Inizializza la coda di priorità
        priorityQueue.clear();

        // Inizializza i valori di tutti i nodi
        for(GraphNode<L> node: g.getNodes()) {
            node.setFloatingPointDistance(Double.MAX_VALUE);
            node.setPrevious(null);
            // Aggiunge il nodo alla coda di priorità
            priorityQueue.add(node);
        }

        // Imposta il valore chiave (distanza) del nodo sorgente a default
        s.setFloatingPointDistance(0);
        s.setPrevious(null);

        // Finché la coda di priorità non è vuota
        while (!priorityQueue.isEmpty()) {
            // Estra il primo nodo dalla coda di priorità
            GraphNode<L> extractedNode = priorityQueue.remove(0);
            System.out.println("Estratto " + extractedNode);
            // Itera sui nodi adiacenti al nodo estratto
            for(GraphNode<L> currentNode: g.getAdjacentNodesOf(extractedNode)) {
                System.out.println("-- Nodo: " + currentNode);
                // Ottiene l'arco che collega il nodo estratto al nodo corrente
                GraphEdge<L> edge = g.getEdge(extractedNode, currentNode);
                System.out.println("-- Arco: " + edge);
                // Se la coda di priorità contiene il nodo corrente
                // e il peso dell'arco è minore del valore chiave (distanza) del nodo corrente
                if(priorityQueue.contains(currentNode) && edge.getWeight() < currentNode.getFloatingPointDistance()) {
                    System.out.println("---- "+ currentNode + " è nella queue e " + edge.getWeight() + " < " + currentNode.getFloatingPointDistance());
                    // Imposta il parent del nodo corrente al nodo estratto
                    currentNode.setPrevious(extractedNode);
                    System.out.println("---- Imposto " + currentNode + ".previous a " + extractedNode);
                    // Imposta come valore chiave (distanza) il peso dell'arco
                    currentNode.setFloatingPointDistance(edge.getWeight());
                }
            }
        }

        // TODO implementare
    }

}
