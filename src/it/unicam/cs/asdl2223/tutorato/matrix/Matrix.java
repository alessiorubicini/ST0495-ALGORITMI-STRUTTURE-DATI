package it.unicam.cs.asdl2223.tutorato.matrix;

/**
 * Tutorato 2.12.2022
 * Moltiplicazione di matrici
 */
public class Matrix {

    double[][] matrix = null;

    public Matrix(int rows, int columns) {
        this.matrix = new double[rows][columns];
    }

    public Matrix(double[] arr, int columns) {
        int rows = arr.length / columns;
        matrix = new double[rows][columns];
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = arr[count];
                count++;
            }
        }
    }

    public int getRows() {
        return this.matrix.length;
    }

    public int getColumns() {
        return this.matrix[0].length;
    }

    public double get(int row, int column) {
        return this.matrix[row][column];
    }

    public void set(int row, int column, double value) {
        this.matrix[row][column] = value;
    }

    /**
     * Multiplies this matrix with another one
     *
     * @param b Matrix b
     * @return Matrix result
     */
    public Matrix times(Matrix b) {
        if (this.getColumns() != b.getRows()) {
            throw new IllegalArgumentException("Dimensioni non compatibili");
        }
        Matrix c = new Matrix(this.getRows(), b.getColumns());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                c.set(i, j, 0);
                for (int k = 0; k < this.getColumns(); k++) {
                    double cij = c.get(i, j);
                    double aik = this.get(i, k);
                    double bkj = b.get(k, j);
                    c.set(i, j, cij + aik * bkj);
                }
            }
        }
        return c;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] row : this.matrix) {
            for (double x : row) {
                s.append("[ ");
                s.append(x);
                s.append(" ]");
                s.append(", ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
