package org.example;

import java.util.Random;

public class MatrixGenerator {
    public double [][] generateRandomMatrix(int size) {
        double[][] matrix = new double[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(100) + 1;
            }
        }
        return matrix;
    }
}
