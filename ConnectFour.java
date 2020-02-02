package javaDesign;

import java.util.*;

public class ConnectFour implements BoardInterface {
	private int boardHeight, boardWidth, diagonalReq;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState;
	private int moveCount;
	private ArrayList<Integer> prevXcord, prevYcord;

	public ConnectFour() {
		boardHeight = 4;
		boardWidth = 4;
		diagonalReq = 4;
		moveCount = 0;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
		prevXcord = new ArrayList<Integer>();
		prevYcord = new ArrayList<Integer>();
	}

	public ConnectFour(int bSize) {
		boardHeight = bSize;
		boardWidth = bSize;
		diagonalReq = bSize;
		moveCount = 0;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
		prevXcord = new ArrayList<Integer>();
		prevYcord = new ArrayList<Integer>();
	}

	public void displayBoard() {
		
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
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

	public boolean moveIsValid(int xcord, int ycord) {
		if(Math.min(xcord, ycord) < 0 || xcord >= boardHeight || ycord >= boardWidth) {
			return false;
		}
		if( boardState.containsKey(getCoordinates(xcord, ycord)) ) {
			return false;
		}
		if(xcord == boardHeight - 1) {
			return true;
		}
		if( !boardState.containsKey(getCoordinates(xcord + 1, ycord)) ) {
			return false;
		}

		return true;
	}

	// result of 2 indicates win
	// result of 1 indicates draw
	// result of 0 indicates loss
	// result of -1 in the first cell indicates games is not decided yet
	public ArrayList<Integer> analyseBoard() {
		ArrayList<Integer> results = new ArrayList<>();
		
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				if(!boardState.containsKey(getCoordinates(i, j))) {
					continue;
				}

				int playerIndex = boardState.get(getCoordinates(i, j));
				//checking horizontal
				if(j + diagonalReq - 1 < boardWidth) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i, j + k))) {
							se.add(-1);
							se.add(-2);
							break;
						}
						se.add((int)boardState.get(getCoordinates(i, j + k)));
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
						return results;
					}
				}

				//checking vertical
				if(i + diagonalReq - 1 < boardHeight) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j))) {
							se.add(-1);
							se.add(-2);
							break;
						}
						se.add(boardState.get(getCoordinates(i + k, j)));
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
						return results;
					}
				}

				//checking diagonal 
				if(j + diagonalReq - 1 < boardWidth && i + diagonalReq - 1 < boardHeight) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j + k))) {
							se.add(-1);
							se.add(-2);
							break;
						}
						se.add(boardState.get(getCoordinates(i + k, j + k)));
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
						return results;
					}
				}

				//checking diagonal backwards
				if(j - diagonalReq + 1 >= 0 && i + diagonalReq - 1 < boardHeight ) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j - k))) {
							se.add(-1);
							se.add(-2);
							break;
						}
						se.add((int)boardState.get(getCoordinates(i + k, j - k)));
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
						return results;
					}
				}
			}
		}

		if(!results.isEmpty()) {
			return results;
		}

		if(this.noMovesLeft()) {
			results.add(1);
		}
		else {
			results.add(-1);
		}
		return results;
	}

	public boolean noMovesLeft() {
		return ( boardState.size() == boardHeight * boardWidth );
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
		return xcord * boardWidth + ycord;
	}
}