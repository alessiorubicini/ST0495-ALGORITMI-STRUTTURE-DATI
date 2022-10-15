package it.unicam.cs.asdl2223.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 *
 */
public class CombinationLock {

    private String combination;         // La combinazione segreta della cassaforte
    private char[] lastPositions;       // Le ultime tre lettere inserite nel tentativo corrente
    private int nextPosition;           // La prossima posizione per l'input
    private boolean opened;             // Indica se la cassaforte è aperta o meno

    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     * 
     * @param aCombination
     *                         la combinazione che deve essere una stringa di 3
     *                         lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public CombinationLock(String aCombination) {
        if(aCombination == null) {
            throw new NullPointerException();
        } else if(!this.isCombinationValid(aCombination)) {
            throw new IllegalArgumentException();
        } else {
            this.combination = aCombination;
            this.opened = true;
            this.nextPosition = 0;
            this.lastPositions = new char[]{'-', '-', '-'};
        }
    }

    /**
     * Imposta la manopola su una certa posizione.
     * 
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        if(!Character.isUpperCase(aPosition)) {         // Controlla se il carattere è una lettera maiuscolo
            throw new IllegalArgumentException();
        }
        this.lastPositions[nextPosition] = aPosition;   // Imposta il nuovo carattere
        if(nextPosition == 2) {                         // Reimposta la posizione per l'input
            this.nextPosition = 0;
        } else {
            this.nextPosition++;
        }
    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        // Se le tre lettere inserite combaciano con la combinazione segreta, apre la cassaforte
        if(lastPositions[0] == combination.charAt(0) &&
                lastPositions[1] == combination.charAt(1) &&
                lastPositions[2] == combination.charAt(2)) {
            this.opened = true;
        } else {
            this.resetLastCombination();
        }
    }

    /**
     * Determina se la cassaforte è aperta.
     * 
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        return this.opened;
    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {
        this.opened = false;
        this.resetLastCombination();
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     * 
     * @param aCombination
     *                         la nuova combinazione che deve essere una stringa
     *                         di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {
        if(aCombination == null) {
            throw new NullPointerException();
        } else if (!isCombinationValid(aCombination)) {
            throw new IllegalArgumentException();
        } else {
            this.combination = aCombination;
            this.opened = false;
            this.resetLastCombination();
        }
    }

    /**
     * Controlla se la combinazione è <b>valida</b>
     * cioè se è composta da 3 lettere maiuscole dell'alfabeto inglese
     * @param aCombination     il carattere da controllare
     * @return true se la combinazione è valida, false altrimenti
     */
    private boolean isCombinationValid(String aCombination) {
        if (aCombination.length() != 3 || !aCombination.equals(aCombination.toUpperCase())) {
            return false;
        }
        return true;
    }

    /**
     * <b>Reimposta</b> le lettere inserite precedentemente
     */
    private void resetLastCombination() {
        this.lastPositions = new char[]{'-', '-', '-'};
        this.nextPosition = 0;
    }
}