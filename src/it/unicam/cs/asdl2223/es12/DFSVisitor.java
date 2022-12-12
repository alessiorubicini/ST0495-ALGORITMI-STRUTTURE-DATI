package it.unicam.cs.asdl2223.es12;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // Inizializza il grafo
        for (GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            node.setIntegerDistance(-1);
            node.setPrevious(null);
        }
        // Inizializza il tempo
        this.time = 0;
        // Esegue la DFS su ogni nodo
        for (GraphNode<L> node : g.getNodes()) {
            if (node.getColor() == GraphNode.COLOR_WHITE) {
                recDFS(g, node);
            }
        }
    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // Imposta il colore del nodo corrente su grigio
        u.setColor(GraphNode.COLOR_GREY);
        // Incrementa il tempo
        time++;
        // Imposta il tempo di ingresso e di uscita per il nodo corrente
        u.setEnteringTime(time);
        u.setExitingTime(time);
        // Itera sugli archi in uscita del nodo corrente
        for (GraphEdge<L> e : g.getEdgesOf(u)) {
            // Per ogni arco ottiene il nodo destinazione
            GraphNode<L> neighbor = e.getNode2();
            // Se il colore del nodo destinazione è bianco, vuol dire che è ancora da visitare
            if (neighbor.getColor() == GraphNode.COLOR_WHITE) {
                neighbor.setPrevious(u);
                // Richiama ricorsivamente la DFS sul nodo di destinazione
                recDFS(g, neighbor);
            }
        }
        // Imposta il colore del nodo a nero perchè è stato visitato
        u.setColor(GraphNode.COLOR_BLACK);
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
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
