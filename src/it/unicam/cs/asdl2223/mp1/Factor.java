package it.unicam.cs.asdl2223.mp1;

/**
 * Un oggetto di quest classe rappresenta un fattore primo di un numero naturale
 * con una certa molteplicità.
 * 
 * @author Luca Tesei (template) // Alessio Rubicini
 *         alessio.rubicini@studenti.unicam.it
 *
 */
public class Factor implements Comparable<Factor> {

    /*
     * Numero primo corrispondente a questo fattore
     */
    private final int primeValue;

    /*
     * Molteplicità del numero primo di questo fattore, deve essere maggiore o
     * uguale a 1.
     */
    private final int multiplicity;

    /**
     * Crea un fattore primo di un numero naturale, formato da un numero primo e
     * dalla sua molteplicità.
     * 
     * @param primeValue,
     *                          numero primo
     * @param multiplicity,
     *                          valore della molteplicità, deve essere almeno 1
     * @throws IllegalArgumentException
     *                                      se la molteplicità è minore di 1
     *                                      oppure se primeValue è minore o
     *                                      uguale di 0.
     */
    public Factor(int primeValue, int multiplicity) {
        if(multiplicity < 1 || primeValue <= 0) {
            throw new IllegalArgumentException();
        }
        this.primeValue = primeValue;
        this.multiplicity = multiplicity;
    }

    /**
     * @return the primeValue
     */
    public int getPrimeValue() {
        return primeValue;
    }

    /**
     * @return the multiplicity
     */
    public int getMultiplicity() {
        return multiplicity;
    }

    /*
     * Calcola l'hashcode dell'oggetto in accordo ai valori usati per definire
     * il metodo equals.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.primeValue;
        hash = 89 * hash + this.multiplicity;
        return hash;
    }

    /*
     * Due oggetti Factor sono uguali se e solo se hanno lo stesso numero primo
     * e la stessa molteplicità
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof Factor)) return false;
        Factor other = (Factor) obj;
        if(this.primeValue == other.getPrimeValue() && this.multiplicity == other.getMultiplicity()) {
            return true;
        }
        return false;
    }

    /*
     * Un Factor è minore di un altro se contiene il numero primo minore. Se due
     * Factor hanno lo stesso numero primo allora il più piccolo dei due è
     * quello ce ha minore molteplicità.
     */
    @Override
    public int compareTo(Factor o) {
        if(this.primeValue > o.getPrimeValue()) {
            return 1;
        } else if(this.primeValue < o.getPrimeValue()) {
            return -1;
        } else {
            if(this.multiplicity > o.getMultiplicity()) {
                return 1;
            } else if (this.multiplicity < o.getMultiplicity()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /*
     * Il fattore viene reso con la stringa primeValue^multiplicity
     */
    @Override
    public String toString() {
        return primeValue + "^" + multiplicity;
    }
}
