/**
 * 
 */
package it.unicam.cs.asdl2223.es3;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;
    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        if(start == null || stop == null) throw new NullPointerException();
        if(start.compareTo(stop) >= 0) throw new IllegalArgumentException();
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof TimeSlot)) return false;
        TimeSlot other = (TimeSlot) obj;
        if(this.start.compareTo(other.start) == 0 && this.stop.compareTo(other.stop) == 0) {
            return true;
        }
        return false;
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + start.hashCode();
        hash = 89 * hash + stop.hashCode();
        return hash;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        // Se iniziano nello stesso momento, controlla se finiscono anche nello stesso momento
        if(this.start.compareTo(o.start) != 0) {
            return(this.stop.compareTo(o.stop));
        } else {
            return(this.start.compareTo(o.start));
        }
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        if(o == null) throw new NullPointerException();
        if(this.overlapsWith(o)) {
            // Ottiene gli orari di inizio e fine dei timeslot in millisecondi
            long start = this.start.getTimeInMillis();
            long stop = this.stop.getTimeInMillis();
            long oStart = o.getStart().getTimeInMillis();
            long oStop = o.getStop().getTimeInMillis();

            int result = 0;

            // Controlla se c'Ë overlap oppure se i due timeslot iniziano in sequenza
            if(!this.overlapsWith(o) || stop == oStart || oStop == start) {
                return -1;
            } else {
                // Primo caso: il secondo timeslot Ë contenuto nel primo
                if (start <= oStart && stop >= oStop) result = (int) ((oStop - oStart)/1000/60);
                // Secondo caso: il primo timeslot Ë contenuto nel secondo
                else if (start >= oStart && stop <= oStop) result = (int) ((stop - start)/1000/60);
                // Terzo caso: il primo timeslot inizia e finisce dopo il secondo
                else if (start >= oStart && stop >= oStop) result = (int) ((oStop - start)/1000/60);
                // Quarto caso: il primo timeslot inizia e finisce prima del secondo
                else if(start <= oStart && stop <= oStop) result = (int) ((stop - oStart)/1000/60);
            }

            if(result > Integer.MAX_VALUE) throw new IllegalArgumentException();
            else return result;
        } else {
            return -1;
        }
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        if(o == null) throw new NullPointerException();
        if(this.isSameDate(o)) {
            long start = this.start.getTimeInMillis();
            long stop = this.stop.getTimeInMillis();

            long oStart = o.start.getTimeInMillis();
            long oStop = o.stop.getTimeInMillis();

            if((start <= oStart) && (stop >= oStop) && ((oStop-oStart)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;
            else if((start >= oStart) && (stop <= oStop) && ((stop-start)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;
            else if((start >= oStart) && (stop >= oStop) && ((oStop-start)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;
            else if((start <= oStart) && (stop <= oStop) && ((stop-oStart)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;
            else return false;
        }
        return false;
    }

    /**
     * Determina se la data (intesa come anno, mese e giorno) coincide
     * con quella di un altro time slot dato
     * @param o
     *              il time slot che viene passato per il controllo
     * @return true se la data coincide
     */
    private boolean isSameDate(TimeSlot o) {
        if(this.start.get(Calendar.YEAR) == o.start.get(Calendar.YEAR)) {
            if(this.start.get(Calendar.MONTH) == o.start.get(Calendar.MONTH)) {
                if(this.start.get(Calendar.DAY_OF_MONTH) == o.start.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        String result = "[";
        result += start.get(GregorianCalendar.DAY_OF_MONTH) + "/" + (start.get(GregorianCalendar.MONTH)+1) + "/" +
                start.get(GregorianCalendar.YEAR) + " " + start.get(GregorianCalendar.HOUR_OF_DAY) + "." +
                start.get(GregorianCalendar.MINUTE);
        result += " - " + stop.get(GregorianCalendar.DAY_OF_MONTH) + "/" +  (stop.get(GregorianCalendar.MONTH)+1) + "/" +
                stop.get(GregorianCalendar.YEAR) + " " + stop.get(GregorianCalendar.HOUR_OF_DAY) + "." +
                stop.get(GregorianCalendar.MINUTE) + "]";
        return result;
    }
}
