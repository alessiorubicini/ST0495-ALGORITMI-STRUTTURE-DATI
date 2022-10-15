package it.unicam.cs.asdl2223.es4;
import it.unicam.cs.asdl2223.es3.TimeSlot;
import it.unicam.cs.asdl2223.es3.Prenotazione;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

    /*
     * numero iniziale delle posizioni dell'array facilities. Se viene richiesto
     * di inserire una facility e l'array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai test JUnit
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * numero iniziale delle posizioni dell'array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l'array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * test JUnit.
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L'array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l'array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L'array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l'array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        if(nome == null || location == null) {
            throw new NullPointerException();
        }
        this.nome = nome;
        this.location = location;
        this.facilities = new Facility[INIT_NUM_FACILITIES];
        this.numFacilities = 0;
        this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];
        this.numPrenotazioni = 0;
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.nome.hashCode();
        return hash;
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof Aula)) return false;
        Aula other = (Aula) obj;
        if(this.nome.equals(other.getNome())) {
            return true;
        }
        return false;
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.getNome());
    }

    /**
     * @return the facilities
     */
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null) throw new NullPointerException();
        if(this.isAlreadyIn(f)) return false;
        facilities[numFacilities] = f;
        numFacilities++;
        return true;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        for (int i = 0; i < prenotazioni.length ; i++) {
            if(prenotazioni[i].getTimeSlot().overlapsWith(ts)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare, sono da considerare solo le
     *                                posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        // TODO implementare
        return false;
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        if(ts == null || docente == null || motivo == null) {
            throw new NullPointerException("Uno dei parametri è nullo");
        }
        if(this.overlapsWithSomeSlot(ts)) {
            throw new IllegalArgumentException("Il time slot si sovrappone con un altro");
        }
        Prenotazione p = new Prenotazione(this.nome, ts, docente, motivo);
        this.prenotazioni[numPrenotazioni] = p;
        this.numPrenotazioni++;
    }

    /**
     * Controlla se una facility è già presente nell'aula
     * @param f
     *              la facility da controllare
     * @return true se è già presente, false altrimenti
     */
    private boolean isAlreadyIn(Facility f) {
        for (int i = 0; i < numPrenotazioni; i++) {
            if(facilities[i].equals(f)) return true;
        }
        return false;
    }

    /**
     * Controlla se un time slot si sovrappone con una delle prenotazioni
     * @param ts
     *              il time slot da controllare
     * @return true se il time slot si sovrappone, false altrimenti
     */
    private boolean overlapsWithSomeSlot(TimeSlot ts) {
        for (int i = 0; i < numPrenotazioni; i++) {
            if(ts.overlapsWith(prenotazioni[i].getTimeSlot())) {
                return true;
            }
        }
        return false;
    }
}
