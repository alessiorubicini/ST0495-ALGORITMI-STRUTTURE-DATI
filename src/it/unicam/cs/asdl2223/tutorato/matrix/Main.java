package it.unicam.cs.asdl2223.tutorato.matrix;

public class Main {
    public static void main(String[] args) {
        Matrix a = new Matrix(new double[] {0,1,3,7,2,9,6,1,4,3,3,2}, 3);
        System.out.println(a);
        Matrix b = new Matrix(new double[] {2,7,8,5,3,8,1,7,8,5,7,5,2,4,4}, 5);
        System.out.println(b);
        System.out.println(a.times(b));
    }
    
}
