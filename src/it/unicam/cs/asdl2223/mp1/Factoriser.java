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
        CrivelloDiEratostene crivello = new CrivelloDiEratostene(n);
        for (int i = 2; crivello.hasNextPrime(); i++) {
            while() {

            }
        }

        return null;
    }
}
