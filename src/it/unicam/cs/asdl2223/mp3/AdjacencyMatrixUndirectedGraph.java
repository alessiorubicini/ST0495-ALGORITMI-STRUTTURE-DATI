/**
 * 
 */
package it.unicam.cs.asdl2223.mp3;

import com.sun.xml.internal.ws.addressing.WsaActionUtil;

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
 * @author Luca Tesei (template), Alessio Rubicini alessio.rubicini@studenti.unicam.it (implementazione)
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
        // Initialize the edge count to 0
        int edgeCount = 0;

        // Loop over all the nodes in the graph and count the number of edges
        for (int i = 0; i < matrix.size(); i++) {
            // Loop over all the other nodes in the graph and check if there is an edge between the current
            // node and the other node
            for (int j = 0; j < matrix.get(i).size(); j++) {
                // Get the edge between the current node and the other node
                GraphEdge<L> edge = matrix.get(i).get(j);
                if (edge != null) {
                    // There is an edge, so increment the edge count
                    edgeCount++;
                }
            }
        }

        return edgeCount;
    }

    @Override
    public void clear() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
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
        }
        // Aggiunge il nodo alla lista dei nodi indice
        this.nodesIndex.put(node, nodesIndex.size());
        // Aggiunge la rispettiva entry nella matrice degli archi
        this.matrix.add(new ArrayList<GraphEdge<L>>());
        return true;
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
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo è già presente
        if(!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        // Salva l'indice del nodo da rimuovere
        int nodeToRemoveIndex = nodesIndex.get(node);
        // Elimina il nodo
        this.nodesIndex.remove(node);
        // Modifica l'indice dei nodi successivi
        for (GraphNode<L> currentNode: nodesIndex.keySet()) {
            if(nodesIndex.get(currentNode) > nodeToRemoveIndex) {
                nodesIndex.put(currentNode, nodesIndex.get(currentNode)-1);
            }
        }
        // Elimina gli archi corrispondenti al nodo eliminato
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if(i == nodeToRemoveIndex || j == nodeToRemoveIndex) {
                    this.matrix.get(i).set(j, null);
                }
            }
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
        this.removeNode(new GraphNode<L>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // Controlla se l'indice dato rientra nel range
        if(this.isEmpty() || i > nodesIndex.size()-1) {
            throw new IndexOutOfBoundsException();
        }
        // Cerca l'indice dato
        for (GraphNode<L> currentNode : this.nodesIndex.keySet()) {
            // Se lo trova, richiama il metodo di rimozione sul nodo corrispondente
            if(nodesIndex.get(currentNode) == i) {
                this.removeNode(currentNode);
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
            if(node.equals(currentNode)) {
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
        return this.getNode(new GraphNode<L>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // Controlla se l'indice dato rientra nel range
        if(this.isEmpty() || i > nodesIndex.size()-1) {
            throw new IndexOutOfBoundsException();
        }
        // Ritorna il nodo
        for (GraphNode<L> currentNode : this.nodesIndex.keySet()) {
            if(nodesIndex.get(currentNode) == i) {
                return currentNode;
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
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        // Controlla se il nodo dato è nullo
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        // Crea il nodo
        GraphNode<L> node = new GraphNode<L>(label);
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        return this.nodesIndex.get(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if(edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Controlla se l'arco è valido
        if(!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()) || edge.isDirected()) {
            throw new IllegalArgumentException("Nodi non esistenti o Arco diretto");
        }
        // Controlla se l'arco esiste già
        if(this.getEdge(edge) != null) { return false; }
        // Ottiene l'indice del nodo collegato
        int node1Index = this.getNodeIndexOf(edge.getNode1());
        // Aggiunge l'arco alla matrice di adiacenza
        this.matrix.get(node1Index).add(edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Controlla se i nodi dati sono nulli
        if(node1 == null || node2 == null) {
            throw new IllegalArgumentException("I nodi dati sono nulli");
        }
        // Crea l'arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Aggiunge il nodo
        return this.addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // Controlla se i nodi dati sono nulli
        if(node1 == null || node2 == null) {
            throw new IllegalArgumentException("I nodi dati sono nulli");
        }
        // Crea l'arco che collega i due nodi
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        // Imposta il peso
        edge.setWeight(weight);
        // Aggiunge il nodo
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
        // Controlla se gli indici rientrano nel range
        if(this.isEmpty() || i > matrix.size() || j > matrix.size()) {
            throw new IndexOutOfBoundsException();
        }
        // Aggiunge l'arco alla matrice di adiacenza
        // Richiamando l'apposito metodo
        return this.addEdge(this.getNode(i), this.getNode(j));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // Controlla se gli indici rientrano nel range
        if(this.isEmpty() || i > matrix.size() || j > matrix.size()) {
            throw new IndexOutOfBoundsException();
        }
        // Aggiunge l'arco alla matrice di adiacenza
        // Richiamando l'apposito metodo
        return this.addWeightedEdge(this.getNode(i), this.getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if(edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Controlla se l'arco esiste
        if(!this.containsEdge(edge)) {
            throw new IllegalArgumentException("L'arco non esiste");
        }
        // Cerca l'arco nella matrice
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                // Se lo trova, lo elimina
                if(this.matrix.get(i).get(j).equals(edge)) {
                    this.matrix.get(i).set(j, null);
                }
            }
        }
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Controlla se i nodi dati sono nulli
        if(node1 == null || node2 == null) {
            throw new NullPointerException("I nodi dati sono nulli");
        }
        if(!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2) || this.getEdge(node1, node2) == null) {
            throw new IllegalArgumentException();
        }
        // Richiama il metodo di rimozione sull'arco che collega i due nodi
        this.removeEdge(this.getEdge(node1, node2));

        // TODO ERROR
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // Controlla se le etichette date sono nulle
        if(label1 == null || label2 == null) {
            throw new NullPointerException("Le etichette date sono nulle");
        }
        // Richiama il metodo di rimozione sull'arco che collega i nodi con queste etichette
        this.removeEdge(this.getEdge(label1, label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        // Controlla se gli indici rientrano nel range
        if(this.isEmpty() || i > matrix.size() || j > matrix.size()) {
            throw new IndexOutOfBoundsException();
        }
        // Richiama il metodo di rimozione sull'arco che collega i nodi con questi indici
        this.removeEdge(this.getEdge(i, j));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // Controlla se l'arco dato è nullo
        if(edge == null) {
            throw new NullPointerException("L'arco dato è nullo");
        }
        // Controlla se i nodi dell'arco dato esistono
        if(!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("I nodi dell'arco dato non esistono");
        }
        // Cerca l'arco nella matrice
        GraphEdge<L> currentEdge = null;
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                currentEdge = this.matrix.get(i).get(j);
                // Se lo trova, lo ritorna
                if(currentEdge.equals(edge)) {
                    return currentEdge;
                }
            }
        }
        // Altrimenti l'arco non c'è, quindi ritorna null
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Controlla se i nodi dati sono nulli
        if(node1 == null || node2 == null) {
            throw new NullPointerException("I nodi dati sono nulli");
        }
        // Richiama il metodo get sull'arco che collega i due nodi
        return this.getEdge(new GraphEdge<L>(node1, node2, false));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // Controlla se le etichette date sono nulle
        if(label1 == null || label2 == null) {
            throw new NullPointerException("Le etichette date sono nulle");
        }
        return this.getEdge(this.getNode(label1), this.getNode(label2));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // Controlla se gli indici sono validi
        if((i < 0 || i > nodeCount()-1) || (j < 0 || j > nodeCount()-1)) {
            throw new IndexOutOfBoundsException();
        }
        // Cerca l'arco nella matrice
        GraphEdge<L> currentEdge = null;
        for (int a = 0; a < matrix.size(); a++) {
            for (int b = 0; b < matrix.get(a).size(); b++) {
                // Se lo trova, lo elimina
                currentEdge = this.matrix.get(a).get(b);
                if(a == i && b == j) {
                    return currentEdge;
                }
            }
        }
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // Controlla se il nodo dato è nullo
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) return null;
        // Crea un insieme di nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        // Itera su tutti i nodi
        int index = nodesIndex.get(node);
        for (int i = 0; i < matrix.size(); i++) {
            // Ottiene l'arco tra il nodo dato e il nodo corrente
            GraphEdge<L> edge = matrix.get(index).get(i);
            if (edge != null) {
                // L'arco esiste, quindi aggiunge il nodo all'insieme dei nodi adiacenti a quello dato
                adjacentNodes.add(edge.getNode1());
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // Controlla se le etichette date sono nulle
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        return this.getAdjacentNodesOf(this.getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // Controlla se l'indice dato rientra nel range
        if(this.isEmpty() || i > nodesIndex.size()-1) {
            throw new IndexOutOfBoundsException();
        }
        return this.getAdjacentNodesOf(this.getNode(i));
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
        if(node == null) {
            throw new NullPointerException("Il nodo dato è nullo");
        }
        // Controlla se il nodo esiste
        if(!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esitse");
        }
        // Crea un insieme di archi
        Set<GraphEdge<L>> incidentEdges = new HashSet<>();
        // Itera su tutti i nodi
        int index = nodesIndex.get(node);
        for (int i = 0; i < matrix.size(); i++) {
            // Ottiene l'arco tra il nodo dato e il nodo corrente
            GraphEdge<L> edge = matrix.get(index).get(i);
            if (edge != null) {
                // L'arco esiste, quindi aggiunge il nodo all'insieme dei nodi adiacenti a quello dato
                incidentEdges.add(edge);
            }
        }

        return incidentEdges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // Controlla se le etichette date sono nulle
        if(label == null) {
            throw new NullPointerException("L'etichetta data è nulla");
        }
        return this.getEdgesOf(this.getNode(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // Controlla se l'indice dato rientra nel range
        if(this.isEmpty() || i > nodesIndex.size()-1) {
            throw new IndexOutOfBoundsException();
        }
        return this.getEdgesOf(this.getNode(i));
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

    private boolean containsEdge(GraphEdge<L> edge) {
        GraphEdge<L> currentEdge = null;
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                currentEdge = matrix.get(i).get(j);
                if(currentEdge.equals(edge)) {
                    return true;
                }
            }
        }
        return false;
    }
}
