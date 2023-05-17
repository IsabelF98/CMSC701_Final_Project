package cmsc701.group.project.edit.distance;

import edu.berkeley.cs.succinct.util.vector.BitVector;

/**
 * Edit distance node that uses bit vectors to store traceback information.
 * 
 * @author Valerie Wray
 *
 */
public class BitVectorNode {
    private int score;
    private BitVector matchA;
    private BitVector matchB;
    private long time;
    private int popCountA = 0;

    private int popCountB = 0;

    public BitVectorNode(int score, BitVectorNode previousNode, boolean setA, boolean setB, int indexA, int indexB) {
        this.score = score;
        matchA = new BitVector(previousNode.getMatchA().getData().clone());
        popCountA = previousNode.getPopCountA();
        if (setA) {
            matchA.setBit(indexA + popCountA);
            popCountA++;
        }
        matchB = new BitVector(previousNode.getMatchB().getData().clone());
        popCountB = previousNode.getPopCountB();
        if (setB) {
            matchB.setBit(indexB + popCountB);
            popCountB++;
        }
    }

    public BitVectorNode(int score, BitVector matchA, BitVector matchB) {
        this.score = score;
        this.matchA = matchA;
        this.matchB = matchB;
    }

    public int getScore() {
        return score;
    }

    public BitVector getMatchA() {
        return matchA;
    }

    public BitVector getMatchB() {
        return matchB;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPopCountA() {
        return popCountA;
    }

    public int getPopCountB() {
        return popCountB;
    }

    @Override
    public String toString() {
        return "BitVectorNode [score=" + score + ", matchA=" + matchA + ", matchB=" + matchB + ", time=" + time
                + ", popCountA=" + popCountA + ", popCountB=" + popCountB + "]";
    }

}
