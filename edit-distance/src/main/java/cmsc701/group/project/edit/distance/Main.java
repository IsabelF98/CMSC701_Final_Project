package cmsc701.group.project.edit.distance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
        String b = null;
        String outputFile = null;
        int gapCost = -3;
        int mismatchCost = -1;
        int matchCost = 1;
        int bandwidth = 1000;
        int i = 2;

        if ("file".equals(args[0])) {
            String filename = args[1];
            outputFile = args[i];
            i++;
            try (Scanner scanner = new Scanner(new File(filename))) {
                a = scanner.nextLine().trim();
                b = scanner.nextLine().trim();
            }
        } else {
            a = args[0];
            b = args[1];
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

        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, gapCost, mismatchCost, matchCost, bandwidth);
        if (outputFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(outputFile);
                fileWriter.write(node.getMatchA().append("\n").append(node.getMatchB()).append("\n")
                        .append(node.getScore()).append("\n").append(node.getTime()).toString());
                fileWriter.close();
                System.out.println("Successfully wrote to the output file.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

        } else {
            System.out.println(node);
        }
    }

}
