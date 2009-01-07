package com.google.code.norush.heuristics;

import com.google.code.norush.RushHourBoard;
import com.google.code.norush.RushHourPuzzles;
import com.google.code.norush.ai.AStarPathFinder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for com.google.code.norush.heuristics classes.
 * @author DL, GT
 */
public class HeuristicsTest {
    public HeuristicsTest() {}
    
    @Test
    public void testNullHeuristic() {
        AStarPathFinder aStar = new AStarPathFinder(new NullHeuristic());
        for (int i = 0; i < RushHourPuzzles.Puzzles.length; ++i) {
            RushHourBoard board = new RushHourBoard(RushHourPuzzles.Puzzles[i]);
            assertEquals(RushHourPuzzles.SolutionsLengths[i],
                aStar.solve(board).size());
        }
    }
    
    @Test
    public void testBlockingVehiclesHeuristic() {
        AStarPathFinder aStar = new AStarPathFinder(new BlockingVehiclesHeuristic());
        for (int i = 0; i < RushHourPuzzles.Puzzles.length; ++i) {
            RushHourBoard board = new RushHourBoard(RushHourPuzzles.Puzzles[i]);
            assertEquals(RushHourPuzzles.SolutionsLengths[i],
                aStar.solve(board).size());
        }
    }
}