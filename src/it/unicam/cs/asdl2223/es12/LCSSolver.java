package it.unicam.cs.asdl2223.es12;

/**
 * Un oggetto di questa classe è un risolutore del problema della più lunga
 * sottosequenza comune tra due stringhe date.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class LCSSolver {

    /* Prima stringa */
    private final String x;

    /* Seconda stringa */
    private final String y;

    /* Matrice per il calcolo di una soluzione ottima */
    private int[][] m;

    /*
     * Flag che indica che questo solver ha svolto il proprio calcolo. Alla
     * creazione del solver non viene svolto il calcolo. Esso viene eseguito
     * successivamente alla chiamata del metodo solve()
     */
    private boolean isSolved;

    /**
     * Costruisce un risolutore LCS fra due stringhe date.
     * 
     * @param x
     *              la prima stringa
     * @param y
     *              la seconda stringa
     * @throws NullPointerException
     *                                  se almeno una delle due stringhe passate
     *                                  è nulla
     */
    public LCSSolver(String x, String y) {
        if (x == null || y == null)
            throw new NullPointerException(
                    "Creazione di un solver con una o due stringhe null");
        this.x = x;
        this.y = y;
        // creo la matrice
        this.m = new int[this.x.length() + 1][this.y.length() + 1];
        this.isSolved = false;
    }

    /**
     * @return the string x
     */
    public String getX() {
        return x;
    }

    /**
     * @return the string y
     */
    public String getY() {
        return y;
    }

    /**
     * Risolve il problema LCS delle due stringhe di questo solver, se non è
     * stato già risolto precedentemente. Dopo l'esecuzione di questo metodo la
     * prima volta il problema verrà considerato risolto.
     */
    public void solve() {
        // Per ogni carattere nella stringa x
        for (int i = 1; i <= x.length(); i++) {
            // Per ogni carattere nella stringa y
            for (int j = 1; j <= y.length(); j++) {
                // Se i caratteri corrispondono, allora la lunghezza della più lunga sottosequenza comune fino a quel punto
                // è uguale alla lunghezza della più lunga sottosequenza comune fino ai caratteri precedenti + 1
                if (x.charAt(i-1) == y.charAt(j-1)) {
                    m[i][j] = m[i-1][j-1] + 1;
                } else {
                    /* Altrimenti, la lunghezza della più lunga sottosequenza comune è uguale alla massima
                       tra la lunghezza della più lunga sottosequenza comune fino al carattere i nella stringa x
                       e la lunghezza della più lunga sottosequenza comune fino al carattere j nella stringa y
                    */
                    m[i][j] = Math.max(m[i-1][j], m[i][j-1]);
                }
            }
        }
        // Impostiamo il flag isSolved a true per indicare che il problema è stato risolto
        isSolved = true;
    }

    /**
     * Determina se questo solver ha già risolto il problema.
     * 
     * @return true se il problema LCS di questo solver è già stato risolto
     *         precedentemente, false altrimenti
     */
    public boolean isSolved() {
        return this.isSolved;
    }

    /**
     * Determina la lunghezza massima delle sottosequenze comuni.
     * 
     * @return la massima lunghezza delle sottosequenze comuni di x e y.
     * @throws IllegalStateException
     *                                   se il solver non ha ancora risolto il
     *                                   problema LCS
     */
    public int getLengthOfSolution() {
        if (!this.isSolved)
            throw new IllegalStateException(
                    "Richiesta delle soluzioni prima della risoluzione del problema");
        return this.m[this.x.length()][this.y.length()];
    }

    /**
     * Restituisce una soluzione del problema LCS.
     * 
     * @return una sottosequenza di this.x e this.y di lunghezza massima
     * @throws IllegalStateException
     *                                   se il solver non ha ancora risolto il
     *                                   problema LCS
     */
    public String getOneSolution() {
        if (!this.isSolved)
            throw new IllegalStateException(
                    "Richiesta delle soluzioni prima della risoluzione del problema");
        return traceBack(this.x.length(), this.y.length());
    }

    /*
     * NOTA: Determina una soluzione ottime ripercorrendo le caselle della
     * matrice m seguendo le condizioni con cui sono state costruite. In questo
     * caso non deve venire usata una matrice di "supporto" per ricostruire una
     * soluzione ottima, ma va usata la stessa matrice m
     */
    private String traceBack(int i, int j) {
        // Se i o j sono uguali a 0, significa che siamo arrivati ai bordi della matrice e non ci sono
        // altri caratteri da considerare nella sottosequenza comune. In questo caso, ritorniamo una stringa vuota.
        if (i == 0 || j == 0) {
            return "";
        }

        // Se il carattere nella stringa x alla posizione i-1 corrisponde al carattere nella stringa y
        // alla posizione j-1, allora questo carattere fa parte della sottosequenza comune e lo
        // aggiungiamo alla stringa risultato.
        // Quindi, continuiamo il ripercorrimento della matrice verso la posizione (i-1, j-1)
        if (x.charAt(i-1) == y.charAt(j-1)) {
            return traceBack(i-1, j-1) + x.charAt(i-1);
        } else {
            // Altrimenti, se la cella (i-1, j) contiene un valore maggiore della cella (i, j-1), significa
            // che la sottosequenza comune non contiene il carattere nella stringa y alla posizione j-1.
            // Quindi, continuiamo il ripercorrimento della matrice verso la posizione (i-1, j)
            if (m[i-1][j] > m[i][j-1]) {
                return traceBack(i-1, j);
            } else {
                // Se nessuna delle condizioni sopra è vera, significa che la sottosequenza comune non contiene
                // il carattere nella stringa x alla posizione i-1.
                // Quindi, continuiamo il ripercorrimento della matrice verso la posizione (i, j-1)
                return traceBack(i, j-1);
            }
        }
    }


    /**
     * Determina se una certa stringa è una sottosequenza comune delle due
     * stringhe di questo solver.
     * 
     * @param z
     *              la string da controllare
     * @return true se z è sottosequenza di this.x e di this.y, false altrimenti
     * @throws NullPointerException
     *                                  se z è null
     */
    public boolean isCommonSubsequence(String z) {
        if (z == null)
            throw new NullPointerException("Test di una sequenza nulla");
        return isSubsequence(z, this.x) && isSubsequence(z, this.y);
    }

    /*
     * Determina se una stringa è sottosequenza di un'altra stringa.
     * 
     * @param z la stringa da testare
     * 
     * @param w la stringa di cui z dovrebbe essere sottosequenza
     * 
     * @return true se z è sottosequenza di w, false altrimenti
     */
    private static boolean isSubsequence(String z, String w) {
        // Se la lunghezza della stringa z è uguale a 0, significa che abbiamo esaurito tutti i caratteri
        // da considerare nella stringa z. In questo caso, la stringa z è una sottosequenza di w.
        if (z.length() == 0) {
            return true;
        }

        // Se la lunghezza della stringa w è uguale a 0, significa che abbiamo esaurito tutti i caratteri
        // da considerare nella stringa w. In questo caso, la stringa z non è una sottosequenza di w.
        if (w.length() == 0) {
            return false;
        }

        // Se il primo carattere della stringa z corrisponde al primo carattere della stringa w,
        // allora verifichiamo se la sottostringa z che inizia dal secondo carattere è una
        // sottosequenza della sottostringa w che inizia dal secondo carattere
        if (z.charAt(0) == w.charAt(0)) {
            return isSubsequence(z.substring(1), w.substring(1));
        }

        // Altrimenti, verifichiamo se la stringa z è una sottosequenza della sottostringa w che
        // inizia dal secondo carattere, ignorando il primo carattere di w
        return isSubsequence(z, w.substring(1));
    }

}
