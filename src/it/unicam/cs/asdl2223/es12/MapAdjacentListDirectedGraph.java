/**
 * 
 */
package it.unicam.cs.asdl2223.es12;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi uscenti dal nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presenta basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListDirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListDirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;
        // Itera su tutte le liste di adiacenza
        for (Set<GraphEdge<L>> edges : this.adjacentLists.values()) {
            // Aggiungi il numero di archi per il nodo corrente al contatore
            count += edges.size();
        }
        return count;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi orientati
        return true;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) throw new NullPointerException("Il nodo dato è nullo");
        // Controlla se il nodo è già presente nel grafo
        if (!adjacentLists.containsKey(node)) {
            // Aggiunge il nodo al grafo
            this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        }
        return false;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) {
            throw new NullPointerException("Il nodo dato è null");
        }
        // Controlla se il nodo dato è presente nel grafo
        return this.adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        // Controlla se l'etichetta data è nulla
        if (label == null) {
            throw new NullPointerException("L'etichetta data è null");
        }
        // Cerca il nodo con l'etichetta specificata nelle liste di adiacenza
        for (GraphNode<L> node : this.adjacentLists.keySet()) {
            if (node.getLabel().equals(label)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) throw new NullPointerException("Il nodo dato è nullo");
        // Controlla che il nodo appartiene al grafo
        if (!this.adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non appartiene al grafo");
        }
        // Crea l'insieme dei nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();
        // Itera su tutti gli archi uscenti dal nodo
        for (GraphEdge<L> edge : this.adjacentLists.get(node)) {
            // Aggiunge il nodo di arrivo all'insieme dei nodi adiacenti
            adjacentNodes.add(edge.getNode2());
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) throw new NullPointerException("Il nodo non può essere null");
        // Se il nodo non esiste, non può avere predecessori
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        // Crea l'insieme dei nodi predecessori
        Set<GraphNode<L>> predecessors = new HashSet<GraphNode<L>>();
        // Itera sulla mappa
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : adjacentLists.entrySet()) {
            // Per ogni nodo
            GraphNode<L> currentNode = entry.getKey();
            // e la sua relativa lista di adiacenza
            Set<GraphEdge<L>> edges = entry.getValue();
            // Se il nodo ha un arco uscente che arriva al nodo cercato, allora è un predecessore
            for (GraphEdge<L> edge : edges) {
                if (edge.getNode2().equals(node)) {
                    predecessors.add(currentNode);
                }
            }
        }
        return predecessors;
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // Crea l'insieme degli archi
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Itera sulla mappa
        for (Set<GraphEdge<L>> edgeSet : this.adjacentLists.values()) {
            // Aggiunge gli archi di questo nodo all'insieme di archi
            edges.addAll(edgeSet);
        }
        return edges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if (edge == null) throw new NullPointerException("Arco nullo");
        // Controlla se l'arco è orientato e se i suoi nodi esistono
        if (!edge.isDirected() || !this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) {
            throw new IllegalArgumentException("I due nodi dell'arco non appartengono al grafo");
        }
        // Se il primo nodo esiste già nella mappa
        if (this.adjacentLists.containsKey(edge.getNode1())) {
            // Aggiungiamo l'arco alla sua lista di adiacenza
            return this.adjacentLists.get(edge.getNode1()).add(edge);
        } else {
            // Altrimenti se il primo nodo non esiste ancora nella mappa, lo aggiungiamo insieme all'arco
            Set<GraphEdge<L>> edges = new HashSet<>();
            edges.add(edge);
            this.adjacentLists.put(edge.getNode1(), edges);
            return true;
        }
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if (edge == null) throw new NullPointerException("L'arco dato è nullo");
        // Controlla se i nodi dell'arco dato esistono
        if (!adjacentLists.containsKey(edge.getNode1()) || !adjacentLists.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("L'arco passato non appartiene a questo grafo");
        }
        // Itera sulla mappa
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : adjacentLists.entrySet()) {
            // Ottiene l'insieme di archi associati al nodo corrente
            Set<GraphEdge<L>> edges = entry.getValue();
            // Controlla se l'arco dato è nell'insieme di archi del nodo corrente
            if (edges.contains(edge)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) throw new NullPointerException("Il nodo passato è null");
        // Controlla se il nodo dato esiste
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non appartiene al grafo");
        }
        // Crea un insieme di archi
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Ottiene l'insieme degli archi del nodo
        Set<GraphEdge<L>> adjacentEdges = this.adjacentLists.get(node);
        // Se il nodo ha archi, li aggiunge all'insieme
        if (adjacentEdges != null) {
            edges.addAll(adjacentEdges);
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) throw new NullPointerException("Nodo nullo");
        // Controlla se il nodo dato esiste
        if (!this.adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        }
        // Crea un insieme degli archi
        Set<GraphEdge<L>> ingoingEdges = new HashSet<>();
        // Itera sulla mappa
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists.entrySet()) {
            // Per ogni nodo
            GraphNode<L> n = entry.getKey();
            // e la sua relativa lista di adiacenza
            Set<GraphEdge<L>> edges = entry.getValue();
            // Se il nodo ha un arco uscente che arriva al nodo cercato, allora l'arco è l'arco entrante
            for (GraphEdge<L> edge : edges) {
                if (edge.getNode2().equals(node)) {
                    ingoingEdges.add(edge);
                }
            }
        }
        return ingoingEdges;
    }

    /**
     * Returns the set of edges that are adjacent to the specified node.
     *
     * @param node the node
     * @return the set of adjacent edges, or an empty set if the node is not in the graph
     */
    public Set<GraphEdge<L>> getAdjacentEdgesOf(GraphNode<L> node) {
        return this.adjacentLists.getOrDefault(node, new HashSet<>());
    }


}
