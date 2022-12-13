package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;
import java.util.List;

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
        this.table = new Integer[expression.size()][expression.size()];
        this.tracebackTable = new Integer[expression.size()][expression.size()];
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
     * Initializes the tables used by this solver.
     */
    private void initializeTables() {
        for (int i = 0; i < expression.size(); i++) {
            for (int j = 0; j < expression.size(); j++) {
                table[i][j] = null;
                tracebackTable[i][j] = null;
            }
        }
    }

    private List<Integer> getCandidateSolutions(int start, int end, Expression expression) {
        List<Integer> candidates = new ArrayList<>();

        // Iterate over the possible split positions of the sub-problem
        for (int split = start; split < end; split++) {
            // Left and right sub-problems
            int left = table[start][split];
            int right = table[split + 1][end];

            // Get the operator at the split position
            String operator = expression.get(split).getValue().toString();

            // Calculate the candidate solution based on the operator
            if (operator.equals("+")) {
                candidates.add(left + right);
            } else if (operator.equals("*")) {
                candidates.add(left * right);
            }
        }

        return candidates;
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
        if (function == null) {
            throw new NullPointerException("Objective function is null");
        }

        initializeTables();

        for (int span = 1; span <= expression.size(); span++) {
            for (int i = 0; i + span <= expression.size(); i++) {
                int j = i + span - 1;
                if (span == 1) {
                    // base case: a single digit
                    table[i][j] = (Integer) expression.get(i).getValue();
                } else {
                    // general case: find the optimal solution among the candidates
                    List<Integer> candidates = getCandidateSolutions(i, j, expression);
                    table[i][j] = function.getBest(candidates);

                    // record the chosen optimal solution in the traceback table
                    int k = function.getBestIndex(candidates);
                    tracebackTable[i][j] = k;
                }
            }
        }

        this.solved = true;
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
        if (!solved) {
            throw new IllegalStateException("Problem not solved");
        }
        return table[0][expression.size() - 1];
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

    // TODO implementare: inserire eventuali metodi privati per rendere l'implementazione più modulare

    // Recursive method to compute the optimal parenthesization of a sub-expression
    private String traceback(int i, int j) {
        // If the sub-expression is just a digit, return the digit as a string
        if (i == j)
            return expression.get(i).toString();
        // Otherwise, compute the index of the operator that separates the
        // sub-expression in two sub-expressions
        int k = tracebackTable[i][j];
        // Recursively compute the optimal parenthesization for the two
        // sub-expressions and return the result with the appropriate parenthesis
        return "(" + traceback(i, k) + expression.get(k) + traceback(k + 1, j) + ")";
    }
}
