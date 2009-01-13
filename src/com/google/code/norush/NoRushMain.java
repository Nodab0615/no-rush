package com.google.code.norush;

import com.google.code.norush.ai.AStarPathFinder;
import com.google.code.norush.ai.Move;
import com.google.code.norush.heuristics.BlockingVehiclesHeuristic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Main class for solving RushHour boards from the command-line.
 * 
 * @author DL, GT
 */
public class NoRushMain {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: NoRushMain <input file> <output file>");
            System.out.println("input is textual, each line being an encoded board.");
            return;
        }
        
        AStarPathFinder aStar = new AStarPathFinder(
            new BlockingVehiclesHeuristic());
        
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
            try {
                String boardStr;
                while ((boardStr = br.readLine()) != null) {
                    RushHourBoard board = new RushHourBoard(boardStr);
                    long before = System.currentTimeMillis();
                    AStarPathFinder.AStarResult result = aStar.solve(board);
                    long after = System.currentTimeMillis();
                    
                    bw.append(
                        "time (ms): " + (after-before) + 
                        ", states: " + result.states +
                        ", uniques: " + result.uniqueStates +
                        ", ebf: " + result.ebf +
                        ", sol:");
                    if (result.solution == null) {
                        bw.append(" N/A");
                    } else if (result.solution.size() == 0) {
                        bw.append(" already solved.");
                    } else {
                        for (Move move : result.solution) {
                            bw.append(" " + move.toString());
                        }
                    }
                    bw.newLine();
                }
            } finally {
                bw.close();
            }
        } finally {
            br.close();
        }
    }
}
