package cmsc701.group.project.edit.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CustomAlgorithmTest {

    @Test
    void test() {
        String a = "abcde";
        String b = "abbde";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 5);
        assertEquals("abcde", node.getMatchA().toString());
        assertEquals("abbde", node.getMatchB().toString());
        assertEquals(3, node.getScore());
    }

    @Test
    void test2() {
        String a = "gggabcde";
        String b = "abbde";
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 8);
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
        CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 6);
    }

    @Test
    void testStatic() {
        String a = "AGTCAGTA";
        String b = "ATCAGTAC";
        EditDistanceNode node = CustomAlgorithm.computeEditDistance(a, b, -3, -1, 1, 14);

        // EditDistanceNode node = naiveAlgorithm.computeEditDistance();
        // System.out.println("Score = " + node.getScore());
    }

}
