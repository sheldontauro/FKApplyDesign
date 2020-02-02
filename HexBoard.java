package javaDesign;

import java.util.*;

public class HexBoard implements BoardInterface {

	private int diagReq, distCenter;
	private int boardHeight, boardWidth;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState;
	private int totalCells, moveCount;
	private ArrayList<Integer> prevXcord, prevYcord;

	public HexBoard(int distCenter) {
		diagReq = 2*distCenter - 1;
		this.distCenter = distCenter;
		this.boardWidth = diagReq;
		this.boardHeight = diagReq;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
		totalCells = getTotalCells();
		prevXcord = new ArrayList<Integer>();
		prevYcord = new ArrayList<Integer>();
		moveCount = 0;
	}

	private int getTotalCells() {
		int ret = 0;
		for(int i = 1; i < distCenter ; i++) {
			ret += i * 2;
		}
		ret = diagReq * diagReq - ret;
		return ret;
	}

	public boolean moveIsValid(int xcord, int ycord) {
		if(Math.min(xcord, ycord) < 0 || xcord >= boardHeight || ycord >= boardWidth) {
			return false;
		}

		if(ycord >= distCenter + Math.min(xcord, diagReq - xcord - 1)) {
			return false;
		}

		if( boardState.containsKey(getCoordinates(xcord, ycord)) ) {
			return false;
		}
		return true;
	}

	public void displayBoard() {
		
		for(int i = 0; i < diagReq; i++) {

			for(int k = 0; k < Math.abs(i - diagReq / 2); k++) {
				System.out.print("    ");		
			}

			for(int j = 0; j < Math.min(i, diagReq - i - 1) + distCenter; j++) {
				int coord = getCoordinates(i, j);
				
				if( boardState.containsKey(coord) ) {
					System.out.print("   " + letters.get(boardState.get(coord)) + "   ");
				}
				else {
					System.out.print("   __   ");
				}
			}
			System.out.println();

		}

	}

	public ArrayList<Integer> analyseBoard() {
		ArrayList<Integer> results = new ArrayList<>();

		if( !boardState.containsKey(getCoordinates(distCenter - 1, distCenter - 1)) ) {
			results.add(-1);
			return results;
		}
		int playerIndex = boardState.get(getCoordinates(distCenter - 1, distCenter - 1));

		//checking horizontal
		Set <Integer> se = new HashSet<>();
		for(int k = 0; k < diagReq; k++) {
			if(!boardState.containsKey(getCoordinates(distCenter - 1, k))) {
				se.add(-1);
				se.add(-2);
				break;
			}
			se.add(boardState.get(getCoordinates(distCenter - 1, k)));
		}
		if(se.size() == 1) {
			results.add(2);
			results.add(playerIndex);
			return results;
		}

		//checking forward diagonal
		se = new HashSet<>();
		int tempCt = 0;
		for(int i = 0; i < diagReq; i++) {
			
			if( !boardState.containsKey(getCoordinates(i, tempCt)) ) {
				se.add(-1);
				se.add(-2);
				break;
			}
			se.add(boardState.get(getCoordinates(i, tempCt)));


			if(i < diagReq / 2) {
				tempCt++;
			}
		}
		if(se.size() == 1) {
			results.add(2);
			results.add(playerIndex);
			return results;
		}

		//checking backward diagonal
		se = new HashSet<>();
		tempCt = 0;
		for(int i = 0; i < diagReq; i++) {
			
			if( !boardState.containsKey(getCoordinates(diagReq - i - 1, tempCt)) ) {
				se.add(-1);
				se.add(-2);
				break;
			}
			se.add(boardState.get(getCoordinates(diagReq - i - 1, tempCt)));


			if(i < diagReq / 2) {
				tempCt++;
			}
		}
		if(se.size() == 1) {
			results.add(2);
			results.add(playerIndex);
			return results;
		}

		if(noMovesLeft()) {
			results.add(1);
		}
		else {
			results.add(-1);
		}

		return results;
	}

	public boolean noMovesLeft() {
		return ( boardState.size() == totalCells );
	}

	public int updateBoard(String operation, int xcord, int ycord, int playerNum) {
		int coordinates = getCoordinates(xcord, ycord);
		if(operation.toLowerCase().equals("set")) {
			boardState.put(coordinates, playerNum);
			moveCount++;
			prevXcord.add(xcord);
			prevYcord.add(ycord);
			return 1;
		}
		if(operation.toLowerCase().equals("unset")) {
			if(moveCount == 0) {
				System.out.println("No moves to undo!!");
				return -1;
			}
			coordinates = getCoordinates(prevXcord.get(moveCount - 1), prevYcord.get(moveCount - 1));
			prevXcord.remove(moveCount - 1);
			prevYcord.remove(moveCount - 1);
			moveCount--;
			boardState.remove(coordinates);
			return 1;
		}
		return 0;
	}

	public int getHeight() {
		return boardHeight;
	}

	public int getWidth() {
		return boardWidth;
	}

	private int getCoordinates(int xcord, int ycord) {
		return xcord * diagReq + ycord;
	}
}