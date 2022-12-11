/**
 * 
 */
package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        return this.matrix.size();
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
        if(!this.nodesIndex.containsKey(node)) {
            return null;
        }
        // Ritorna il nodo
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
        // Crea il nodo
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
        // TODO implementare
        return false;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // TODO implementare
        return false;
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {
        // TODO implementare
        return false;
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // TODO implementare
        return false;
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // TODO implementare
        return false;
    }

    @Override
    public boolean addEdge(int i, int j) {
        // TODO implementare
        return false;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // TODO implementare
        return false;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // TODO implementare
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // TODO implementare
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // TODO implementare
    }

    @Override
    public void removeEdge(int i, int j) {
        // TODO implementare
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // TODO implementare
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // TODO implementare
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // TODO implementare
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // TODO implementare
        return null;
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
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // TODO implementare
        return null;
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
        // TODO implementare
        return null;
    }
}
