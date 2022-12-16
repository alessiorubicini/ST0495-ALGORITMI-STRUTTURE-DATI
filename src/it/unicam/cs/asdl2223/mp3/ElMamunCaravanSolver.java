package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Inserisco i valori dell'espressione nella diagonale della matrice
        for (int i = 0; i < expression.size(); i+=2) {
            this.table[i][i] = (Integer) this.expression.get(i).getValue();
        }

        ArrayList<Integer> values = new ArrayList<Integer>();       // Valori calcolati da best
        ArrayList<Integer> kChoices = new ArrayList<Integer>();     // k per i valori calcolati da best

        int bestValueIndex = 0;     // Indice del valore migliore
        int bestK = 0;              // k per il valore migliore
        int operand1, operand2;     // Operandi dell'operazione
        int result = 0;             // Risultato dell'operazione
        int k = 0;
        printTable();

        // Itera sulla matrice e sull'espressione
        for (int i = 0; i < this.expression.size(); i+=2) {
            for (int j = 2; j < this.expression.size(); j=j+2) {
                // Calcola il valore migliore facendo variare k di due in due finché i + k + 2 <= j
                k = 0;
                System.out.println("Loop j, j = " + j);
                // while(i + k + 2 <= j) {
                while(i + k + 2 <= j) {
                    System.out.println("Inizio while: k = " + k);
                    // Se i < j  e  expression[i] ed expression.[j] sono cifre
                    if(i < j && expression.get(i).getType() == ItemType.DIGIT && expression.get(j).getType() == ItemType.DIGIT) {

                        System.out.println(i + " + " + k + " + 2 <= " + j);
                        System.out.print("table["+i+"]["+(i+k)+"] " +  expression.get(i+k+1).getValue() + " table["+(i+k+2)+"]["+j+"] \t");
                        System.out.println(table[i][i+k] + " " +  expression.get(i+k+1).getValue() + " " + table[i+k+2][j]);

                        // Esegue la corretta operazione tra i due operandi
                        operand1 = table[i][i+k];
                        operand2 = table[i+k+2][j];
                        ExpressionItem operator = expression.get(i+k+1);
                        if(operator.getValue().equals("+")) {
                            result = operand1 + operand2;
                        } else {
                            result = operand1 * operand2;
                        }
                        // Aggiunge il risultato e il rispettivo k ai due array di supporto
                        values.add(result);
                        kChoices.add(k);
                        // Aggiunge il risultato alla tabellas
                        this.table[i][j] = result;
                    }
                    k+=2;
                    System.out.println("Fine while, k incrementata a " + k +", j = "+ j);
                }
                // Calcolo il valore migliore tra quelli calcolati utilizzando gli array di supporto
                bestValueIndex = function.getBestIndex(values);
                // Memorizza la scelta di k per il valore migliore nella tabella di traceback
                this.tracebackTable[i][j] = kChoices.get(bestValueIndex);
            }
        }

        this.solved = true;
        // TODO Implement
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
        int k = tracebackTable[i][j];
        // Recursively compute the optimal parenthesization for the two
        // sub-expressions and return the result with the appropriate parenthesis
        return "(" + traceback(i, k) + expression.get(k) + traceback(k + 1, j) + ")";
    }

    public void printTable() {
        System.out.print("   ");
        for (int i = 0; i <table.length ; i++) {
            System.out.print(i + "\t  ");
        }
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + " [");
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
