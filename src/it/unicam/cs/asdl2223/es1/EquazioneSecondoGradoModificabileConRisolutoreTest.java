/**
 * 
 */
package it.unicam.cs.asdl2223.es1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        e1.setA(15);
        assertTrue(e1.getA() == 15 && !e1.isSolved());
        assertThrows(IllegalArgumentException.class, () -> new EquazioneSecondoGradoModificabileConRisolutore(
                0, 1, 1));
    }

    @Test
    final void testGetB() {
        double b = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, b, 1);
        assertTrue(b == e1.getB());
    }

    @Test
    final void testSetB() {
        double b = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, b, 1);
        e1.setB(15);
        assertTrue(e1.getB() == 15 && !e1.isSolved());
    }

    @Test
    final void testGetC() {
        double c = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, c);
        assertTrue(c == e1.getC());
    }

    @Test
    final void testSetC() {
        double c = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, c);
        e1.setC(15);
        assertTrue(e1.getC() == 15 && !e1.isSolved());
    }

    @Test
    final void testIsSolved() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        e1.solve();
        assertTrue(e1.isSolved());
        e1.setA(5);
        assertFalse(!e1.isSolved());
    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {
        EquazioneSecondoGradoModificabileConRisolutore eq;
        eq = new EquazioneSecondoGradoModificabileConRisolutore(3, 2, 6);
        eq.solve();
        assertTrue(eq.getSolution().isEmptySolution());

        eq = new EquazioneSecondoGradoModificabileConRisolutore(1, 2, 1);
        eq.solve();
        System.out.println(eq.getSolution());
        assertTrue(eq.getSolution().isOneSolution());
        assertTrue(eq.getSolution().getS1() == -1);

        eq = new EquazioneSecondoGradoModificabileConRisolutore(1, 4, 3);
        eq.solve();
        assertTrue(eq.getSolution().isEmptySolution() == false);
        assertTrue(eq.getSolution().isOneSolution() == false);
        assertTrue(eq.getSolution().getS1() == -1);
        assertTrue(eq.getSolution().getS2() == -3);
    }
}
