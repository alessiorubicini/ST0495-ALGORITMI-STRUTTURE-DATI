package it.unicam.cs.asdl2223.es13;

import java.util.*;

/**
 * Classe che implementa l'algoritmo di Dijkstra per il calcolo dei cammini
 * minimi da una sorgente singola. L'algoritmo usa una coda con priorità
 * inefficiente (implementata con una List) che per estrarre il minimo impiega
 * O(n).
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                le etichette dei nodi del grafo
 */
public class DijkstraShortestPathComputer<L> implements SingleSourceShortestPathComputer<L> {
    // ultima sorgente su cui sono stati calcolati i cammini minimi
    private GraphNode<L> lastSource;

    // il grafo su cui opera questo oggetto
    private final Graph<L> grafo;

    // flag che indica se i cammini minimi sono stati calcolati almeno una volta
    private boolean isComputed = false;

    /*
     * Contiene i nodi ancora da analizzare, la coda con priorità viene gestita
     * tramite lista e l'elemento minimo viene cercato e rimosso con costo O(n)
     */
    private List<GraphNode<L>> queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un grafo
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *                  il grafo su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è orientato
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                      se il grafo passato contiene almeno
     *                                      un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {
        if (graph == null) {
            throw new NullPointerException("Il grafo dato è nullo");
        }
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Il grafo non è orientato.");
        }
        for (GraphEdge<L> edge: graph.getEdges()) {
            if (!edge.hasWeight())
                throw new IllegalArgumentException("Trovato un arco non pesato.");
            if (edge.getWeight() < 0)
                throw new IllegalArgumentException("Trovato un arco con peso negativo.");
        }
        this.grafo = graph;
        this.queue = new ArrayList<GraphNode<L>>();
        this.isComputed = false;
        this.lastSource = null;
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        // Controlla se il grafo e il nodo sorgente sono validi
        this.checkComputationParameters(sourceNode);
        // Inizializza i nodi del grafo
        this.intializeSingleSource(sourceNode);
        // Imposta l'ultima sorgente
        this.lastSource = sourceNode;
        // Crea una lista di nodi il cui peso finale nel cammino è stato determinato
        List<GraphNode<L>> determinedNodes = new ArrayList<GraphNode<L>>();
        // Inserisce i nodi del grafo nella coda di priorità
        for(GraphNode<L> node: this.grafo.getNodes()) {
            this.queue.add(node);
        }
        // Finché la coda non è vuota
        while(!this.queue.isEmpty()) {
            // Estrae il minimo
            GraphNode<L> extractedNode = this.extractMinNode();
            // Aggiunge il nodo estratto all'insieme dei nodi determinati
            determinedNodes.add(extractedNode);
            for(GraphEdge<L> edge: this.grafo.getEdgesOf(extractedNode)) {
                // Rilassamento del nodo
                L node2Label = edge.getNode2().getLabel();
                // Calcola la somma tra il peso dell'arco e la distanza del nodo estratto
                double weightPlusDistance = edge.getWeight() + extractedNode.getFloatingPointDistance();
                // Se la somma è minore della distanza dell'altro nodo
                if(weightPlusDistance < grafo.getNodeOf(node2Label).getFloatingPointDistance()) {
                    // Imposta la distanza dell'altro nodo alla somma
                    grafo.getNodeOf(node2Label).setFloatingPointDistance(weightPlusDistance);
                    // Imposta il nodo estratto come predecessore dell'altro nodo
                    grafo.getNodeOf(node2Label).setPrevious(edge.getNode1());
                }
            }
        }
        // Imposta il flag a true
        this.isComputed = true;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        if (!this.isComputed)
            throw new IllegalStateException("Richiesta last source, ma non "
                    + "sono mai stati calcolati i cammini minimi");
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.grafo;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        // Controlla se il nodo è nullo
        if(targetNode == null) {
            throw new NullPointerException("Il nodo target è nullo");
        }
        // Controlla se il nodo esiste nel grafo
        if(this.grafo.getNodeOf(targetNode.getLabel()) == null) {
            throw new IllegalArgumentException("Il nodo sorgente non esiste nel grafo");
        }
        // Controlla se il calcolo è stato eseguito almeno una volta
        if(!isComputed) {
            throw new IllegalStateException("Il calcolo dei cammini minimi non è stato mai eseguito");
        }

        // Crea una lista di archi
        List<GraphEdge<L>> edges = new ArrayList<>();
        // Salva il nodo target
        GraphNode<L> currentNode = targetNode;
        // Se il nodo target non corrisponde all'ultimo nodo sorgente di cui è stato calcolato il cammino minimo
        if (!targetNode.equals(this.lastSource)) {
            // Scorre all'indietro a partire dal target
            while (currentNode.getPrevious() != null) {
                // Ottiene il predecessore
                GraphNode<L> previous = currentNode.getPrevious();
                // Itera sugli archi del nodo predecessore
                for (GraphEdge<L> edge : this.grafo.getEdgesOf(previous)) {
                    if (edge.getNode2().equals(currentNode))
                        edges.add(0, edge);
                }
                // Scorre al predecessore
                currentNode = previous;
            }
        }
        // Se la lista di archi è vuota
        if (!edges.isEmpty()) {
            if ((edges.get(0).getNode1().equals(this.lastSource))) {
                return edges;
            } else {
                return null;
            }
        }
        return edges;
    }

    /**
     * Estrae dalla coda di priorità il nodo con chiave (distanza) minima
     * @return il nodo con chiave (distanza) minima
     */
    private GraphNode<L> extractMinNode() {
        // Prende come riferimento il primo elemento
        GraphNode<L> minimum = this.queue.get(0);
        // Scorre la coda di priorità cercando il minimo
        for(GraphNode<L> node: this.queue) {
            if(node.getFloatingPointDistance() < minimum.getFloatingPointDistance()) {
                minimum = node;
            }
        }
        // Rimuove il nodo dalla coda
        this.queue.remove(minimum);
        // Ritorna il nodo
        return minimum;
    }


    private void relax(GraphNode<L> u, GraphNode<L> v, int weight) {

    }

    /**
     * Inizializza i valori delle distanze di tutti i nodi della grafica a infinito,
     * tranne per il nodo di partenza, che viene impostato a zero.
     * Inoltre, imposta il predecessore di tutti i nodi a null.
     * Questa procedura viene eseguita all'inizio dell'algoritmo di Dijkstr
     * per preparare la grafica per l'elaborazione e per determinare il percorso
     * più corto dal nodo di partenza a tutti gli altri nodi.
     * @param sourceNode
     *                      il nodo sorgente da cui calcolare i cammini minimi
     *                      verso tutti gli altri nodi del grafo
     */
    private void intializeSingleSource(GraphNode<L> sourceNode) {
        for(GraphNode<L> node: this.grafo.getNodes()) {
            // Inizializza la distanza del nodo a infinito
            node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            // Inizializza il colore a bianco (non visitato)
            node.setColor(GraphNode.COLOR_WHITE);
            // inizializza precedessore a null
            node.setPrevious(null);
        }
        // Inizializza distanza del nodo sorgente a zero
        sourceNode.setFloatingPointDistance(0);
    }

    /**
     * Controlla se il grafo e il nodo sorgente utilizzati per la computazione di Prim sono validi
     * @param sourceNode
     *                      il nodo sorgente da cui calcolare i cammini minimi
     *                      verso tutti gli altri nodi del grafo
     */
    private void checkComputationParameters( GraphNode<L> sourceNode) {
        // Controlla se il nodo sorgente è nullo
        if(sourceNode == null) {
            throw new NullPointerException("Il nodo sorgente è nullo");
        }
        // Controlla se il nodo esiste nel grafo
        if(this.grafo.getNodeOf(sourceNode.getLabel()) == null) {
            throw new IllegalArgumentException("Il nodo sorgente non esiste nel grafo");
        }
        // Controlla se il calcolo può essere svolto
        for(GraphEdge<L> edge: this.grafo.getEdges()) {
            if(edge.getWeight() < 0) {
                throw new IllegalStateException("Il grafo contiene archi con pesi negativi");
            }
        }
    }

}
