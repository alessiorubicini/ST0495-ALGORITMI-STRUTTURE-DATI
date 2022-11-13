package it.unicam.cs.asdl2223.es7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Un oggetto di questa classe rappresenta un binary search tree, cioè un albero
 * binario di ricerca, realizzato tramite nodi ricorsivi (rappresentati da
 * oggetti della classe interna RecBST). Le API pubbliche chiamano i
 * corrispondenti metodi ricorsivi sul nodo RecBST che attualmente è la radice.
 * Questa classe non accetta elementi null e non accetta elementi duplicati.
 * 
 * Il binary search tree rappresentato da un oggetto di questa classe può essere
 * anche vuoto, mentre un oggetto della classe RecBST non può rappresentare un
 * albero vuoto: in quel caso l'albero vuoto è rappresentato dal fatto che il
 * puntatore all'oggetto RecBST è null, cioè dal fatto che l'oggetto non esiste.
 * 
 * La complessità delle operazioni di ricerca, inserimento e cancellazione nel
 * caso pessimo sono O(h) dove h è l'altezza dell'albero. Questa classe non
 * esegue un autobilanciamento dell'altezza, quindi nei casi degeneri la
 * complessità delle operazioni può diventare O(n) dove n è il numero degli
 * elementi presenti.
 * 
 * @param E
 *              il tipo delle etichette dei nodi in questo Binary Search Tree.
 *              La classe {@code E} deve avere un ordinamento naturale definito
 *              tra gli elementi.
 * 
 * @author Luca Tesei
 *
 */
public class BinarySearchTree<E extends Comparable<E>> {

    /*
     * Puntatore all'attuale nodo radice dell'albero, se null allora l'albero è
     * vuoto
     */
    private RecBST root;

    /*
     * Numero di nodi attualmente presenti in questo albero
     */
    private int size;

    /**
     * Crea un albero binario di ricerca vuoto.
     */
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Costruisce un albero contenente solo un nodo radice/foglia.
     * 
     * @param label
     *                  etichetta del nodo radice/foglia
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public BinarySearchTree(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta della radice null");
        this.root = new RecBST(label);
        this.size = 1;
    }

    /**
     * Determina se questo albero è vuoto.
     * 
     * @return true se questo albero è vuoto, false altrimenti
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Determina il numero di nodi in questo albero.
     * 
     * @return il numero di nodi in questo albero
     */
    public int size() {
        return this.size;
    }

    /**
     * Cancella tutti i nodi di questo albero, che quindi diventa vuoto.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Restituisce l'altezza di questo albero. L'altezza è definita come la
     * massima lunghezza di un percorso dal nodo radice a un nodo foglia in
     * questo albero. L'altezza dell'albero vuoto è -1, l'altezza dell'albero
     * con una radice/foglia è 0, e così via.
     * 
     * @return l'altezza di questo albero oppure -1 se questo albero è vuoto.
     */
    public int getHeight() {
        if (this.isEmpty())
            return -1;
        return this.root.computeHeight();
    }

    /**
     * Aggiunge ad una lista data la lista delle etichette dei nodi dell'albero
     * nell'ordine naturale.
     * 
     * @param l
     *              una lista (può essere anche vuota) su cui inserire le
     *              etichette in ordine.
     * @throws NullPointerException
     *                                  se la lista passata è null
     */
    public void addOrderedLabelsTo(List<E> l) {
        if (l == null)
            throw new NullPointerException(
                    "Lista null su cui aggiungere le etichette");
        if (this.isEmpty())
            return;
        this.root.addLabelsInOrder(l);
    }

    /**
     * Restituisce la lista ordinata delle etichette dei nodi di questo albero
     * secondo l'ordinamento naturale della classe {@code E}.
     * 
     * @return la lista ordinata delle etichette dei nodi di questo albero
     *         secondo l'ordinamento naturale della classe {@code E}
     */
    public List<E> getOrderedLabels() {
        if (this.isEmpty())
            return new ArrayList<E>();
        return this.root.inOrderVisit();
    }

    /**
     * Cerca un certo nodo in questo albero che ha una etichetta data.
     * 
     * @param label
     *                  l'etichetta da cercare
     * @return true se l'etichetta è presente, false altrimenti
     * 
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean contains(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta da cercare null");
        if (this.isEmpty())
            return false;
        RecBST n = this.root.search(label);
        if (n == null)
            return false;
        else
            return true;
    }

    /**
     * Restituisce l'etichetta più piccola, in base all'ordinamento naturale
     * della classe {@code E}, presente nell'albero.
     * 
     * @return l'etichetta minima presente nell'albero oppure null se l'albero è
     *         vuoto
     */
    public E getMin() {
        if (this.isEmpty())
            return null;
        return this.root.getMinNode().getLabel();
    }

    /**
     * Restituisce l'etichetta più grande, in base all'ordinamento naturale
     * della classe {@code E}, presente nell'albero.
     * 
     * @return l'etichetta massima presente nell'albero oppure null se l'albero
     *         è vuoto
     */
    public E getMax() {
        if (this.isEmpty())
            return null;
        return this.root.getMaxNode().getLabel();
    }

    /**
     * Restituisce l'etichetta successiva a una etichetta data secondo l'ordine
     * canonico della classe E.
     * 
     * @param label
     *                  l'etichetta di cui trovare il successore
     * 
     * @return l'etichetta successore di {@code label} in questo albero, oppure
     *         null se {@code label} non ha un successore
     * @throws IllegalArgumentException
     *                                      se l'etichetta {@code label} non è
     *                                      presente in questo albero
     * @throws NullPointerException
     *                                      se l'etichetta passata è null
     */
    public E getSuccessor(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Etichetta di cui cercare il successore null");
        if (this.isEmpty())
            throw new IllegalArgumentException(
                    "Tentativo di cercare il successore di una etichetta non esistente");
        RecBST n = this.root.search(label);
        if (n == null)
            throw new IllegalArgumentException(
                    "Tentativo di cercare il successore di una etichetta non esistente");
        RecBST succ = n.getSuccessorNode();
        if (succ == null)
            return null;
        else
            return succ.getLabel();
    }

    /**
     * Restituisce l'etichetta precedente a una etichetta data secondo l'ordine
     * canonico della classe E.
     * 
     * @param label
     *                  l'etichetta di cui trovare il predecessore
     * 
     * @return l'etichetta predecessore di {@code label} in questo albero,
     *         oppure null se {@code label} non ha un predecessore
     * @throws IllegalArgumentException
     *                                      se l'etichetta {@code label} non è
     *                                      presente in questo albero
     * @throws NullPointerException
     *                                      se l'etichetta passata è null
     */
    public E getPredecessor(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Etichetta di cui cercare il predecessore null");
        if (this.isEmpty())
            throw new IllegalArgumentException(
                    "Tentativo di cercare il predecessore di una etichetta non esistente");
        RecBST n = this.root.search(label);
        if (n == null)
            throw new IllegalArgumentException(
                    "Tentativo di cercare il predecessore di una etichetta non esistente");
        RecBST pred = n.getPredecessorNode();
        if (pred == null)
            return null;
        else
            return pred.getLabel();
    }

    /**
     * Aggiunge un nodo a questo albero con una etichetta specificata.
     * 
     * @param label
     *                  etichetta da inserire
     * 
     * @return true se il nodo è stato effettivamente inserito, false se
     *         l'etichetta era già presente
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean add(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta da aggiungere null");
        if (this.isEmpty()) {
            // aggiunge la radice
            this.root = new RecBST(label);
            this.size = 1;
            return true;
        } // chiama il corrispondente metodo sulla radice
        else {
            boolean result = this.root.insert(label);
            if (result)
                this.size++;
            return result;
        }
    }

    /**
     * Rimuove da questo albero il nodo contenente una certa etichetta.
     * 
     * @param label
     *                  l'etichetta del nodo da rimuovere
     * @return true se il nodo è stato rimosso, false se l'etichetta non era
     *         presente
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean remove(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere etichetta null");
        if (this.isEmpty())
            // etichetta sicuramente non presente in un albero vuoto
            return false;
        RecBST n = this.root.search(label);
        if (n == null)
            // etichetta non presente
            return false;
        n.deleteSelfLabel();
        this.size--;
        return true;
    }

    /**
     * Just for JUnit testing purposes.
     * 
     * @return the RecBST node corresponding to the root of this binary search
     *         tree.
     */
    protected RecBST getRoot() {
        return this.root;
    }

    /*
     * Classe interna che implementa tutti i metodi ricorsivamente e in cui ogni
     * nodo è un (sotto-)albero. Lo specificatore è protected solamente per
     * permettere i test JUnit.
     */
    protected class RecBST {
        /*
         * Etichetta associata al nodo
         */
        private E label;

        /*
         * Sottoalbero sinistro, se non presente vale null
         */
        private RecBST left;

        /*
         * Sottoalbero destro, se non presente vale null
         */
        private RecBST right;

        /*
         * Genitore di questo (sotto-)albero, può essere null quando questo nodo
         * è la radice dell'albero rappresentato dall'oggetto della classe
         * principale.
         */
        private RecBST parent;

        /*
         * Costruisce un (sotto-)albero che contiene solo la radice/foglia.
         * 
         * @param label etichetta da associare al nodo
         */
        protected RecBST(E label) {
            this.label = label;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        /*
         * Cancella l'etichetta di questo nodo dall'albero. Potrebbe non
         * eliminare proprio questo nodo, ma un'altro nodo, copiando l'etichetta
         * di quel nodo cancellato (scollegandolo dal parent) in questo nodo.
         */
        protected void deleteSelfLabel() {
            RecBST nodeToDelete = null;
            // Determino il nodo effettivo da eliminare
            if (this.left == null || this.right == null)
                nodeToDelete = this;
            else // questo nodo ha almeno un successore
                nodeToDelete = this.getSuccessorNode();
            // Determino il figlio non null più a sinistra del nodo da eliminare
            // (se esiste, altrimenti è null)
            RecBST leftMostNonNullChild = null;
            if (nodeToDelete.left != null)
                leftMostNonNullChild = nodeToDelete.left;
            else
                leftMostNonNullChild = nodeToDelete.right;
            // Collego il parent del figlio non null (se esiste) con il parent
            // del nodo da cancellare
            if (leftMostNonNullChild != null)
                leftMostNonNullChild.parent = nodeToDelete.parent;
            // Controllo se sto cancellando la radice dell'albero della classe
            // principale
            if (nodeToDelete.parent == null)
                // Aggiorno la radice dell'albero della classe principale
                BinarySearchTree.this.root = leftMostNonNullChild;
            else if (nodeToDelete.parent.left == nodeToDelete)
                // sto cancellando un figlio sinistro, quindi collego il nuovo
                // figlio a sinistra
                nodeToDelete.parent.left = leftMostNonNullChild;
            else // sto cancellando un figlio destro, quindi collego il nuovo
                 // figlio a destra
                nodeToDelete.parent.right = leftMostNonNullChild;
            // Infine, se il nodo che ho cancellato non è effettivamente questo
            // allora copio qui l'etichetta del nodo cancellato
            if (nodeToDelete != this)
                this.label = nodeToDelete.label;
        }

        /*
         * Costruisce un (sotto-albero) a partire da un nodo, due sotto-alberi e
         * un nodo genitore.
         * 
         * @param label etichetta da associare al nodo
         * 
         * @param aLeft sotto-albero sinistro, può essere null
         * 
         * @param aRight sotto-albero destro, può essere null
         * 
         * @param aParent nodo genitore, può essere null
         */
        protected RecBST(E label, RecBST aLeft, RecBST aRight, RecBST aParent) {
            this.label = label;
            this.left = aLeft;
            this.right = aRight;
            this.parent = aParent;
        }

        /**
         * @return the label
         */
        protected E getLabel() {
            return this.label;
        }

        /**
         * @param label
         *                  the label to set
         */
        protected void setLabel(E label) {
            this.label = label;
        }

        /**
         * @return the left
         */
        protected RecBST getLeft() {
            return left;
        }

        /**
         * @param left
         *                 the left to set
         */
        protected void setLeft(RecBST left) {
            this.left = left;
        }

        /**
         * @return the right
         */
        protected RecBST getRight() {
            return right;
        }

        /**
         * @param right
         *                  the right to set
         */
        protected void setRight(RecBST right) {
            this.right = right;
        }

        /**
         * @return the parent
         */
        protected RecBST getParent() {
            return parent;
        }

        /**
         * @param parent
         *                   the parent to set
         */
        protected void setParent(RecBST parent) {
            this.parent = parent;
        }

        /*
         * Restituisce l'altezza di questo nodo.
         * 
         * @return la lunghezza del massimo cammino da questo nodo a una foglia.
         */
        protected int computeHeight() {
            if (this.left == null && this.right == null)
                // Sono una radice foglia
                return 0;
            else if (this.left == null)
                // Ho solo il figlio destro
                return 1 + this.right.computeHeight();
            else if (this.right == null)
                // Ho solo il figlio sinistro
                return 1 + this.left.computeHeight();
            else
                // Ho tutti e due i figli
                return 1 + Math.max(this.left.computeHeight(),
                        this.right.computeHeight());
        }

        /*
         * Aggiunge ad una lista data le etichette dei nodi di questo
         * (sotto-)albero nell'ordine naturale. Per far questo esegue una visita
         * in-order di questo (sotto-)albero.
         * 
         * @param l una lista (può essere anche vuota) su cui inserire le
         * etichette in ordine
         */
        protected void addLabelsInOrder(List<E> l) {
            // Visita simmetrica
            // Visito il sottoalbero sinistro, se esiste
            if (this.left != null)
                this.left.addLabelsInOrder(l);
            // Adesso visito il nodo corrente
            l.add(this.label);
            // Visito il sottoalbero destro, se esiste
            if (this.right != null)
                this.right.addLabelsInOrder(l);
        }

        /*
         * Restituisce la lista ordinata delle etichette dei nodi di questo
         * (sotto-)albero secondo l'ordinamento naturale della classe {@code E}.
         * Per ottenere il risultato fa una visita in-order.
         * 
         * @return la lista ordinata delle etichette dei nodi di questo
         * (sotto-)albero secondo l'ordinamento naturale della classe {@code E}
         */
        protected List<E> inOrderVisit() {
            List<E> l = null;
            // Visito il sottoalbero sinistro, se esiste
            if (this.left != null)
                // la lista ls non è mai vuota perché l'albero non null contiene
                // sempre
                // almeno un elemento
                l = this.left.inOrderVisit();
            else // creo io la lista vuota
                l = new ArrayList<E>();
            // aggiungo alla lista ordinata dei valori a sinistra me stesso
            l.add(this.label);
            // aggiungo tutti gli elementi della lista ordinata dei valori a
            // destra
            if (this.right != null)
                l.addAll(this.right.inOrderVisit());
            return l;
        }

        /*
         * Cerca un nodo con una certa etichetta in questo albero.
         * 
         * @param label l'etichetta da cercare
         * 
         * @return il puntatore al nodo che contiene l'etichetta cercata, oppure
         * null se l'etichetta non è presente
         */
        protected RecBST search(E label) {
            // caso base
            if (this.left == null && this.right == null)
                // sono radice foglia
                if (this.label.equals(label))
                    return this;
                else
                    return null;
            // caso ricorsivo
            int cmp = this.label.compareTo(label);
            if (cmp == 0)
                return this;
            else if (cmp > 0) {
                if (this.left == null)
                    return null;
                else
                    return this.left.search(label);
            } else // cmp < 0
            if (this.right == null)
                return null;
            else
                return this.right.search(label);
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta più piccola
         * presente in questo (sotto-)albero.
         * 
         * @return il nodo più a sinistra che non ha un sotto-albero sinistro in
         * questo (sotto-)albero
         */
        protected RecBST getMinNode() {
            if (this.left == null)
                // Sono il nodo più a sinistra che non ha il figlio sinistro
                return this;
            else
                // Mi richiamo sul sottoalbero sinistro
                return this.left.getMinNode();
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta più grande
         * presente in questo (sotto-)albero.
         * 
         * @return il nodo più a destra che non ha un sottoalbero destro in
         * questo (sotto-)albero
         */
        protected RecBST getMaxNode() {
            if (this.right == null)
                // Sono il nodo più a destra che non ha il figlio destro
                return this;
            else
                // Mi richiamo sul sottoalbero destro
                return this.right.getMaxNode();
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta successiva
         * all'etichetta di questo nodo secondo l'ordine canonico della classe
         * E.
         * 
         * @return il puntatore al nodo successore oppure null se questo nodo
         * non ha successore
         */
        protected RecBST getSuccessorNode() {
            // Caso 1 - questo nodo ha un figlio destro
            if (this.right != null)
                return this.right.getMinNode();
            // Caso 2 - questo nodo non ha un figlio destro
            Set<E> ancestors = new HashSet<E>();
            // Questo nodo è considerato antenato di se stesso
            ancestors.add(this.label);
            // Cerco il primo nodo che ha un figlio sinistro che è tra gli
            // antenati
            RecBST p = this;
            while (p.parent != null) {
                if (p.parent.left != null
                        && ancestors.contains(p.parent.left.label))
                    // p.parent è il successore
                    return p.parent;
                // altrimenti aggiungi p.parent agli antenati e continua a
                // salire
                ancestors.add(p.parent.label);
                p = p.parent;
            }
            // non ho trovato il successore
            return null;
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta precedente
         * all'etichetta di questo nodo secondo l'ordine canonico della classe
         * E.
         * 
         * @return il puntatore al nodo predecessore oppure null se questo nodo
         * non ha predecessore
         */
        protected RecBST getPredecessorNode() {
            // Caso 1 - questo nodo ha un figlio sinistro
            if (this.left != null)
                return this.left.getMaxNode();
            // Caso 2 - questo nodo non ha un figlio sinistro
            Set<E> ancestors = new HashSet<E>();
            // Questo nodo è considerato antenato di se stesso
            ancestors.add(this.label);
            // Cerco il primo nodo che ha un figlio destro che è tra gli
            // antenati
            RecBST p = this;
            while (p.parent != null) {
                if (p.parent.right != null
                        && ancestors.contains(p.parent.right.label))
                    // p.parent è il predecessore
                    return p.parent;
                // altrimenti aggiungi p.parent agli antenati e continua a
                // salire
                ancestors.add(p.parent.label);
                p = p.parent;
            }
            // non ho trovato il predecessore
            return null;
        }

        /*
         * Aggiunge un nodo a questo (sotto-)albero con una etichetta
         * specificata.
         * 
         * @param label etichetta da inserire
         * 
         * @return true se il nodo è stato effettivamente inserito, false se
         * l'etichetta era già presente.
         */
        protected boolean insert(E label) {
            // Un nuovo nodo inserito non presente è sempre
            // inserito come foglia
            // Confronto l'elemento con la radice
            int x = this.label.compareTo(label);
            // Caso di uguaglianza
            if (x == 0)
                // L'elemento è già presente in questo nodo
                return false;
            // Caso non di uguaglianza
            if (x < 0)
                // L'elemento da inserire va nel sottoalbero destro
                if (this.right == null) {
                    // Inseriamo l'elemento come sottoalbero destro
                    this.right = new RecBST(label);
                    this.right.setParent(this);
                    return true;
                } else
                    return this.right.insert(label);
            else
            // L'elemento da inserire va nel sottoalbero sinistro
            if (this.left == null) {
                // Inseriamo l'elemento come sottoalbero sinistro
                this.left = new RecBST(label);
                this.left.setParent(this);
                return true;
            } else
                return this.left.insert(label);
        }
    }

}
