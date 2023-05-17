package cmsc701.group.project.edit.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CustomAlgorithmTest {

    @Test
    void test() {
        String a = "abcde";
        String b = "abbde";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 5);
        node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 5);
        node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 5);
        node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 5);
        assertEquals("abcde", node.getMatchA().toString());
        assertEquals("abbde", node.getMatchB().toString());
        assertEquals(3, node.getScore());

        int score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 5);
        score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 5);
        score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 5);
        score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 5);
        assertEquals(node.getScore(), score);
    }

    @Test
    void test2() {
        String a = "gggabcde";
        String b = "abbde";
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 8);
        CustomAlgorithm.computeEditDistanceWithBitVectors(a, b, -3, -1, 1, 8);
    }

    @Test
    void testBand() {
        String a = "abcdejghijk";
        String b = "abcdefghijk";
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 3);
    }

    @Test
    void testBandGap() {
        String a = "cabcdefghijk";
        String b = "abcdefghijk";
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 3);
    }

    @Test
    void testOffByOne() {
        String a = "abcejghijk";
        String b = "abcdefghijk";
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 11);
    }

    @Test
    void test4() {
        String a = "AACGTCAGTCAGTA";
        String b = "ATACGATACAGTAC";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 6);
        int score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 6);
        assertEquals(node.getScore(), score);
    }

    @Test
    void testStatic() {
        String a = "AGTCAGTA";
        String b = "ATCAGTAC";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 2);
        int score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -3, -1, 1, 2);
        assertEquals(node.getScore(), score);
    }

    @Test
    void testGapA() {
        String a = "CCAAGGTTCCAATTGGCCAAGGTTGGAAACTGACTGACTGACTGACTGACTGACTGACTGACTGYYY";
        String b = "AXXXCCAAGGTTCCAATTGGCCAAGGTTGGAAACTGACTGACTGACTGACTGACTGACTGACTGACTG";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -2, -1, 1, 5);
        int score = CustomAlgorithm.computeEditDistanceNoTraceback(a, b, -2, -1, 1, 5);
        assertEquals(node.getScore(), score);
    }

    @Test
    void testRuntime() {
        String a = "TTGAGCAAAAGCCCCGTATACTTCAGAACACGAGAAGCGGGTATCAGCATTAGCCTGAAGGGAAAGGATTACGAACCAACTGAGAATCGGCCCAATAGCTGATCGCAGTCTTAACACGAGGTTGGTACATTCACCAAACATGTATGTCAAAAG";
        String b = "GTTGAGCAAAACCCCGTATTAGTTAGAACACGGCAAGCGGGTTACTGAATTAGCCTGAAGGGAAAGGGATACGACCAATGAAATCGGCCCAATAGCTGAGCGCTATTTAAACACGAGGTTGGTACATCAACAAAATGTATGTAAAGTATTCCA";
        int gapCost = -1;
        int mismatchCost = -1;
        int matchCost = 0;
        int bandwidth = 1000;

        // throw away first 100 runs
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
        for (int j = 0; j < 100; j++) {
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
        }

        // get average of 100 runs with traceback
        long sum = 0;
        for (int j = 0; j < 100; j++) {
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
            sum += node.getTime();
        }
        sum = sum / 100;
//        System.out.println("average runtime in nanoseconds with traceback: " + sum);

        // get average of 100 runs with traceback with bandwidth 20%
        int bandwidthParam = (int) (a.length() * 0.1);
        long sumBandwidth = 0;
        for (int j = 0; j < 100; j++) {
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidthParam);
            sumBandwidth += node.getTime();
        }
        sumBandwidth = sumBandwidth / 100;

        long runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost, matchCost,
                bandwidth);
        for (int j = 0; j < 50; j++) {
            runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost, matchCost,
                    bandwidth);
        }
        // get average of 100 runs with no traceback
        int sum2 = 0;
        for (int j = 0; j < 100; j++) {
            runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost, matchCost,
                    bandwidth);
            sum2 += runtime;
        }
        sum2 = sum2 / 100;

        // get average of 100 runs with bit vector
        int sumBitVector = 0;
        for (int j = 0; j < 100; j++) {
            runtime = CustomAlgorithm
                    .computeEditDistanceWithBitVectors(a, b, gapCost, mismatchCost, matchCost, bandwidth).getTime();
            sumBitVector += runtime;
        }
        sumBitVector = sumBitVector / 100;

        // get average of 100 runs with bit vector, banded
        int sumBitVectorBanded = 0;
        for (int j = 0; j < 100; j++) {
            runtime = CustomAlgorithm
                    .computeEditDistanceWithBitVectors(a, b, gapCost, mismatchCost, matchCost, bandwidthParam)
                    .getTime();
            sumBitVectorBanded += runtime;
        }
        sumBitVectorBanded = sumBitVectorBanded / 100;

        // get average of 100 runs with no traceback, banded
        int sumBandwidth2 = 0;
        for (int j = 0; j < 100; j++) {
            runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost, matchCost,
                    bandwidthParam);
            sumBandwidth2 += runtime;
        }
        sumBandwidth2 = sumBandwidth2 / 100;

        System.out.println("runtime with traceback: " + (double) sum / (double) (Math.pow(10, 9)));
        System.out.println("runtime no traceback: " + (double) sum2 / (double) (Math.pow(10, 9)));
        System.out.println("runtime with traceback, banded: " + (double) sumBandwidth / (double) (Math.pow(10, 9)));
        System.out.println("runtime no traceback, banded: " + (double) sumBandwidth2 / (double) (Math.pow(10, 9)));
        System.out.println("runtime with bitVector: " + (double) sumBitVector / (double) (Math.pow(10, 9)));
        System.out
                .println("runtime with bitVector, banded: " + (double) sumBitVectorBanded / (double) (Math.pow(10, 9)));
        System.out.println("memory usage with traceback: " + CustomAlgorithm.computeMemory(a, b));
        System.out.println("memory usage no traceback: " + CustomAlgorithm.computeMemoryNoTraceback(a, b));
        System.out.println("memory usage with bitVector: " + CustomAlgorithm.computeMemoryWithBitVector(a, b));
    }

}
