package it.unicam.cs.asdl2223.mp3;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * Class that solves an instance of the the El Mamun's Caravan problem using
 * dynamic programming.
 *
 * Template: Daniele Marchei and Luca Tesei, Implementation: Alessio rubicini (alessio.rubicini@studenti.unicam.it)
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
        // Check if the given expressio is null
        if (expression == null) {
            throw new NullPointerException("Creazione di solver con expression null");
        }
        // Initialize the expression
        this.expression = expression;
        // Initialize the matrices
        this.table = new Integer [expression.size()][expression.size()];
        this.tracebackTable = new Integer [expression.size()] [expression.size()];
        // Initialize the resolution flag
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
        // Check if the given function is null
        if(function == null) {
            throw new NullPointerException("Function is null");
        }
        // Initialize the variable to store sub-problem result
        int subproblemResult = 0;
        // Loop through all possible sub-problems of increasing length
        for (int length = 1; length <= expression.size(); length += 2) {
            // Iterate over all possible sub-problems of fixed length within the expression
            for (int i = 0; (i < expression.size() - length + 1); i += 2) {
                // Index of the last element in the sub-problem
                int j = i + length - 1;
                // If the sub-problem is just a single digit
                if (length == 1) {
                    // Store the value in the table
                    table[i][j] = (Integer) expression.get(i).getValue();
                    // Store the index in the traceback table
                    tracebackTable[i][j] = i;
                }
                // Otherwise, compute the optimal solution for the sub-problem
                if(i < j && isDigit(i) && isDigit(j)) {
                    // Initialize the list of candidates to be the optimal value
                    List<Integer> optimalCandidates = new ArrayList<Integer>();
                    // Loop through all possible splits of the sub-problem
                    for (int k = 0; (i + k + 2 <= j); k += 2) {
                        // Evaluate the expression resulting from the split
                        int leftOperand = table[i][i+k];
                        int rightOperand = table[i+k+2][j];
                        if (expression.get(i+k+1).getValue().equals("+")) {
                            subproblemResult = leftOperand + rightOperand;
                        } else {
                            subproblemResult = leftOperand * rightOperand;
                        }
                        // Add the result to the list of candidates
                        optimalCandidates.add(subproblemResult);
                    }
                    // Select the optimal value from the list of candidates and store it in the table
                    int bestValue = function.getBest(optimalCandidates);
                    table[i][j] = bestValue;
                    // Select the index of the optimal value and store it in the traceback table
                    int bestIndex = function.getBestIndex(optimalCandidates);
                    tracebackTable[i][j] = bestIndex;
                }
            }
        }
        // Set the solved flag to true
        this.solved = true;
    }

    /**
     * Checks if the expression's item at the given index is a digit
     * @param i
     *              index of the item to check
     * @return true if the item is a digit, false otherwise.
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

    /**
     * Recursive method to compute the optimal parenthesization of a sub-expression
     * @param i
     *              start of the sub-expression
     * @param j
     *              end of the sub-expression
     * @return  the optimal parenthesization as a string
     */
    private String traceback(int i, int j) {
        // If the sub-expression is just a digit, returns the digit as a string
        if (i == j) {
            return expression.get(i).getValue().toString();
        }

        // Otherwise, compute the index of the operator that separates the sub-expression in two sub-expressions
        // The index of the split point must be multiplied by 2 to get the index of the operator in the original expression
        int index = tracebackTable[i][j] * 2;

        // Recursively compute the optimal parenthesization for the two sub-expressions
        // and returns the result with the appropriate parenthesis
        String result = "(" + traceback(i, i+index) + expression.get(i+index+1).toString() + traceback(i+index+2, j) + ")";

        return result;
    }

}