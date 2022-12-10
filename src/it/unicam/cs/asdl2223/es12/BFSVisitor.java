package it.unicam.cs.asdl2223.es12;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Classe singoletto che fornisce lo schema generico di visita Breadth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class BFSVisitor<L> {

    /**
     * Esegue la visita in ampiezza di un certo grafo a partire da un nodo
     * sorgente. Setta i valori seguenti valori associati ai nodi: distanza
     * intera, predecessore. La distanza indica il numero minimo di archi che si
     * devono percorrere dal nodo sorgente per raggiungere il nodo e il
     * predecessore rappresenta il padre del nodo in un albero di copertura del
     * grafo. Ogni volta che un nodo viene visitato viene eseguito il metodo
     * visitNode sul nodo. In questa classe il metodo non fa niente, basta
     * creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *                   il grafo da visitare.
     * @param source
     *                   il nodo sorgente.
     * @throws NullPointerException
     *                                      se almeno un valore passato è null
     * @throws IllegalArgumentException
     *                                      se il nodo sorgente non appartiene
     *                                      al grafo dato
     */
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        // Controlla se il grafo e il nodo sorgente sono nulli
        if (g == null || source == null) {
            throw new NullPointerException("Grafo o nodo sorgente nulli");
        }
        // Controlla se il grafo contiene il nodo sorgente dato
        if (!g.getNodes().contains(source)) {
            throw new IllegalArgumentException("Nodo sorgente non appartenente al grafo dato");
        }

        // Crea un insieme di nodi non ancora visitati
        Set<GraphNode<L>> nodesToVisit = new HashSet<>(g.getNodes());
        // Crea una lista di nodi visitati i cui vicini non sono stati visitati
        Queue<GraphNode<L>> nodesWithUnvisitedNeighbors = new LinkedList<>();


        // Imposta i nodi del grafo ai valori di default che rappresentano un nodo non visitato
        // Così l'algoritmo BFS può calcolare correttamente questi valori per ciascun nodo
        for (GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            node.setIntegerDistance(-1);
            node.setPrevious(null);
        }

        // Imposta il nodo sorgente come visitato
        source.setColor(GraphNode.COLOR_GREY);
        source.setIntegerDistance(0);
        nodesWithUnvisitedNeighbors.add(source);

        // Finché non c'è più nessun nodo i cui vicini non sono stati visitati
        while (!nodesWithUnvisitedNeighbors.isEmpty()) {
            // Estrae il nodo i cui vicini sono da visitare
            GraphNode<L> current = nodesWithUnvisitedNeighbors.poll();
            // Rimuove il nodo dai nodi ancora da visitare
            nodesToVisit.remove(current);

            // Itera sugli archi di questo nodo
            for (GraphEdge<L> e : g.getEdgesOf(current)) {
                // Per ogni arco ottiene il nodo destinazione
                GraphNode<L> neighbor = e.getNode2();
                // Se il colore del nodo destinazione è bianco, vuol dire che è ancora da visitare
                if (neighbor.getColor() == GraphNode.COLOR_WHITE) {
                    // Imposta i suoi valori correttamente
                    neighbor.setColor(GraphNode.COLOR_GREY);
                    neighbor.setIntegerDistance(current.getIntegerDistance() + 1);
                    neighbor.setPrevious(current);
                    // Aggiunge il nodo destinazione alla lista dei nodi i cui vicini sono da visitare
                    nodesWithUnvisitedNeighbors.add(neighbor);
                }
            }

            // Il nodo viene visitato e il suo colore impostato a nero
            current.setColor(GraphNode.COLOR_BLACK);
            visitNode(current);
        }
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la BFS quando i nodi passano da grigio a nero.
     * Ridefinire il metodo in una sottoclasse per effettuare azioni specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
