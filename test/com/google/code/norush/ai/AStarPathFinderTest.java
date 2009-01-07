package com.google.code.norush.ai;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for AStarPathFinder.
 * @author DL, GT
 */
public class AStarPathFinderTest {
    
    private class IntegerMove implements Move {
        public IntegerMove(int jump) {
            this.jump = jump;
        }
        public final int jump;
    }
    
    private class IntegerState implements State {
        public IntegerState(int value) {
            this.value = value;
        }
        public List<Move> getMoves() {
            List<Move> moves = new ArrayList<Move>();
            if (value < 10) {
                moves.add(new IntegerMove(2));
            }
            if (value > -10) {
                moves.add(new IntegerMove(-2));
            }
            return moves;
        }
        public State applyMove(Move move) {
            IntegerMove integerMove = (IntegerMove) move;
            return new IntegerState(value + integerMove.jump);
        }
        public boolean isGoal() {
            return (value == 0);
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof IntegerState)) {
                return false;
            }
            IntegerState otherState = (IntegerState)other;
            return (value == otherState.value);
        }

        @Override
        public int hashCode() {
            return value;
        }
        private final int value;
    }
    
    private class IntegerHeuristic implements AStarHeuristic {
        public IntegerHeuristic() {}
        public int distanceToGoal(State state) {
            IntegerState integerState = (IntegerState) state;
            return Math.abs(integerState.value);
        }
    }

    public AStarPathFinderTest() {}
    
    @Test
    public void testStartStateIsGoal() {
        IntegerState state = new IntegerState(0);
        List<Move> solution = aStar.solve(state).solution;
        assertNotNull(solution);
        assertEquals(0, solution.size());
    }
    
    @Test
    public void testImpossiblePathFinding() {
        IntegerState state = new IntegerState(9);
        List<Move> solution = aStar.solve(state).solution;
        assertNull(solution);
    }

    @Test
    public void testPossiblePathFinding() {
        IntegerState state = new IntegerState(10);
        List<Move> solution = aStar.solve(state).solution;
        assertNotNull(solution);
        assertEquals(5, solution.size());
        for (Move move : solution) {
            IntegerMove integerMove = (IntegerMove) move;
            assertEquals(-2, integerMove.jump);
        }
    }
    
    private AStarPathFinder aStar = new AStarPathFinder(new IntegerHeuristic());
}