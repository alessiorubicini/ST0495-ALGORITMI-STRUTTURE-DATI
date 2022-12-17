package it.unicam.cs.asdl2223.mp3;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * Class that solves an instance of the the El Mamun's Caravan problem using
 * dynamic programming.
 * 
 * Template: Daniele Marchei and Luca Tesei, Implementation: ALESSIO RUBICINI (alessio.rubicini@studenti.unicam.it)
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
        this.table = new Integer [expression.size()][expression.size()];
        this.tracebackTable = new Integer [expression.size()] [expression.size()];
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
        if(function == null) {
            throw new NullPointerException("Function is null");
        }
        // Clears the matrices
        table = new Integer[expression.size()][expression.size()];
        tracebackTable = new Integer[expression.size()][expression.size()];
        // Runs recursive solver on 0, size-1 with the given function
        this.recursiveSolver(0, expression.size()-1, function);
        // Set the solved flag to true
        this.solved = true;
    }

    private int recursiveSolver (int i, int j, ObjectiveFunction function) {
        if (!(table[i][j] == null)) {
            return table [i][j]; //basic case where the element at indexes i1, i2 is already existing
        }

        if (i==j) {
            table[i][j] = (Integer) expression.get(i).getValue();
            return table[i][j];
        }

        // i < j and i and j are digits
        if (i<j && this.isDigit(i) && this.isDigit(j)) {
            // Initializes operation result and list of candidates
            int result = 0;
            List<Integer> candidates = new ArrayList<>();

            // Iterates over the allowed Ks
            for (int k = 0; (i + k + 2 <= j); k += 2) {
                // Recursive calls on the sub-problems
                int rec1 = this.recursiveSolver(i, i+k, function);
                int rec2 = this.recursiveSolver(i+k+2, j, function);
                // Calculates the result
                String operator = expression.get(i+k+1).getValue().toString();
                if(operator.equals("+")) {
                    result = rec1 + rec2;
                } else {
                    result = rec1 * rec2;
                }
                // Adds the result to the candidates' list
                candidates.add(result);
            }
            // Save the optimal value in the table
            table[i][j] = function.getBest(candidates);
            // Saves the respective k of the optimal value in the traceback table
            tracebackTable[i][j] = function.getBestIndex(candidates);

        }
        return table[i][j];
    }

    /**
     * Checks if the expression's item at the given index is a digit
     * @param i
     *              index of the item to check
     * @return      true if the item is a digit, false otherwise
     */
    private boolean isDigit(int i) {
        return expression.get(i).getType().equals(ItemType.DIGIT);
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
        // Checks if the problem is solved
        if(!this.isSolved()) {
            throw new IllegalStateException("Problem not solved");
        }
        // Returns the optimal solution
        return this.table[0][table.length - 1];
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
        // Checks if the problem is solved
        if(!this.isSolved()) {
            throw new IllegalStateException("Problem not solved");
        }
        // Calls traceback on the whole expression
        String parenthesization = this.traceback(0, expression.size() - 1);
        // Returns the optimal parenthesization
        return parenthesization;
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
            return expression.get(i).getValue().toString();
        }

        // Otherwise, compute the index of the operator that separates the
        // sub-expression in two sub-expressions
        int index = tracebackTable[i][j] * 2;

        // Recursively compute the optimal parenthesization for the two
        // sub-expressions and return the result with the appropriate parenthesis
        String result = "("+ traceback(i, i + index)  + expression.get(i+index+1).toString()
                + traceback(i + index + 2, j) + ")";

        return result;
    }

}
