package it.unicam.cs.asdl2223.mp1;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 * 
 * @author Luca Tesei (template) // Alessio Rubicini
 *         alessio.rubicini@studenti.unicam.it
 *
 */
public class Factoriser {

    /*
     * La quantità effettiva di fattori del numero fattorizzato.
     * Variabile necessaria per restituire il corretto output
     * dal momento che il metodo getFactors non sa a priori
     * il numero di fattori del numero dato in input
     */
    private int effectiveNumberOfFactors = 0;

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
        Factor[] factors = new Factor[(int)Math.sqrt(n)];
        CrivelloDiEratostene crivello = new CrivelloDiEratostene(n+1 );
        int i = 0;
        int result = n;
        for (int primeValue = 2; crivello.hasNextPrime() && result != 1; primeValue++) {
            int multiplicity = 0;
            while(result % primeValue == 0) {
                result = result/primeValue;
                multiplicity++;
            }
            if(multiplicity > 0) {
                factors[i] = new Factor(primeValue, multiplicity);
                this.effectiveNumberOfFactors++;
                i++;
            }
            crivello.nextPrime();
        }
        return buildFactorsArrayOfCorrectLength(factors);
    }

    /**
     * Costruisce l'array di fattori della giusta lunghezza
     * da restituire come output del metodo getFactors
     * @param factors
     *                  l'array di fattori della lunghezza errata
     * @return l'array di fattori della giusta lungehzza
     */
    private Factor[] buildFactorsArrayOfCorrectLength(Factor[] factors) {
        Factor[] result = new Factor[this.effectiveNumberOfFactors];
        for (int i = 0; i < result.length; i++) {
            if(factors[i] != null) result[i] = factors[i];
        }
        this.effectiveNumberOfFactors = 0;
        return result;
    }
}
