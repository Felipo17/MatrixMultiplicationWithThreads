package org.example;

import java.util.Scanner;

public class MatrixMultiplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pobranie rozmiaru macierzy
        int size = getInput(scanner, "Enter matrix size: ");
        if (size <= 0) {
            System.out.println("Matrix size must be positive.");
            return;
        }

        // Pobranie liczby wątków
        int numThreads = getInput(scanner, "Enter number of threads: ");
        if (numThreads <= 0) {
            System.out.println("Number of threads must be positive.");
            return;
        }

        MatrixGenerator generator = new MatrixGenerator();
        double[][] matrixA = generator.generateRandomMatrix(size);
        double[][] matrixB = generator.generateRandomMatrix(size);

        MatrixMultiplier multiplier = new MatrixMultiplier();

        long startTime = System.nanoTime();
        double[][] resultMatrixExecutor = multiplier.multiplyMatrixWithExecutor(matrixA, matrixB, numThreads);
        long endTime = System.nanoTime();
        long durationExecutor = endTime - startTime;

        System.out.println("Execution time with ExecutorService: " + durationExecutor / 1_000_000 + "ms");

        startTime = System.nanoTime();
        double[][] resultMatrixThreads = multiplier.multiplyMatrixWithThreads(matrixA, matrixB, numThreads);
        endTime = System.nanoTime();
        long durationThreads = endTime - startTime;

        System.out.println("Execution time with Thread: " + durationThreads / 1_000_000 + "ms");

        // Wyświetlenie macierzy
//        System.out.println("MATRIX A:");
//        printMatrix(matrixA);
//        System.out.println("MATRIX B:");
//        printMatrix(matrixB);
//        System.out.println("RESULT MATRIX:");
//        printMatrix(resultMatrixExecutor);
//        System.out.println("THREADS:");
//        printMatrix(resultMatrixThreads);
    }

    private static int getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print((int) value + " ");
            }
            System.out.println();
        }
    }
}
