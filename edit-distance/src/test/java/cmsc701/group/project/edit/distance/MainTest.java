package cmsc701.group.project.edit.distance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testRuntime() throws IOException {
        String[] refs = new String[64];
        String[] reads = new String[64];
        int[] sizes = new int[] { 100, 150, 200, 250 };
        int[] errors = new int[] { 5, 25, 50, 75 };
        int[] substitutionRatios = new int[] { 95, 75, 25, 5 };
        int fileIndex = 0;
        for (int size : sizes) {
            for (int error : errors) {
                for (int substitutionRatio : substitutionRatios) {
                    refs[fileIndex] = new StringBuilder("src/test/resources/ref_n").append(size).append("_e")
                            .append(error).append("_s").append(substitutionRatio).append(".fasta").toString();
                    reads[fileIndex] = new StringBuilder("src/test/resources/reads_n").append(size).append("_e")
                            .append(error).append("_s").append(substitutionRatio).append(".fasta").toString();
                    fileIndex++;
                }
            }
        }

        for (int i = 0; i < refs.length; i++) {
            System.out.println(refs[i]);
        }

        List<String> sequences = new ArrayList<>();

        FileWriter fileWriterWithTracebackBanded = new FileWriter(
                "src/test/resources/outputFile_withTracebackBanded2.csv");

        FileWriter fileWriterNoTracebackBanded = new FileWriter("src/test/resources/outputFile_noTracebackBanded2.csv");
        FileWriter fileWriterBitVectorBanded = new FileWriter("src/test/resources/outputFile_bitVectorBanded2.csv");

        FileWriter fileWriterWithTraceback = new FileWriter("src/test/resources/outputFile_withTraceback2.csv");

        FileWriter fileWriterNoTraceback = new FileWriter("src/test/resources/outputFile_noTraceback2.csv");
        FileWriter fileWriterBitVector = new FileWriter("src/test/resources/outputFile_bitVector2.csv");

        for (int i = 0; i < refs.length; i++) {
            String a = Main.readFastaFileFirstEntry(refs[i]);
            sequences = Main.readFastaFile(reads[i]);
            // String b = reads[i];

            int gapCost = -1;
            int mismatchCost = -1;
            int matchCost = 0;
            int bandwidthFull = 1000;
            int bandwidthPartial = (int) (a.length() * 0.1);

            // throw away first 100 runs
            EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, sequences.get(0), gapCost, mismatchCost,
                    matchCost, bandwidthFull);
            for (int j = 0; j < 100; j++) {
                node = CustomAlgorithm.computeEditDistance(a, sequences.get(0), gapCost, mismatchCost, matchCost,
                        bandwidthFull);
            }

            long superSum = 0;
            long superSumBandwidth = 0;
            long superSumNoTraceback = 0;
            long superSumNoTracebackBanded = 0;
            long superSumBitVector = 0;
            long superSumBitVectorBanded = 0;

            for (String b : sequences) {
                // String b = sequences.get(j);
//                System.out.println("a= " + a);
//                System.out.println("b= " + b);

                // get average of 100 runs with traceback
                long sum = 0;
                for (int j = 0; j < 10; j++) {
                    sum += CustomAlgorithm.computeEditDistanceTime(a, b, gapCost, mismatchCost, matchCost,
                            bandwidthFull);
                    // sum += node.getTime();
                }
                sum = sum / 10;
//        System.out.println("average runtime in nanoseconds with traceback: " + sum);

                // get average of 100 runs with traceback with bandwidth 10%
                long sumBandwidth = 0;
                for (int j = 0; j < 10; j++) {
                    sumBandwidth += CustomAlgorithm.computeEditDistanceTime(a, b, gapCost, mismatchCost, matchCost,
                            bandwidthPartial);
                    // sumBandwidth += node.getTime();
                }
                sumBandwidth = sumBandwidth / 10;

                long runtime = 0;
//                CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost,
//                        matchCost, bandwidthFull);
//                for (int j = 0; j < 50; j++) {
//                    runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost,
//                            matchCost, bandwidthFull);
//                }
                // get average of 100 runs with no traceback
                long sumNoTraceback = 0;
                for (int j = 0; j < 10; j++) {
                    runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost,
                            matchCost, bandwidthFull);
                    sumNoTraceback += runtime;
                }
                sumNoTraceback = sumNoTraceback / 10;

                // get average of 100 runs with no traceback, banded
                long sumNoTracebackBanded = 0;
                for (int j = 0; j < 10; j++) {
                    runtime = CustomAlgorithm.computeEditDistanceNoTracebackGetTime(a, b, gapCost, mismatchCost,
                            matchCost, bandwidthPartial);
                    sumNoTracebackBanded += runtime;
                }
                sumNoTracebackBanded = sumNoTracebackBanded / 10;

                // get average of 100 runs with bit vector
                long sumBitVector = 0;
                for (int j = 0; j < 10; j++) {
                    sumBitVector += CustomAlgorithm.computeEditDistanceTimeWithBitVectors(a, b, gapCost, mismatchCost,
                            matchCost, bandwidthFull);
                    // sumBitVector += runtime;
                }
                sumBitVector = sumBitVector / 10;

                // get average of 100 runs with bit vector, banded
                long sumBitVectorBanded = 0;
                for (int j = 0; j < 10; j++) {
                    sumBitVectorBanded += CustomAlgorithm.computeEditDistanceTimeWithBitVectors(a, b, gapCost,
                            mismatchCost, matchCost, bandwidthPartial);
                    // sumBitVectorBanded += runtime;
                }
                sumBitVectorBanded = sumBitVectorBanded / 10;

                superSum += sum;
                superSumBandwidth += sumBandwidth;
                superSumNoTraceback += sumNoTraceback;
                superSumNoTracebackBanded += sumNoTracebackBanded;
                superSumBitVector += sumBitVector;
                superSumBitVectorBanded += sumBitVectorBanded;
            }

//            int numberOfSequences = sequences.size();
//            superSum = superSum / numberOfSequences;
//            superSumBandwidth = superSumBandwidth / numberOfSequences;
//            superSumNoTraceback = superSumNoTraceback / numberOfSequences;
//            superSumNoTracebackBanded = superSumNoTracebackBanded / numberOfSequences;
//            superSumBitVector = superSumBitVector / numberOfSequences;
//            superSumBitVectorBanded = superSumBitVectorBanded / numberOfSequences;

            fileWriterWithTracebackBanded.write(Double.toString(superSumBandwidth / Math.pow(10, 9)));
            fileWriterNoTracebackBanded.write(Double.toString(superSumNoTracebackBanded / Math.pow(10, 9)));
            fileWriterBitVectorBanded.write(Double.toString(superSumBitVectorBanded / Math.pow(10, 9)));

            fileWriterWithTraceback.write(Double.toString(superSum / Math.pow(10, 9)));
            fileWriterNoTraceback.write(Double.toString(superSumNoTraceback / Math.pow(10, 9)));
            fileWriterBitVector.write(Double.toString(superSumBitVector / Math.pow(10, 9)));

            if (i % 16 == 15) {
                fileWriterWithTracebackBanded.write("\n");
                fileWriterNoTracebackBanded.write("\n");
                fileWriterBitVectorBanded.write("\n");
                fileWriterWithTraceback.write("\n");
                fileWriterNoTraceback.write("\n");
                fileWriterBitVector.write("\n");
            } else {
                fileWriterWithTraceback.write(" , ");
                fileWriterNoTraceback.write(" , ");
                fileWriterBitVector.write(" , ");
                fileWriterWithTracebackBanded.write(" , ");
                fileWriterNoTracebackBanded.write(" , ");
                fileWriterBitVectorBanded.write(" , ");

            }

            System.out.println(refs[i]);
            System.out.println("runtime with traceback: " + (double) superSum / (double) (Math.pow(10, 9)));
            System.out.println("runtime no traceback: " + (double) superSumNoTraceback / (double) (Math.pow(10, 9)));
            System.out.println(
                    "runtime with traceback, banded: " + (double) superSumBandwidth / (double) (Math.pow(10, 9)));
            System.out.println(
                    "runtime no traceback, banded: " + (double) superSumNoTracebackBanded / (double) (Math.pow(10, 9)));
            System.out.println("runtime with bitVector: " + (double) superSumBitVector / (double) (Math.pow(10, 9)));
            System.out.println(
                    "runtime with bitVector, banded: " + (double) superSumBitVectorBanded / (double) (Math.pow(10, 9)));
            // System.out.println("memory usage with traceback: " +
            // CustomAlgorithm.computeMemory(a, b));
            // System.out.println("memory usage no traceback: " +
            // CustomAlgorithm.computeMemoryNoTraceback(a, b));
            // System.out.println("memory usage with bitVector: " +
            // CustomAlgorithm.computeMemoryWithBitVector(a, b));
            System.out.println();
        }
        fileWriterWithTracebackBanded.close();
        fileWriterNoTracebackBanded.close();
        fileWriterBitVectorBanded.close();
        fileWriterWithTraceback.close();
        fileWriterNoTraceback.close();
        fileWriterBitVector.close();
    }

}
