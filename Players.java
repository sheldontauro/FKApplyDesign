package javaDesign;

import java.util.*;

public class Players implements PlayerInterface {
	
	private String name;
	private int playerId;
	private static int id = 1;
	private boolean isMachine;
	private BoardInterface board;
	private int performanceScore;

	public Players(String name, TicTacToeBoard board) {
		this.name = name;
		if(name.equals("machine")) {
			isMachine = true;
		}
		this.playerId = id;
		id = id + 1;
		this.board = board;
		this.performanceScore = 0;
	}

	public Players(String name, HexBoard board) {
		this.name = name;
		if(name.equals("machine")) {
			isMachine = true;
		}
		this.playerId = id;
		id = id + 1;
		this.board = board;
		this.performanceScore = 0;
	}

	public Players(String name, ConnectFour board) {
		this.name = name;
		if(name.equals("machine")) {
			isMachine = true;
		}
		this.playerId = id;
		id = id + 1;
		this.board = board;
		this.performanceScore = 0;
	}

	public String getName() {
		return this.name;
	}

	public void updateScore(int value) {
		performanceScore = performanceScore + value;
	}

	public int getScore() {
		return performanceScore;
	}
 

	//Using ArrayList<T> instead of Pair<Integer, Integer> because javafx.util package is not present in my system
	public ArrayList<Integer> doMove() {
		ArrayList<Integer> moves;
		TicTacToe tic = new TicTacToe();

		if(isMachine) {
			Random r = new Random();
				int xcord = r.nextInt(board.getHeight());
				int ycord = r.nextInt(board.getWidth());
			while(!board.moveIsValid(xcord, ycord)) {
				xcord = r.nextInt(board.getHeight());
				ycord = r.nextInt(board.getWidth());//
			}
			moves = new ArrayList<>(List.of(xcord, ycord));
		
			return moves;
		}
		
		int xcord = tic.sc.nextInt();
		if(xcord == -1) {
			System.out.println("Player wants to undo move");
			return new ArrayList<>(List.of(xcord));
		}
		int ycord = tic.sc.nextInt();
		moves = new ArrayList<>(List.of(xcord, ycord));
		
		return moves;
	}
}