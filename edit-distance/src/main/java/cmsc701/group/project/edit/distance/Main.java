package cmsc701.group.project.edit.distance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A main class to call the {@link CustomAlgorithm} class to compute edit
 * distance between two strings.
 * 
 * @author Valerie Wray
 *
 */
public class Main {

    /**
     * Takes in two strings from the command line or from a file and computes the
     * edit distance.
     * 
     * To call from the command line, send in the two strings as the first two
     * parameters, i.e. "ACGTAAGAT" "ACTACGATAC" with optional parameters gap cost,
     * mismatch cost, match score, and bandwidth space separated in this order. The
     * defaults are gap cost of -3, mismatch cost of -1, match score of 1, and
     * bandwidth of the whole edit distance matrix.
     * 
     * To call with two strings in a file, use parameters file myStringFile.txt
     * myOutputFile.txt. This will read in two strings from the file
     * myStringFile.txt, one string on each line, and output the edit distance
     * computation to myOutputFile.txt. *
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String a = null;
        String outputFile = null;
        int gapCost = -3;
        int mismatchCost = -1;
        int matchCost = 1;
        int bandwidth = 1000;
        int i = 2;
        List<String> sequences = new ArrayList<>();

        if ("file".equals(args[0])) {
            String filename1 = args[1];
            String filename2 = args[i];
            i++;
            outputFile = args[i];
            i++;
            a = readFastaFileFirstEntry(filename1);
            sequences = readFastaFile(filename2);
        } else {
            a = args[0];
            sequences.add(args[1]);
        }
        if (args.length > i) {
            gapCost = Integer.valueOf(args[i]);
            i++;

            if (args.length > i) {
                mismatchCost = Integer.valueOf(args[i]);
                i++;

                if (args.length > i) {
                    matchCost = Integer.valueOf(args[i]);
                    i++;

                    if (args.length > i) {
                        bandwidth = Integer.valueOf(args[i]);
                    }
                }
            }
        }

        FileWriter fileWriter = null;
        if (outputFile != null) {
            fileWriter = new FileWriter(outputFile);
        }

        for (String b : sequences) {

            EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost,
                    bandwidth);
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
            node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
            long sum = 0;
            for (int j = 0; j < 100; j++) {
                node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
                sum += node.getTime();
            }
            sum = sum / 100;
            System.out.println("average runtime in nanoseconds: " + sum);

            if (outputFile != null) {
                fileWriter.write(node.getMatchA().append("\n").append(node.getMatchB()).append("\n")
                        .append(node.getScore()).append("\n").append(node.getTime()).append("\n\n").toString());

            } else {
                System.out.println(node);
            }
        }
        if (outputFile != null) {
            fileWriter.close();
        }
    }

    /**
     * Reads in the first entry in FASTA file and returns a string containing the
     * text.
     * 
     * @param filename the file name
     * @return the text string
     * @throws IOException if the file cannot be found
     */
    protected static String readFastaFileFirstEntry(String filename) throws IOException {
        StringBuilder inputString = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        boolean isFirst = true;
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (line.charAt(0) != '>') {
                inputString = inputString.append(line);
            } else if (!isFirst) {
                break;
            }
            isFirst = false;
        }
        bufferedReader.close();
        return inputString.toString();
    }

    /**
     * Reads in the first entry in FASTA file and returns a string containing the
     * text.
     * 
     * @param filename the file name
     * @return the text string
     * @throws IOException if the file cannot be found
     */
    protected static List<String> readFastaFile(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line = "";
        List<String> sequences = new ArrayList<String>();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.charAt(0) != '>') {
                sequences.add(line);
            }
        }
        bufferedReader.close();
        return sequences;
    }

}
