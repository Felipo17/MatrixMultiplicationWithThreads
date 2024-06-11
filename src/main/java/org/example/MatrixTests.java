package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixTests {

    @Test
    public void testGenerateRandomMatrix() {
        MatrixGenerator generator = new MatrixGenerator();
        int size = 5;
        double[][] matrix = generator.generateRandomMatrix(size);

        assertEquals(size, matrix.length, "Matrix should have correct number of rows.");
        for (int i = 0; i < size; i++) {
            assertEquals(size, matrix[i].length, "Matrix should have correct number of columns.");
        }

        // Sprawdzenie czy wartości są w zakresie od 1 do 100
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assertTrue(matrix[i][j] >= 1 && matrix[i][j] <= 100, "Matrix values should be within range 1-100.");
            }
        }
    }

    @Test
    public void testMultiplyMatrixWithExecutor() {
        MatrixGenerator generator = new MatrixGenerator();
        MatrixMultiplier multiplier = new MatrixMultiplier();

        double[][] matrixA = generator.generateRandomMatrix(3);
        double[][] matrixB = generator.generateRandomMatrix(3);
        double[][] result = multiplier.multiplyMatrixWithExecutor(matrixA, matrixB, 2);

        assertNotNull(result, "Result matrix should not be null.");
        assertEquals(3, result.length, "Result matrix should have correct number of rows.");
        assertEquals(3, result[0].length, "Result matrix should have correct number of columns.");

        // Dodatkowe sprawdzenie wartości macierzy wynikowej
        assertMatrixMultiplication(matrixA, matrixB, result);
    }

    @Test
    public void testMultiplyMatrixWithThreads() {
        MatrixGenerator generator = new MatrixGenerator();
        MatrixMultiplier multiplier = new MatrixMultiplier();

        double[][] matrixA = generator.generateRandomMatrix(3);
        double[][] matrixB = generator.generateRandomMatrix(3);
        double[][] result = multiplier.multiplyMatrixWithThreads(matrixA, matrixB, 2);

        assertNotNull(result, "Result matrix should not be null.");
        assertEquals(3, result.length, "Result matrix should have correct number of rows.");
        assertEquals(3, result[0].length, "Result matrix should have correct number of columns.");

        // Dodatkowe sprawdzenie wartości macierzy wynikowej
        assertMatrixMultiplication(matrixA, matrixB, result);
    }

    @Test
    public void testMultiplyMatrixWithExecutorWithSpecificMatrices() {
        MatrixMultiplier multiplier = new MatrixMultiplier();
        double[][] matrixA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] matrixB = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };
        double[][] expected = {
                {30, 24, 18},
                {84, 69, 54},
                {138, 114, 90}
        };

        double[][] result = multiplier.multiplyMatrixWithExecutor(matrixA, matrixB, 2);

        assertNotNull(result, "Result matrix should not be null.");
        assertEquals(expected.length, result.length, "Result matrix should have correct number of rows.");
        assertEquals(expected[0].length, result[0].length, "Result matrix should have correct number of columns.");
        assertMatrixEquals(expected, result, "Result matrix should match expected matrix.");
    }

    @Test
    public void testMultiplyMatrixWithThreadsWithSpecificMatrices() {
        MatrixMultiplier multiplier = new MatrixMultiplier();
        double[][] matrixA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] matrixB = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };
        double[][] expected = {
                {30, 24, 18},
                {84, 69, 54},
                {138, 114, 90}
        };

        double[][] result = multiplier.multiplyMatrixWithThreads(matrixA, matrixB, 2);

        assertNotNull(result, "Result matrix should not be null.");
        assertEquals(expected.length, result.length, "Result matrix should have correct number of rows.");
        assertEquals(expected[0].length, result[0].length, "Result matrix should have correct number of columns.");
        assertMatrixEquals(expected, result, "Result matrix should match expected matrix.");
    }

    private void assertMatrixMultiplication(double[][] matrixA, double[][] matrixB, double[][] result) {
        int size = matrixA.length;
        double[][] expected = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                expected[i][j] = 0;
                for (int k = 0; k < size; k++) {
                    expected[i][j] += matrixA[i][k] * matrixB[k][j];
                }
                assertEquals(expected[i][j], result[i][j], 0.0001, "Matrix multiplication result should be correct.");
            }
        }
    }

    private void assertMatrixEquals(double[][] expected, double[][] actual, String message) {
        assertEquals(expected.length, actual.length, "Matrices should have the same number of rows.");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], message + " Row " + i + " should be equal.");
        }
    }
}
