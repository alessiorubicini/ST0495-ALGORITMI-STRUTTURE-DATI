/**
 * 
 */
package it.unicam.cs.asdl2223.es10;

import it.unicam.cs.asdl2223.mp2.ASDL2223Deque;

import java.util.*;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 *
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 *
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 *
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     *
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     *
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     *
     * this.table[i] = new Node<E>(item, next);
     *
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     *
     * Node<E> myNode = (Node<E>) this.table[i];
     *
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // Controlla se l'oggetto dato è null
        if(o == null) throw new NullPointerException();
        // Calcola il bucket in cui cercare l'oggetto dato
        int bucket = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        // Ottiene il nodo del bucket ottenuto
        Node<E> n = (Node<E>) this.table[bucket];
        // Se il nodo contiene null, ritorna false
        if(n == null) {
            return false;
        } else {
            // Altrimenti scorre la lista a cui punta il nodo
            while(n != null) {
                // Se trova l'oggetto dato, ritorna true
                if(n.item.equals(o)) return true;
                n = n.next;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        // Controlla se l'elemento dato è null
        if(e == null) throw new NullPointerException();
        // Controlla se la tabella contiene già l'elemento
        if(this.contains(e)) return false;
        // Crea nuovo nodo con l'elemento dato
        Node<E> newNode = new Node<E>(e, null);
        // Ottiene il bucket dell'elemento dato
        int bucket = this.phf.hash(e.hashCode(), this.getCurrentCapacity());
        // Ottiene il nodo del bucket ottenuto
        Node<E> n = (Node<E>) this.table[bucket];
        // Se il nodo è null, ci inserisce il nuovo nodo creando la lista
        if(n == null) {
            this.table[bucket] = newNode;
        } else {
            // Altrimenti scorre la lista a cui punta il bucket
            while(n != null) {
                n = n.next;
            }
            // Imposta ultimo nodo al nuovo nodo
            n = newNode;
        }
        // Incrementa dimensione tabella e Contatore modifiche
        this.size++;
        this.modCount++;
        // Se ha superato la capacità della tabella, effettua il resize
        if(this.size > this.getCurrentThreshold()) {
            this.resize();
        }
        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // Crea nuova tabella grande il doppio della corrente
        Object[] doubleTable = new Object[this.getCurrentCapacity() * 2];
        int originalBucket, newBucket;
        // Scorre la tabella utilizzando l'iterator
        for(Iterator i = this.iterator(); i.hasNext(); ) {
            E element = (E) i.next();
            // Ottiene bucket dell'elemento corrente
            originalBucket = this.phf.hash(element.hashCode(), table.length);
            newBucket = this.phf.hash(element.hashCode(), doubleTable.length);
            // Ottiene il nodo del bucket ottenuto
            Node<E> n = (Node<E>) this.table[originalBucket];
            Node<E> newNode = new Node<E>(element, null);
            // Se il nodo è null, ci inserisce il nuovo nodo creando la lista
            if(n == null) {
                doubleTable[originalBucket] = newNode;
            } else {
                // Altrimenti scorre la lista a cui punta il bucket
                while(n != null) {
                    n = n.next;
                }
                // Imposta ultimo nodo al nuovo nodo
                n = newNode;
            }
            // Inserisce nuovo nodo nella corretta posizione
            //n = newNode;
        }
        // Sostituisce la tabella corrente con la nuova
        System.out.println("EFFETTUATO RESIZE");
        this.table = doubleTable;
    }

    @Override
    public boolean remove(Object o) {
        // Controlla se l'elemento dato è nullo
        if(o == null) throw new NullPointerException();
        // Controlla se la tabella non contiene l'elemento
        if(!this.contains(o)) return false;
        // Ottiene il bucket dell'elemento dato
        int bucket = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        // Ottiene il nodo dalla posizione della tabella
        Node<E> n = (Node<E>) this.table[bucket];
        // Se il nodo è diverso da null, allora punta a una lista
        if(n != null) {
            // Scorre la lista cercando l'elemento dato
            while(n.next != null) {
                // Se l'elemento successivo è quello dato, lo elimina
                if(n.next.item.equals(o)) {
                    n.next = n.next.next;
                } else {
                    // Altrimenti, passa all'elemento successivo della lista
                    n = n.next;
                }
            }
        }
        // Decrementa dimensione tabella e Incrementa numero modifiche
        this.size--;
        this.modCount++;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Scorre gli oggetti della collezione, controllando se la tabella li contiene
        for(Object o : c) {
            if(!this.contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Scorre gli oggetti della collezione, aggiungendoli alla tabella
        for(E e : c) {
            this.add(e);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Controlla se la collezione contiene null
        if(c.contains(null)) throw new NullPointerException();
        // Scorre gli oggetti della collezione rimuovendoli dalla tabella
        for(Object o : c) {
            this.remove(o);
        }
        return true;
    }

    @Override
    public void clear() {
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;
        private int numeroModificheAtteso;

        private Itr() {
            this.lastReturned = null;
            this.numeroModificheAtteso = modCount;
        }

        @Override
        public boolean hasNext() {
            // Se l'ultimo elemento ritornato è null
            if (lastReturned == null) {
                // Ritorna true se la tabella non è vuota
                return size != 0;
            } else if(lastReturned.next == null) {
                // Altrimenti, se il successivo dell'ultimo ritornato è null
                // Allora siamo alla fine della lista di collisioni
                // Quindi ottiene bucket dell'ultimo nodo ritornato
                int currentBucket = phf.hash(this.lastReturned.hashCode(), getCurrentCapacity());
                // Scorre in avanti la tabella
                while(currentBucket < getCurrentCapacity()-1) {
                    currentBucket++;
                    // Se trova un bucket che punta a una lista ritorna true
                    if(table[currentBucket] != null) return true;
                }
                // Se non ha trovato altri bucket con liste, ritorna false
                return false;
            } else {
                return true;
            }
        }

        @Override
        public E next() {
            // Controlla se sono avvenute modifiche concorrenti durante l'iterazione
            if (this.numeroModificheAtteso != modCount) {
                throw new ConcurrentModificationException("Tabella modificata durante l'iterazione");
            }
            // Controlla se c'è un nodo successivo
            if (!this.hasNext()) throw new NoSuchElementException("Non c'è un nodo successivo");

            int currentBucket;
            // Se l'ultimo elemento ritornato è null
            if(lastReturned == null) {
                // Scorre la tabella dal primo bucket
                currentBucket = 0;
                while(currentBucket < getCurrentCapacity()-1) {
                    currentBucket++;
                    // Se il bucket corrente punta ad una lista (quindi non è null)
                    if(table[currentBucket] != null) {
                        // Imposta come ultimo elemento ritornato la testa di quella lista
                        lastReturned = (Node<E>) table[currentBucket];
                        break;
                    }
                }
            } else {
                // Se l'ultimo elemento ritornato non è null
                // Calcola il bucket dell'ultimo elemento ritornato
                currentBucket = phf.hash(lastReturned.hashCode(), getCurrentCapacity());
                // Se il nodo successivo è null, la lista di collisioni è finita
                if(lastReturned.next == null) {
                    // Scorre la tabella in avanti
                    while(currentBucket < getCurrentCapacity()-1) {
                        currentBucket++;
                        // Se il nuovo bucket punta ad una lista (quindi non è null)
                        if(table[currentBucket] != null) {
                            // Imposta come ultimo elemento ritornato la testa di quella lista
                            lastReturned = (Node<E>) table[currentBucket];
                            break;
                        }
                    }
                } else {
                    // Altrimenti, scorre al prossimo nodo della lista di collisioni
                    lastReturned = lastReturned.next;
                }
            }
            // Ritorna l'item
            return lastReturned.item;
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}