package it.unicam.cs.asdl2223.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

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
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        if(nome == null || location == null || facilities == null) {
            throw new NullPointerException();
        }
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
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
        if(this.nome.equals(other.getNome())) return true;
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
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null) throw new NullPointerException("Facility f nulla");
        if(!this.facilities.contains(f)) {
            this.facilities.add(f);
            return true;
        }
        return false;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
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
        if(ts == null) throw new NullPointerException();
        for (Prenotazione p: this.prenotazioni) {
            // Se è arrivato a un time slot seguente, interrompe la ricerca
            if(p.getTimeSlot().compareTo(ts) > 0) return true;
            if(ts.overlapsWith(p.getTimeSlot())) return false;
        }
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        for (Facility f: requestedFacilities) {
            if(!this.facilities.contains(f)) {
                return false;
            }
        }
        return true;
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
            throw new NullPointerException();
        }
        // Controlla se la prenotazione comporta una sovrapposizione con un'altra prenotazione
        if(overlapsWithSomeOther(ts)) throw new IllegalArgumentException();
        // Crea la prenotazione
        Prenotazione p = new Prenotazione(this, ts, docente, motivo);
        // Aggiunge la prenotazione
        this.prenotazioni.add(p);
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        if(p == null) throw new NullPointerException();
        // Controlla se la prenotazione è presente
        if(this.prenotazioni.contains(p)) {
            this.prenotazioni.remove(p);
            return true;
        }
        return false;
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        if(timePoint == null) throw new NullPointerException();
        Set<Prenotazione> prenotazioniDaCancellare = new HashSet<Prenotazione>();
        for (Prenotazione p: this.prenotazioni) {
            if(p.getTimeSlot().getStart().compareTo(timePoint) > 0) {
                if(!prenotazioniDaCancellare.isEmpty()) return true;
                else return false;
            } else {
                prenotazioniDaCancellare.add(p);
            }
        }
        if(!prenotazioniDaCancellare.isEmpty()) {
            this.prenotazioni.removeAll(prenotazioniDaCancellare);
            return true;
        }
        return false;
    }

    /**
     * Controlla se un time slot si sovrappone con una delle prenotazioni
     * @param ts
     *              il time slot da controllare
     * @return true se si sovrappone almeno con una prenotazione
     */
    private boolean overlapsWithSomeOther(TimeSlot ts) {
        for (Prenotazione p: this.prenotazioni) {
            if(ts.overlapsWith(p.getTimeSlot())) return true;
        }
        return false;
    }
}
