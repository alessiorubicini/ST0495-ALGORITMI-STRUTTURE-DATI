package it.unicam.cs.asdl2223.es2;

import java.util.Random;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    private CombinationLock combinationLock;
    private int attempts;                       // Numero di tentativi

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        if(aCombinationLock == null) {
            throw new NullPointerException();
        }
        this.combinationLock = aCombinationLock;
        this.attempts = 0;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        String combination = "";
        Random r = new Random();

        while(!combinationLock.isOpen()) {
            combination = "";
            for (int i = 0; i < 3; i++) {
                char c = (char)(r.nextInt(25) + 'A');
                combination += c;
                combinationLock.setPosition(c);
            }
            combinationLock.open();
            this.attempts++;
        }

        return combination;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        if(!this.combinationLock.isOpen()) {
            return -1;
        } else {
            return this.attempts;
        }
    }
}
