/**
 *
 */
package it.unicam.cs.asdl2223.mp3;

import java.util.*;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 *
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template) Alessio Rubicini alessio.rubicini@studenti.unicam.it (implementazione)
 *
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        // Inizializza il contatore di archi
        int edgeCount = 0;
        // Conta gli archi presenti nella matrice
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                GraphEdge<L> edge = matrix.get(i).get(j);
                if (edge != null) {
                    edgeCount++;
                }
            }
        }
        // Ritorna il contatore diviso in modo da considerare ogni arco una sola volta (essendo un grafo non orientato)
        return edgeCount/2;
    }

    @Override
    public void clear() {
        // Pulisce la mappa dei nodi
        this.nodesIndex.clear();
        // Pulisce la matrice degli archi
        this.matrix.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo è già presente
        if(this.nodesIndex.containsKey(node)) {
            return false;
        } else {
            // Se non c'è, aggiunge il nodo alla mappa dei nodi indice
            this.nodesIndex.put(node, nodesIndex.size());
            // Aggiunge una nuova casella alle righe della matrice
            for (ArrayList<GraphEdge<L>> row : this.matrix) {
                row.add(null);
            }
            // Crea una nuova riga della matrice per il nuovo nodo
            ArrayList<GraphEdge<L>> newRow = new ArrayList<GraphEdge<L>>(Collections.nCopies(this.nodeCount() + 1, null));
            // La aggiunge alla matrice
            this.matrix.add(newRow);
            return true;
        }

    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        // Controlla se l'etichetta data è nulla
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Aggiunge il nodo
        return this.addNode(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if(node == null) throw new NullPointerException("Il nodo dato è nullo");
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        // Salva l'indice del nodo da rimuovere
        int nodeToRemoveIndex = nodesIndex.get(node);
        // Elimina il nodo dalla mappa dei nodi
        this.nodesIndex.remove(node);
        // Modifica l'indice dei nodi successivi nella mappa
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            // Ottiene l'indice del nodo corrente
            int nodeIndex = entry.getValue();
            // Se l'indice è maggiore, lo decrementa
            if (nodeIndex > nodeToRemoveIndex) {
                entry.setValue(nodeIndex - 1);
            }
        }
        // Elimina gli archi corrispondenti al nodo eliminato
        this.matrix.remove(nodeToRemoveIndex);
        for (ArrayList<GraphEdge<L>> row : this.matrix) {
            row.remove(nodeToRemoveIndex);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        // Controlla se l'etichetta data è nulla
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Rimuove il nodo
        this.removeNode(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // Controlla che l'indice dato sia corretto
        if(i < 0 || i > this.nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        // Ottiene l'insieme dei nodi del grafo
        Set<GraphNode<L>> nodes = nodesIndex.keySet();
        // Itera sui nodi
        for (GraphNode<L> node : nodes) {
            // Quando incontra l'indice dato
            if (nodesIndex.get(node) == i) {
                // Rimuove il nodo
                this.removeNode(node);
                // Esce dal ciclo
                break;
            }
        }
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) return null;
        // Cerca e ritorna il nodo
        for (GraphNode<L> currentNode : this.nodesIndex.keySet()) {
            if(currentNode.equals(node)) {
                return currentNode;
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        // Controlla se l'etichetta data è nulla
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Ritorna il nodo dal grafo
        return this.getNode(node);
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // Controlla che l'indice sia corretto
        if(i < 0 || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        // Itera sui nodi del grafo
        for (GraphNode<L> node : nodesIndex.keySet()) {
            // Quando incontra il nodo con l'indice dato
            if (nodesIndex.get(node) == i) {
                // Ritorna il nodo
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non esiste");
        }
        // Ritorna il nodo dato dalla mappa dei nodi
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Ritorna l'indice del nodo
        return this.getNodeIndexOf(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Controllo se l'arco dato è nullo
        if (edge == null) {
            throw new NullPointerException("L'arco dato è nullo.");
        }
        // Controlla se l'arco è orientato
        if(edge.isDirected()) {
            throw new IllegalArgumentException("L'arco non può essere orientato");
        }
        // Ottiene gli indici dei nodi collegati dall'arco dato
        int node1Index = this.getNodeIndexOf(edge.getNode1());
        int node2Index = this.getNodeIndexOf(edge.getNode2());
        // Controlla se esiste già un arco uguale
        if(matrix.get(node1Index).get(node2Index) != null) return false;
        // Aggiunge l'arco alla matrice di adiacenza
        this.matrix.get(node1Index).set(node2Index, edge);
        // Aggiunge anche l'arco inverso (non essendo il grafo orientato)
        GraphEdge<L> reversedEdge = new GraphEdge<L>(edge.getNode2(), edge.getNode1(), false);
        reversedEdge.setWeight(edge.getWeight());
        this.matrix.get(node2Index).set(node1Index, reversedEdge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Crea l'arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Aggiunge l'arco creato
        return this.addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // Crea l'arco che collega i nodi dati
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Imposta il peso dell'arco
        edge.setWeight(weight);
        // Aggiunge l'arco creato
        return this.addEdge(edge);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // Controlla se le etichette date sono nulle
        if(label1 == null || label2 == null) {
            throw new NullPointerException("Le etichette date sono nulle.");
        }
        // Crea i nodi corrispondenti
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        // Aggiunge l'arco che collega i due nodi
        return this.addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // Controlla se le etichette date sono nulle
        if(label1 == null || label2 == null) {
            throw new NullPointerException("Le etichette date sono nulle.");
        }
        // Crea i nodi con le etichette date
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        // Aggiunge l'arco con il peso dato
        return this.addWeightedEdge(node1, node2, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        // Controlla se gli indici dati sono validi
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        // Ottiene i rispettivi nodi
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        // Crea un arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, false);
        // Aggiunge l'arco
        this.addEdge(edge);
        return true;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        // Aggiunge l'arco alla matrice di adiacenza
        return this.addWeightedEdge(this.getNode(i), this.getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if (edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Ottiene i nodi collegati dall'arco dato
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        // Controlla se i nodi esistono
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Uno dei nodi dell'arco non esiste");
        }
        // Ottiene gli indici dei nodi
        int node1Index = nodesIndex.get(node1);
        int node2Index = nodesIndex.get(node2);
        // Controlla se l'arco esiste
        if (matrix.get(node1Index).get(node2Index) == null) {
            throw new IllegalArgumentException("L'arco non esiste nel grafo");
        }
        // Elimina le entry dell'arco dalla matrice (impostandole a null)
        matrix.get(node1Index).set(node2Index, null);
        matrix.get(node2Index).set(node1Index, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Crea un arco non-orientato con i nodi dati
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Rimuove l'arco
        this.removeEdge(edge);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // Crea i nodi con le etichette date
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        // Rimuove l'arco che collega i due nodi
        this.removeEdge(node1, node2);
    }

    @Override
    public void removeEdge(int i, int j) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi.");
        }
        // Controlla se l'arco esiste
        if (matrix.get(i).get(j) == null) {
            throw new IllegalArgumentException("L'arco specificato non esiste nel grafo.");
        }
        // Imposta a null i valori nella matrice di adiacenza
        matrix.get(i).set(j, null);
        matrix.get(j).set(i, null);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if (edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Ottiene i nodi dell'arco
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        // Controlla se i nodi dell'arco esistono
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("I nodi dell'arco non esistono.");
        }
        // Ottiene gli indici dei nodi
        int node1Index = nodesIndex.get(node1);
        int node2Index = nodesIndex.get(node2);
        // Se l'arco esiste, lo ritorna
        if (this.matrix.get(node1Index).get(node2Index) != null) {
            return matrix.get(node1Index).get(node2Index);
        } else {
            return null;
        }
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Crea un arco non orientato con i nodi dati
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Ritorna lo stesso arco dal grafo
        return this.getEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // Crea due nodi con le etichette date
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        // Ritorna l'arco che collega i due nodi
        return this.getEdge(node1, node2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        // Controlla se gli indici corrispondono a nodi esistenti
        if(!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j)) {
            throw new IndexOutOfBoundsException("Indici non corrispondono a nessun nodo");
        }
        // Ritorna l'arco che collega i due nodi
        return(matrix.get(i).get(j));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // Controlal se il nodo dato è nullo
        if (node == null) {
            throw new NullPointerException("Il nodo dato è nullo.");
        }
        // Controlla se il nodo dato esiste
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non esiste nel grafo.");
        }
        // Ottiene l'indice del nodo dato
        int nodeIndex = nodesIndex.get(node);
        // Crea un insieme di nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();
        // Itera sugli archi del nodo
        for (int i = 0; i < matrix.get(nodeIndex).size(); i++) {
            // Se esiste un arco, aggiunge il nodo a cui è collegato all'insieme
            if (matrix.get(nodeIndex).get(i) != null) {
                adjacentNodes.add(getNode(i));
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Ritorna i nodi adiacenti al nodo
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // Controlla se l'indice rientra nel range
        if (i < 0 || i > nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indice non valido.");
        }
        // Crea un insieme di nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();
        // Itera sulla rispettiva riga del nodo nella matrice di adiacenze
        for (int j = 0; j < matrix.get(i).size(); j++) {
            // Se l'arco esiste, aggiunge l'altro nodo all'insieme
            if (matrix.get(i).get(j) != null) {
                adjacentNodes.add(getNode(j));
            }
        }

        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if (node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo esiste
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non esiste nel grafo.");
        }
        // Ottiene l'indice del nodo dalla mappa
        int nodeIndex = nodesIndex.get(node);
        // Ottiene l'insieme di archi del nodo dato il suo indice
        Set<GraphEdge<L>> edges = this.getEdgesOf(nodeIndex);
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // Crea un nodo con l'etichetta data
        GraphNode<L> node = new GraphNode<L>(label);
        // Ritorna gli archi del nodo
        return this.getEdgesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // Controlla se l'indice rientra nel range
        if (i < 0 || i > nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indice non valido.");
        }
        // Crea un insieme di archi
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        // Itera sulla rispettiva riga del nodo nella matrice di adiacenze
        for (int j = 0; j < matrix.get(i).size(); j++) {
            // Se l'arco esiste, lo aggiunge all'insieme
            if (matrix.get(i).get(j) != null) {
                edges.add(matrix.get(i).get(j));
            }
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // Crea un insieme di archi
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Itera sulla matrice degli archi
        for (List<GraphEdge<L>> row : this.matrix) {
            for (GraphEdge<L> edge : row) {
                // Aggiunge gli archi esistenti all'insieme di archi
                if (edge != null) {
                    edges.add(edge);
                }
            }
        }
        // Ritorna l'insieme degli archi
        return edges;
    }
}
