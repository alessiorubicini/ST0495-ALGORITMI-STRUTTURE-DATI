package it.unicam.cs.asdl2223.mp3;

import sun.java2d.xr.XRRenderer;

import java.util.*;

/**
 * 
 * Class that solves an instance of the the El Mamun's Caravan problem using
 * dynamic programming.
 * 
 * Template: Daniele Marchei and Luca Tesei, Implementation: Alessio Rubicini alessio.rubicini@studenti.unicam.it
 *
 */
public class ElMamunCaravanSolver {

    // the expression to analyse
    private final Expression expression;

    // table to collect the optimal solution for each sub-problem,
    // protected just for Junit Testing purposes
    protected Integer[][] table;

    // table to record the chosen optimal solution among the optimal solution of
    // the sub-problems, protected just for JUnit Testing purposes
    protected Integer[][] tracebackTable;

    // flag indicating that the problem has been solved at least once
    private boolean solved;

    /**
     * Create a solver for a specific expression.
     *
     * @param expression
     *                       The expression to work on
     * @throws NullPointerException
     *                                  if the expression is null
     */
    public ElMamunCaravanSolver(Expression expression) {
        if (expression == null)
            throw new NullPointerException(
                    "Creazione di solver con expression null");
        this.expression = expression;
        // Initialize the table and the traceback table
        this.table = new Integer[this.expression.size()][this.expression.size()];
        this.tracebackTable = new Integer[this.expression.size()][this.expression.size()];
        // Set resolution flag to false
        this.solved = false;
    }

    /**
     * Returns the expression that this solver analyse.
     *
     * @return the expression of this solver
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     * Solve the problem on the expression of this solver by using a given
     * objective function.
     * 
     * @param function
     *                     The objective function to be used when deciding which
     *                     candidate to choose
     * @throws NullPointerException
     *                                  if the objective function is null
     */
    public void solve(ObjectiveFunction function) {
        // Controlla se la funzione passata è nulla
        if(function == null) {
            throw new NullPointerException("The function is null.");
        }

        int result = 0;             // Risultato dell'operazione

        printTable();

        // Ciclo esterno che scorre la tabella da sinistra a destra
        for (int i = 0; i < expression.size(); i++) {
            // Ciclo interno che scorre la tabella dall'alto verso il basso
            for (int j = i; j < expression.size(); j++) {
                if (i == j && isDigit(i)) {
                    this.table[i][j] = (Integer) expression.get(i).getValue();
                } else if(i < j && isDigit(i) && isDigit(j)) {
                    // inizializzo il valore ottimo a -infinito o +infinito a seconda della funzione
                    int best = function instanceof MaximumFunction ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                    // Calcola il valore migliore tra i valori ottenuti utilizzando l'operatore
                    for (int k = 0; (i + k + 2 <= j); k += 2) {
                        System.out.println("\t\ttable["+i+"]["+j+"] = "+"table["+i+"]["+(i+k)+"] "+expression.get(i+k+1).getValue()+" table["+(i+k+2)+"]["+j+"] = "+table[i][i+k]+" "+expression.get(i+k+1).getValue() + " " + table[i+k+2][j]);
                        // Calcola l'operazione
                        if(expression.get(i+k+1).getValue().equals("+")) {
                            result = table[i][i+k] + table[i+k+2][j];
                        } else {
                            result = table[i][i+k] * table[i+k+2][j];
                        }
                        // Ottiene il valore migliore
                        best = function.getBest(Arrays.asList(best, result));
                        // Salva il valore ottimo nella tabella
                        this.table[i][j] = best;
                        // Memorizza la scelta di k per il valore migliore nella tabella di traceback
                        this.tracebackTable[i][j] = k;
                    }

                } else {
                    // I casi in cui e[i] ed e[j] non sono cifre non ci interessano
                    // e i relativi valori di m potranno essere considerati nulli senza produrre errori;
                    // lo stesso dicasi per i casi in cui j < i.
                }
            }
        }


        // Itera sulla matrice e sull'espressione
        /*for (int i = 0; i < this.expression.size(); i+=2) {
            for (int j = 2; j < this.expression.size(); j=j+2) {
                // Calcola il valore migliore facendo variare k di due in due finché i + k + 2 <= j
                k = 0;
                System.out.println("For j = " + j);

            }
        }*/

        this.solved = true;
        printTable();
        // TODO Implement
    }

    private boolean isDigit(int i) {
        return expression.get(i).getType() == ItemType.DIGIT;
    }

    /**
     * Returns the current optimal value for the expression of this solver. The
     * value corresponds to the one obtained after the last solving (which used
     * a particular objective function).
     * 
     * @return the current optimal value
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public int getOptimalSolution() {
        if (!this.solved) {
            throw new IllegalStateException("Problem has never been solved");
        }
        return this.table[0][this.expression.size() - 1];
    }

    /**
     * Returns an optimal parenthesization corresponding to an optimal solution
     * of the expression of this solver. The parenthesization corresponds to the
     * optimal value obtained after the last solving (which used a particular
     * objective function).
     * 
     * If the expression is just a digit then the parenthesization is the
     * expression itself. If the expression is not just a digit then the
     * parethesization is of the form "(<parenthesization>)". Examples: "1",
     * "(1+2)", "(1*(2+(3*4)))"
     * 
     * @return the current optimal parenthesization for the expression of this
     *         solver
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public String getOptimalParenthesization() {
        if (!solved) {
            throw new IllegalStateException("Problem not solved");
        }
        return traceback(0, expression.size() - 1);
    }

    /**
     * Determines if the problem has been solved at least once.
     * 
     * @return true if the problem has been solved at least once, false
     *         otherwise.
     */
    public boolean isSolved() {
        return this.solved;
    }

    @Override
    public String toString() {
        return "ElMamunCaravanSolver for " + expression;
    }

    // Recursive method to compute the optimal parenthesization of a sub-expression
    private String traceback(int i, int j) {
        // If the sub-expression is just a digit, return the digit as a string
        if (i == j) {
            return expression.get(i).toString();
        }
        // Otherwise, compute the index of the operator that separates the
        // sub-expression in two sub-expressions
        System.out.println("Cerco traceback di " + i + ", "+j);
        int k = tracebackTable[i][j];
        // Recursively compute the optimal parenthesization for the two
        // sub-expressions and return the result with the appropriate parenthesis
        return "(" + traceback(i, k) + expression.get(k) + traceback(k + 1, j) + ")";
    }

    public void printTable() {
        for (int i = 0; i < table.length; i++) {
            System.out.print("[");
            for (int j = 0; j < table.length; j++) {
                if(table[i][j] != null) {
                    System.out.print(table[i][j] + "   , ");
                } else {
                    System.out.print(table[i][j] + ", ");
                }
            }
            System.out.println("]");
        }
        System.out.println();
    }
}
