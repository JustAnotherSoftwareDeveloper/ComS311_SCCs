package proj2;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * @author nkarasch
 */
public class GraphProcessorTest {
    private GraphProcessor g;

    @Before
    public void setUp() {
            g = new GraphProcessor("test/_GraphProcessorTest_data.txt");

    }
    @Test
    public void outDegree() {
        // Valid vertices
        assertEquals(0, g.outDegree("Y"));
        assertEquals(1, g.outDegree("A"));
        assertEquals(2, g.outDegree("G"));
        assertEquals(4, g.outDegree("L"));
        assertEquals(1, g.outDegree("U"));

        // Invalid vertex
        assertEquals(-1, g.outDegree("Pork rind? Pork rind."));
    }

    @Test
    public void sameComponent() {
        // Valid vertices
        // In the same component
        assertTrue(g.sameComponent("A", "F"));
        assertTrue(g.sameComponent("Y", "Y"));
        assertTrue(g.sameComponent("M", "K"));
        assertTrue(g.sameComponent("U", "V"));
        assertTrue(g.sameComponent("I", "P"));
        // NOT in the same component
        assertFalse(g.sameComponent("A", "J"));
        assertFalse(g.sameComponent("Y", "A"));
        assertFalse(g.sameComponent("X", "V"));
        assertFalse(g.sameComponent("N", "P"));
        assertFalse(g.sameComponent("C", "U"));

        // Invalid vertices
        assertFalse(g.sameComponent("U", "Ah, Perry the platypus, Your timing is uncanny."));
        assertFalse(g.sameComponent("And by uncanny, of course, I mean... COMPLETELY CANNY!", "C"));
        assertFalse(g.sameComponent("Aren't you a little young to build a rollercoaster?", "Why yes, yes I am."));
    }

    @Test
    public void componentVertices() {
        String[] actual;

        // Valid vertices
        String[] expected1 = {"A", "B", "C", "D", "E", "F", "G", "H", "Z"};
        actual = g.componentVertices("A").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected1, actual);

        String[] expected2 = {"Y"};
        actual = g.componentVertices("Y").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected2, actual);

        String[] expected3 = {"I", "J", "O", "P", "Q", "R", "S"};
        actual = g.componentVertices("I").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected3, actual);

        String[] expected4 = {"K", "L", "M", "W", "X"};
        actual = g.componentVertices("L").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected4, actual);

        String[] expected5 = {"N", "T"};
        actual = g.componentVertices("N").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected5, actual);

        String[] expected6 = {"U", "V"};
        actual = g.componentVertices("U").toArray(new String[0]);
        Arrays.sort(actual);
        assertArrayEquals(expected6, actual);

        // Invalid vertex
        String[] emptyList = {};
        actual = g.componentVertices("It's a trick. Get an axe.").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
    }

    @Test
    public void largestComponent() {
        assertEquals(9, g.largestComponent());
    }

    @Test
    public void numComponents() {
        assertEquals(6, g.numComponents());
    }

    @Test
    public void bfsPath() {
        String[] actual;

        // Valid vertices with a path
        String[] expected1 = {"A", "D", "C", "Z", "E", "F", "G", "J", "I",
                "Q", "R", "S", "T", "N"};
        actual = g.bfsPath("A", "N").toArray(new String[0]);
        assertArrayEquals(expected1, actual);

        String[] expected2 = {"S", "O", "J", "P"};
        actual = g.bfsPath("S", "P").toArray(new String[0]);
        assertArrayEquals(expected2, actual);

        String[] expected3 = {"N", "T"};
        actual = g.bfsPath("N", "T").toArray(new String[0]);
        assertArrayEquals(expected3, actual);

        String[] expected4 = {"U", "V"};
        actual = g.bfsPath("U", "V").toArray(new String[0]);
        assertArrayEquals(expected4, actual);

        String[] expected6 = {"K", "W", "L", "M"};
        actual = g.bfsPath("K", "M").toArray(new String[0]);
        assertArrayEquals(expected6, actual);

        String[] expected7 = {"K", "W", "L", "X", "Y"};
        actual = g.bfsPath("K", "Y").toArray(new String[0]);
        assertArrayEquals(expected7, actual);

        String[] emptyList = {};

        // No path between these vertices
        actual = g.bfsPath("A", "X").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("X", "A").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("V", "W").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("W", "V").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("L", "P").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);

        // Path of length zero for a vertex going to itself
        actual = g.bfsPath("A", "A").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);

        // Invalid vertices
        actual = g.bfsPath("A", "You? What did you do?").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("All you did was hang around and eat our eggs.", "A").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
        actual = g.bfsPath("I expected something more grand, less...", "...fuzzy.").toArray(new String[0]);
        assertArrayEquals(emptyList, actual);
    }

}