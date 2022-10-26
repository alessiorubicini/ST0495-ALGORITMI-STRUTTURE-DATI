package it.unicam.cs.asdl2223.mp1;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 * 
 * @author Luca Tesei (template) // Alessio Rubicini alessio.rubicini@studenti.unicam.it
 *
 */
public class Factoriser {

    /*
     * La quantità effettiva di fattori del numero fattorizzato.
     * Variabile necessaria per restituire il corretto output
     * dal momento che il metodo getFactors non sa a priori
     * il numero di fattori del numero dato in input
     */
    private int numberOfFactors = 0;

    /**
     * Fattorizza un numero restituendo la sequenza crescente dei suoi fattori
     * primi. La molteplicità di ogni fattore primo esprime quante volte il
     * fattore stesso divide il numero fattorizzato. Per convenzione non viene
     * mai restituito il fattore 1. Il minimo numero fattorizzabile è 1. In
     * questo caso viene restituito un array vuoto.
     * 
     * @param n
     *              un numero intero da fattorizzare
     * @return un array contenente i fattori primi di n
     * @throws IllegalArgumentException
     *                                      se si chiede di fattorizzare un
     *                                      numero minore di 1.
     */
    public Factor[] getFactors(int n) {
        if(n < 1) throw new IllegalArgumentException("Numero minore di 1");
        // Crea un array di fattori temporaneo di lunghezza uguale alla radice di n (la massima necessaria)
        Factor[] factors = new Factor[(int)Math.sqrt(n)];
        CrivelloDiEratostene crivello = new CrivelloDiEratostene(n+1 );
        // Fattorizza il numero, inserendo i fattori nell'array creato
        this.findFactors(n, factors, crivello);
        // Restituisce l'array di fattori della giusta lunghezza
        return buildFactorsArrayOfCorrectLength(factors);
    }

    /**
     * Fattorizza un intero
     * @param n             numero intero
     * @param factors       array in cui inserire i fattori
     * @param crivello      crivello di Eratostene da utilizzare
     */
    private void findFactors(int n, Factor[] factors, CrivelloDiEratostene crivello) {
        int primeValue, multiplicity = 0;
        while(crivello.hasNextPrime() && n != 1) {
            // Ottiene l'ultimo numero primo elencato e imposta la sua molteplicità a 0
            primeValue = crivello.getLastListedPrime();
            multiplicity = 0;
            // Divide il risultato per il numero primo finchè è divisibile e incrementa la molteplicità
            while(n % primeValue == 0) {
                n = n / primeValue;
                multiplicity++;
            }
            // Se c'è molteplicità, crea e aggiunge il fattore
            if(multiplicity > 0) {
                factors[numberOfFactors] = new Factor(primeValue, multiplicity);
                this.numberOfFactors++;
            }
            // Passa al prossimo numero primo
            crivello.nextPrime();
        }
    }

    /**
     * Costruisce l'array di fattori della giusta lunghezza
     * da restituire come output del metodo getFactors
     * @param factors
     *                  l'array di fattori della lunghezza errata
     * @return l'array di fattori della giusta lungehzza
     */
    private Factor[] buildFactorsArrayOfCorrectLength(Factor[] factors) {
        Factor[] result = new Factor[this.numberOfFactors];
        for (int i = 0; i < this.numberOfFactors; i++) result[i] = factors[i];
        this.numberOfFactors = 0;
        return result;
    }
}
