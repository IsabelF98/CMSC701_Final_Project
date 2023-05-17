package cmsc701.group.project.edit.distance;

import org.slf4j.profiler.Profiler;

import edu.berkeley.cs.succinct.util.vector.BitVector;

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
                // create the new EditDistanceNode with the attributes set above
                currentNodes[j] = new EditDistanceNode(currentCost, previousNode, matchAChar, matchBChar);
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        EditDistanceNode finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();

        finalNode.setTime(myProfiler.elapsedTime());
//        System.out.println("FinalNode: " + finalNode);
//        System.out.println();
//        System.out.println(finalNode.getMatchA());
//        System.out.println(finalNode.getMatchB());
//        System.out.println("Score: " + finalNode.getScore());
//        System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        return finalNode;
    }

    /**
     * Computes the edit distance time between string a and string b.
     * 
     * @return the {@link EditDistanceNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static long computeEditDistanceTime(String a, String b, int gapCost, int mismatchCost, int matchScore,
            int bandwidth) {
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
                // create the new EditDistanceNode with the attributes set above
                currentNodes[j] = new EditDistanceNode(currentCost, previousNode, matchAChar, matchBChar);
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        EditDistanceNode finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();

        return myProfiler.elapsedTime();
//        System.out.println("FinalNode: " + finalNode);
//        System.out.println();
//        System.out.println(finalNode.getMatchA());
//        System.out.println(finalNode.getMatchB());
//        System.out.println("Score: " + finalNode.getScore());
//        System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        // return finalNode;
    }

    /**
     * Computes the edit distance between string a and string b.
     * 
     * @return the {@link BitVectorNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static BitVectorNode computeEditDistanceWithBitVectors(String a, String b, int gapCost, int mismatchCost,
            int matchScore, int bandwidth) {
        Profiler myProfiler = new Profiler("RankSupportProfiler");
        myProfiler.start("Timing edit distance of " + a + " and " + b);
        int maxLength = a.length() + b.length();

        BitVectorNode[] previousNodes = new BitVectorNode[b.length()];
        BitVectorNode[] currentNodes = new BitVectorNode[b.length()];
        // i=j=0 case, we assume we match the first character of a and b
        boolean firstCharMatch = a.charAt(0) == b.charAt(0);
        previousNodes[0] = new BitVectorNode((firstCharMatch ? matchScore : mismatchCost), new BitVector(maxLength),
                new BitVector(maxLength));
        // compute the edit distances for the rest of the i=0 column by adding the gap
        // score plus the previous score
        for (int j = 1; j < Math.min(1 + bandwidth, b.length()); j++) {
            previousNodes[j] = new BitVectorNode(previousNodes[j - 1].getScore() + gapCost, previousNodes[j - 1],
                    true, false, 0, j - 1);
            // new StringBuilder(previousNodes[j - 1].getMatchA()).append("-"),
            // new StringBuilder(previousNodes[j - 1].getMatchB()).append(b.charAt(j)));
        }

        // loop through each column (character in string a)
        for (int i = 1; i < a.length(); i++) {
            // loop through each character in b
            for (int j = Math.max(i - bandwidth, 0); j < Math.min(i + bandwidth, b.length()); j++) {
                // System.out.println("i=" + i + " j=" + j);
                BitVectorNode previousNode = null;
                int currentCost;
                // char matchAChar = 0;
                // char matchBChar = 0;
                boolean setA = false;
                boolean setB = false;
                // System.out.print("PreviousNode: ");
                // if we are still at the beginning of string b, add the gap cost
                if (j == 0) {
                    // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                    previousNode = previousNodes[j];
                    currentCost = previousNodes[j].getScore() + gapCost;
                    setB = true;
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
                    } else if (max == gapA) {
                        // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                        previousNode = previousNodes[j];
                        setB = true;
                    } else if (max == gapB) {
                        // System.out.print("i=" + (i) + " j=" + (j - 1) + " ");
                        previousNode = currentNodes[j - 1];
                        setA = true;
                    }
                }
                // create the new NodeWithBitVector with the attributes set above
                currentNodes[j] = new BitVectorNode(currentCost, previousNode, setA, setB, i, j);
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        BitVectorNode finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();

        finalNode.setTime(myProfiler.elapsedTime());
//        System.out.println(finalNode.getMatchA().serializedSize());
//        System.out.println("FinalNode: " + finalNode);
//        System.out.println();
//        int indexA = 0;
//        for (int i = 0; i < maxLength; i++) {
//            if (finalNode.getMatchA().getBit(i) == 1) {
//                System.out.print("-");
//            } else {
//                System.out.print(a.charAt(indexA));
//                indexA++;
//            }
//        }
//        System.out.println();
//
//        int indexB = 0;
//        for (int j = 0; j < maxLength; j++) {
//            if (finalNode.getMatchB().getBit(j) == 1) {
//                System.out.print("-");
//            } else {
//                System.out.print(b.charAt(indexB));
//                indexB++;
//            }
//        }

        // System.out.println();
        // System.out.println(finalNode.getMatchA());
        // System.out.println(finalNode.getMatchB());
        // System.out.println("Score: " + finalNode.getScore());
        // System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        return finalNode;
    }

    /**
     * Computes the edit distance time between string a and string b.
     * 
     * @return the {@link BitVectorNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static long computeEditDistanceTimeWithBitVectors(String a, String b, int gapCost, int mismatchCost,
            int matchScore, int bandwidth) {
        Profiler myProfiler = new Profiler("RankSupportProfiler");
        myProfiler.start("Timing edit distance of " + a + " and " + b);
        int maxLength = a.length() + b.length();

        BitVectorNode[] previousNodes = new BitVectorNode[b.length()];
        BitVectorNode[] currentNodes = new BitVectorNode[b.length()];
        // i=j=0 case, we assume we match the first character of a and b
        boolean firstCharMatch = a.charAt(0) == b.charAt(0);
        previousNodes[0] = new BitVectorNode((firstCharMatch ? matchScore : mismatchCost), new BitVector(maxLength),
                new BitVector(maxLength));
        // compute the edit distances for the rest of the i=0 column by adding the gap
        // score plus the previous score
        for (int j = 1; j < Math.min(1 + bandwidth, b.length()); j++) {
            previousNodes[j] = new BitVectorNode(previousNodes[j - 1].getScore() + gapCost, previousNodes[j - 1],
                    true, false, 0, j - 1);
            // new StringBuilder(previousNodes[j - 1].getMatchA()).append("-"),
            // new StringBuilder(previousNodes[j - 1].getMatchB()).append(b.charAt(j)));
        }

        // loop through each column (character in string a)
        for (int i = 1; i < a.length(); i++) {
            // loop through each character in b
            for (int j = Math.max(i - bandwidth, 0); j < Math.min(i + bandwidth, b.length()); j++) {
                // System.out.println("i=" + i + " j=" + j);
                BitVectorNode previousNode = null;
                int currentCost;
                // char matchAChar = 0;
                // char matchBChar = 0;
                boolean setA = false;
                boolean setB = false;
                // System.out.print("PreviousNode: ");
                // if we are still at the beginning of string b, add the gap cost
                if (j == 0) {
                    // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                    previousNode = previousNodes[j];
                    currentCost = previousNodes[j].getScore() + gapCost;
                    setB = true;
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
                    } else if (max == gapA) {
                        // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                        previousNode = previousNodes[j];
                        setB = true;
                    } else if (max == gapB) {
                        // System.out.print("i=" + (i) + " j=" + (j - 1) + " ");
                        previousNode = currentNodes[j - 1];
                        setA = true;
                    }
                }
                // create the new NodeWithBitVector with the attributes set above
                currentNodes[j] = new BitVectorNode(currentCost, previousNode, setA, setB, i, j);
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        BitVectorNode finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();

        return myProfiler.elapsedTime();
    }

    /**
     * Computes the edit distance between string a and string b with no traceback.
     * 
     * @return the {@link EditDistanceNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static int computeEditDistanceNoTraceback(String a, String b, int gapCost, int mismatchCost, int matchScore,
            int bandwidth) {

        int[] previousNodes = new int[b.length()];
        int[] currentNodes = new int[b.length()];
        // i=j=0 case, we assume we match the first character of a and b
        previousNodes[0] = a.charAt(0) == b.charAt(0) ? matchScore : mismatchCost;
        // compute the edit distances for the rest of the i=0 column by adding the gap
        // score plus the previous score
        for (int j = 1; j < Math.min(1 + bandwidth, b.length()); j++) {
            previousNodes[j] = previousNodes[j - 1] + gapCost;
            currentNodes[j] = MIN_SCORE;
        }
        for (int j = Math.min(1 + bandwidth, b.length()); j < b.length(); j++) {
            previousNodes[j] = MIN_SCORE;
            currentNodes[j] = MIN_SCORE;
        }

        // loop through each column (character in string a)
        for (int i = 1; i < a.length(); i++) {
            // loop through each character in b
            for (int j = Math.max(i - bandwidth, 0); j < Math.min(i + bandwidth, b.length()); j++) {
                int previousNode = 0;
                int currentCost;
                // if we are still at the beginning of string b, add the gap cost
                if (j == 0) {
                    previousNode = previousNodes[j];
                    currentCost = previousNode + gapCost;
                } else {
                    // calculate the scores using previous node options of (i-1,j-1), (i-1,j), and
                    // (i,j-1) and take the maximum score
                    int diagonal = (a.charAt(i) == b.charAt(j) ? matchScore : mismatchCost) + previousNodes[j - 1];
                    int gapA = gapCost + previousNodes[j];
                    int gapB = gapCost + currentNodes[j - 1];
                    currentCost = Math.max(diagonal, Math.max(gapA, gapB));
                }
                // create the new EditDistanceNode with the score calculated above
                currentNodes[j] = currentCost;
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        int finalNode = currentNodes[b.length() - 1];
//        System.out.println();
//        System.out.println("Score (no traceback): " + finalNode);
//        System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        return finalNode;
    }

    /**
     * Computes the edit distance between string a and string b with no traceback
     * and returns the runtime.
     * 
     * @return the {@link EditDistanceNode} containing the edit distance score and
     *         the optimal alignment traceback.
     */
    public static long computeEditDistanceNoTracebackGetTime(String a, String b, int gapCost, int mismatchCost,
            int matchScore, int bandwidth) {
        Profiler myProfiler = new Profiler("RankSupportProfiler");
        myProfiler.start("Timing edit distance with no traceback of " + a + " and " + b);

        int[] previousNodes = new int[b.length()];
        int[] currentNodes = new int[b.length()];
        // i=j=0 case, we assume we match the first character of a and b
        previousNodes[0] = a.charAt(0) == b.charAt(0) ? matchScore : mismatchCost;
        // compute the edit distances for the rest of the i=0 column by adding the gap
        // score plus the previous score
        for (int j = 1; j < Math.min(1 + bandwidth, b.length()); j++) {
            previousNodes[j] = previousNodes[j - 1] + gapCost;
            currentNodes[j] = MIN_SCORE;
        }
        for (int j = Math.min(1 + bandwidth, b.length()); j < b.length(); j++) {
            previousNodes[j] = MIN_SCORE;
            currentNodes[j] = MIN_SCORE;
        }

        // loop through each column (character in string a)
        for (int i = 1; i < a.length(); i++) {
            // loop through each character in b
            for (int j = Math.max(i - bandwidth, 0); j < Math.min(i + bandwidth, b.length()); j++) {
                // System.out.println("i=" + i + " j=" + j);
                int previousNode = 0;
                int currentCost;
                // System.out.print("PreviousNode: ");
                // if we are still at the beginning of string b, add the gap cost
                if (j == 0) {
                    // System.out.print("i=" + (i - 1) + " j=" + j + " ");
                    previousNode = previousNodes[j];
                    currentCost = previousNode + gapCost;
                } else {
                    // calculate the scores using previous node options of (i-1,j-1), (i-1,j), and
                    // (i,j-1) and take the maximum score
                    int diagonal = (a.charAt(i) == b.charAt(j) ? matchScore : mismatchCost) + previousNodes[j - 1];
                    int gapA = gapCost + previousNodes[j];
                    int gapB = gapCost + currentNodes[j - 1];
                    currentCost = Math.max(diagonal, Math.max(gapA, gapB));
                }
                // System.out.println(previousNode);
                // create the new EditDistanceNode with the score calculated above
                currentNodes[j] = currentCost;
            }
            // copy all the current nodes into the previous nodes to begin the next loop
            previousNodes = currentNodes.clone();
        }
        int finalNode = currentNodes[b.length() - 1];
        myProfiler.stop();
//        System.out.println();
//        System.out.println("Score (no traceback): " + finalNode);
//        System.out.println("Total time in nanoseconds: " + myProfiler.elapsedTime());
        // return finalNode;
        return myProfiler.elapsedTime();
    }

    public static long computeMemory(String a, String b) {
        return (8 * (int) (((a.length() * 2) + 45) / 8) + 8 * (int) (((b.length() * 2) + 45) / 8) + 32) * b.length() * 2
                * 8 + 32 * 4 + 32 * 8 + 32 * 8;
    }

    public static long computeMemoryNoTraceback(String a, String b) {
        // each int is 32 bits
        return (8 * (int) (((a.length() * 2) + 45) / 8) + 8 * (int) (((b.length() * 2) + 45) / 8) * 8 // reading in the
                                                                                                      // strings
                + 32 * b.length() * 2 // integers in the edit distance matrix
                + 32 * 4 // int params
                + 32 * 8 + 32 * 8); // array
    }

    public static long computeMemoryWithBitVector(String a, String b) {
        int maxStringLength = Math.max(a.length(), b.length());
        int numberOfBlocks = (int) (maxStringLength % 64 == 0 ? (maxStringLength >>> 6)
                : ((maxStringLength >>> 6) + 1));
        int bitVectorSize = (8 * numberOfBlocks + 4) * 8;
        return (8 * (int) (((a.length() * 2) + 45) / 8) + 8 * (int) (((b.length() * 2) + 45) / 8) * 8 // reading in the
        // strings
                + 32 * b.length() * 2 // integers in the edit distance matrix
                + bitVectorSize * b.length() * 4 // bit vector size
                + 32 * 2 // pop count variables
                + 32 * 4 // int params
                + 32 * 8 + 32 * 8);
    }
}
