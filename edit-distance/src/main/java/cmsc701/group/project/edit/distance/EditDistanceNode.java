package cmsc701.group.project.edit.distance;

import java.io.Serializable;

/**
 * Represents a node in the edit distance matrix. The score is the edit distance
 * from the start of the strings to the indices represented by this node. MatchA
 * and matchB represent the traceback and are of the form "ACAG-TAC-ACGT".
 * 
 * @author Valerie Wray
 *
 */
public class EditDistanceNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private int score;
    private StringBuilder matchA = new StringBuilder(); // set initial capacity?
    private StringBuilder matchB = new StringBuilder();
    private long time;

    public EditDistanceNode(int score, EditDistanceNode previousNode, char matchAChar, char matchBChar) {
        this.score = score;
        matchA.append(previousNode.getMatchA()).append(matchAChar);
        matchB.append(previousNode.getMatchB()).append(matchBChar);
    }

    public EditDistanceNode(int score, StringBuilder matchA, StringBuilder matchB) {
        this.score = score;
        this.matchA = matchA;
        this.matchB = matchB;
    }

    public int getScore() {
        return score;
    }

    public StringBuilder getMatchA() {
        return matchA;
    }

    public StringBuilder getMatchB() {
        return matchB;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EditDistanceNode [score=" + score + ", matchA=" + matchA + ", matchB=" + matchB + ", time=" + time
                + "]";
    }
}
