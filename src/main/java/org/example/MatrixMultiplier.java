package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplier {
    public double[][] multiplyMatrixWithExecutor(double[][] matrixA, double[][] matrixB, int numThreads) {
        int size = matrixA.length;
        double[][] result = new double[size][size];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < size; i++) {
            final int row = i;
            executor.submit(() -> {
                for (int j = 0; j < size; j++) {
                    result[row][j] = 0;
                    for (int k = 0; k < size; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public double[][] multiplyMatrixWithThreads(double[][] matrixA, double[][] matrixB, int numThreads) {
        int size = matrixA.length;
        double[][] result = new double[size][size];
        int actualNumThreads = Math.min(numThreads, size);
        Thread[] threads = new Thread[actualNumThreads];

        for (int i = 0; i < actualNumThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int row = threadId; row < size; row += actualNumThreads) {
                    for (int j = 0; j < size; j++) {
                        result[row][j] = 0;
                        for (int k = 0; k < size; k++) {
                            result[row][j] += matrixA[row][k] * matrixB[k][j];
                        }
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < actualNumThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
