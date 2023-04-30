package cmsc701.group.project.edit.distance;

import org.slf4j.profiler.Profiler;

/**
 * An algorithm designed for computing edit distance and optimal alignment which
 * is optimized for short sequences of lengths of a few hundred characters or
 * less.
 * 
 * @author Valerie Wray
 *
 */
public class CustomAlgorithm {
    private static int MIN_SCORE = -2000;

    /**
     * Computes the edit distance between string a and string b.
     * 
     * @return the {@link EditDistanceNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static EditDistanceNode computeEditDistance(String a, String b, int gapCost, int mismatchCost,
            int matchScore, int bandwidth) {
        // TODO: below
        // this.bandwidth = Math.min(bandwidth, b.length());
        Profiler myProfiler = new Profiler("RankSupportProfiler");
        myProfiler.start("Timing edit distance of " + a + " and " + b);

        EditDistanceNode[] previousNodes = new EditDistanceNode[b.length()];
        EditDistanceNode[] currentNodes = new EditDistanceNode[b.length()];
        // i=j=0 case, we assume we match the first character of a and b
        previousNodes[0] = new EditDistanceNode((a.charAt(0) == b.charAt(0) ? matchScore : mismatchCost),
                new StringBuilder(a.substring(0, 1)), new StringBuilder(b.substring(0, 1)));
        // compute the edit distances for the rest of the i=0 column by adding the gap
        // score plus the previous score
        for (int j = 1; j < Math.min(1 + bandwidth, b.length()); j++) {
            previousNodes[j] = new EditDistanceNode(previousNodes[j - 1].getScore() + gapCost,
                    new StringBuilder(previousNodes[j - 1].getMatchA()).append("-"),
                    new StringBuilder(previousNodes[j - 1].getMatchB()).append(b.charAt(j)));
        }

        // loop through each column (character in string a)
        for (int i = 1; i < a.length(); i++) {
            // loop through each character in b
            for (int j = Math.max(i - bandwidth, 0); j < Math.min(i + bandwidth, b.length()); j++) {
                // System.out.println("i=" + i + " j=" + j);
                EditDistanceNode previousNode = null;
                int currentCost;
                char matchAChar = 0;
                char matchBChar = 0;
                // System.out.print("PreviousNode: ");
                // if we are still at the beginning of string b, add the gap cost
                if (j == 0) {
                    // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                    previousNode = previousNodes[j];
                    currentCost = previousNodes[j].getScore() + gapCost;
                    matchAChar = a.charAt(i);
                    matchBChar = '-';
                } else {
                    // calculate the scores using previous node options of (i-1,j-1), (i-1,j), and
                    // (i,j-1) and take the maximum score
                    int diagonal = (a.charAt(i) == b.charAt(j) ? matchScore : mismatchCost)
                            + previousNodes[j - 1].getScore();
                    int gapA = previousNodes[j] != null ? gapCost + previousNodes[j].getScore() : MIN_SCORE;
                    int gapB = gapCost + currentNodes[j - 1].getScore();
                    int max = Math.max(diagonal, Math.max(gapA, gapB));
                    currentCost = max;
                    // set the previous node attributes appropriately
                    if (max == diagonal) {
                        // System.out.print("i=" + (i - 1) + " j=" + (j - 1) + " ");
                        previousNode = previousNodes[j - 1];
                        matchAChar = a.charAt(i);
                        matchBChar = b.charAt(j);
                    } else if (max == gapA) {
                        // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                        previousNode = previousNodes[j];
                        matchAChar = a.charAt(i);
                        matchBChar = '-';
                    } else if (max == gapB) {
                        // System.out.print("i=" + (i) + " j=" + (j - 1) + " ");
                        previousNode = currentNodes[j - 1];
                        matchAChar = '-';
                        matchBChar = b.charAt(j);
                    }
                }
                // System.out.println(previousNode);
                // create the new EditDistanceNode with the attributes set above
                currentNodes[j] = new EditDistanceNode(currentCost, previousNode, matchAChar, matchBChar);
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        EditDistanceNode finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();
        finalNode.setTime(myProfiler.elapsedTime());
        System.out.println("FinalNode: " + finalNode);
        System.out.println();
        System.out.println(finalNode.getMatchA());
        System.out.println(finalNode.getMatchB());
        System.out.println("Score: " + finalNode.getScore());
        System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        return finalNode;
    }
}