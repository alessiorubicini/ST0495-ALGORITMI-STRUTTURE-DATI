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
 * @author Luca Tesei (template) **INSERIRE NOME, COGNOME ED EMAIL
 *         xxxx@studenti.unicam.it DELLO STUDENTE** (implementazione)
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
        int edgeCount = 0;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                GraphEdge<L> edge = matrix.get(i).get(j);
                if (edge != null) {
                    edgeCount++;
                }
            }
        }

        return edgeCount;
    }

    @Override
    public void clear() {
        this.nodesIndex.clear();
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
            // Aggiunge il nodo alla lista dei nodi indice
            this.nodesIndex.put(node, nodesIndex.size());
            // Crea una nuova riga della matrice delle adiacenze per il nuovo nodo
            // La imposta tutta a null
            this.matrix.add(new ArrayList<GraphEdge<L>>(Collections.nCopies(this.nodeCount() + 1, null)));
            for (ArrayList<GraphEdge<L>> row : this.matrix) {
                row.add(null);
            }
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
        // Richiama il metodo di aggiunta del nodo
        return this.addNode(new GraphNode<L>(label));
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
        // Elimina il nodo
        this.nodesIndex.remove(node);
        // Modifica l'indice dei nodi successivi
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            int index = entry.getValue();
            if (index > nodeToRemoveIndex) {
                entry.setValue(index - 1);
            }
        }
        // Elimina gli archi corrispondenti al nodo eliminato
        matrix.remove(nodeToRemoveIndex);
        for (ArrayList<GraphEdge<L>> row : matrix) {
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
        // Controlla se l'etichetta passata è nulla
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Richiama il metodo di rimozione del nodo
        GraphNode<L> node = new GraphNode<L>(label);
        this.removeNode(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // Controlla che l'indice sia corretto
        if(i < 0 || i > this.nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        // Ottiene l'insieme dei nodi
        Set<GraphNode<L>> nodes = nodesIndex.keySet();
        // Itera sui nodi
        for (GraphNode<L> node : nodes) {
            // Quando incontra l'indice dato
            if (nodesIndex.get(node) == i) {
                // Richiama la funzione di rimozione
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
        // Controlla se il nodo dato è nullo
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Crea il nodo con l'etichetta
        GraphNode<L> node = new GraphNode<L>(label);
        return this.getNode(node);
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // Controlla che l'indice sia corretto
        if(i < 0 || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        // Itera sui nodi
        for (GraphNode<L> node : nodesIndex.keySet()) {
            // Quando incontra l'indice dato
            if (nodesIndex.get(node) == i) {
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
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        GraphNode<L> node = new GraphNode<L>(label);
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
            throw new NullPointerException("The edge cannot be null.");
        }
        if(edge.isDirected()) {
            throw new IllegalArgumentException();
        }
        // Ottiene gli indici dei nodi collegati
        int node1Index = this.getNodeIndexOf(edge.getNode1());
        int node2Index = this.getNodeIndexOf(edge.getNode2());
        // Controlla se esiste già un arco uguale
        if(matrix.get(node1Index).get(node2Index) != null) return false;
        // Aggiunge l'arco alla matrice di adiacenza
        this.matrix.get(node1Index).set(node2Index, edge);
        this.matrix.get(node2Index).set(node1Index,new GraphEdge<L>(edge.getNode2(), edge.getNode1(), false));
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Controlla se uno dei noti dati è nullo
        if(node1 == null || node2 == null) {
            throw new NullPointerException("I nodi dati sono nulli");
        }
        // Crea l'arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Aggiunge l'arco
        return this.addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // Controlla se i nodi dati sono nulli
        if(node1 == null || node2 == null) {
            throw new NullPointerException("I nodi dati sono nulli");
        }
        // Crea l'arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Imposta il peso
        edge.setWeight(weight);
        // Aggiunge l'arco
        return this.addEdge(edge);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // Controlla se le etichette date sono nulle
        if(label1 == null || label2 == null) {
            throw new NullPointerException("Le etichette date sono nulle");
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
            throw new NullPointerException("Le etichette date sono nulle");
        }
        // Crea i nodi corrispondenti
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        // Aggiunge l'arco
        return this.addWeightedEdge(node1, node2, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, false);

        // Controlla se l'arco esiste già
        if (this.matrix.get(i).get(j) != null) {
            return false;
        }

        this.matrix.get(i).set(j, edge);
        this.matrix.get(j).set(i, new GraphEdge<>(node2, node1, false));
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
        if (edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Uno dei nodi dell'arco non esiste");
        }

        int node1Index = nodesIndex.get(node1);
        int node2Index = nodesIndex.get(node2);

        if (matrix.get(node1Index).get(node2Index) == null) {
            throw new IllegalArgumentException("L'arco non esiste nel grafo");
        }
        matrix.get(node1Index).set(node2Index, null);
        matrix.get(node2Index).set(node1Index, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        this.removeEdge(edge);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        this.removeEdge(node1, node2);
    }

    @Override
    public void removeEdge(int i, int j) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
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
        if (edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Ottiene i nodi dell'arco
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        // Controlla se l'arco esiste
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("L'arco dato non esiste nel grafo.");
        }

        int node1Index = nodesIndex.get(node1);
        int node2Index = nodesIndex.get(node2);

        if (this.matrix.get(node1Index).get(node2Index) != null) {
            return matrix.get(node1Index).get(node2Index);
        } else {
            return null;
        }
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        return this.getEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return this.getEdge(node1, node2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount()-1 || j < 0 || j > nodeCount()-1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        // Controlla se gli indici corrispondono
        if(!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j)) {
            throw new IndexOutOfBoundsException("Indici non corrispondono a nessun nodo");
        }
        return(matrix.get(i).set(j, null));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Il nodo dato è nulo");
        }
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo dato non esiste nel grafo.");
        }
        int nodeIndex = nodesIndex.get(node);
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();
        for (int i = 0; i < matrix.get(nodeIndex).size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                adjacentNodes.add(getNode(i));
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        GraphNode<L> node = new GraphNode<L>(label);
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();

        for (int j = 0; j < matrix.get(i).size(); j++) {
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

        int nodeIndex = nodesIndex.get(node);
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();

        for (int i = 0; i < matrix.get(nodeIndex).size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                edges.add(matrix.get(nodeIndex).get(i));
            }
        }

        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        GraphNode<L> node = new GraphNode<L>(label);
        return this.getEdgesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // Controlla se gli indici rientrano nel range
        if (i < 0 || i > nodeCount() - 1) {
            throw new IndexOutOfBoundsException("Indici non validi");
        }
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();

        for (int j = 0; j < matrix.get(i).size(); j++) {
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
        // Crea un insieme di archi per l'output
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
