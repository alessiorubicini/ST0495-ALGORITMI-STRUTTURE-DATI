/**
 * 
 */
package it.unicam.cs.asdl2223.es4;
import it.unicam.cs.asdl2223.es3.TimeSlot;
import it.unicam.cs.asdl2223.es3.Prenotazione;

import java.util.Objects;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

    /**
     * Costruisce una presence facility.
     * 
     * @param codice
     * @param descrizione
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public PresenceFacility(String codice, String descrizione) {
        super(codice, descrizione);
        // TODO implementare
    }

    /*
     * Una Presence Facility soddisfa una facility solo se la facility passata è
     * una Presence Facility ed ha lo stesso codice.
     * 
     */
    @Override
    public boolean satisfies(Facility o) {
        if(o == null) throw new NullPointerException();
        // Controlla che la facility sia un'istanza di PresenceFacility
        if(o instanceof PresenceFacility) {
            // Effettua il casting e confronta i codici delle PresenceFacility
            PresenceFacility pf = (PresenceFacility) o;
            if (this.getCodice().equals(pf.getCodice())) return true;
        }
        return false;
    }

}
