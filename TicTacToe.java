package javaDesign;

import java.util.*;


public class TicTacToe {

	Scanner sc;
	TicTacToe() {
		sc = new Scanner(System.in);
	}

	public static void main(String[] args) {
		
		ArrayList<Players> playerList = new ArrayList<>();
		Scanner in = new Scanner(System.in);

		int players = 2, count = 0;

		while(count < players) {
			count = count + 1;
			System.out.println("Enter the name of player " + count);
			String tempName = in.next();
			Players createdPlayer = new Players(tempName);
			playerList.add(createdPlayer);
		}

		TicTacToeBoard board = new TicTacToeBoard();

		TicTacGame gm = new TicTacGame(playerList, board);
		gm.startGame();


	}
}

interface PlayerInterface {
	ArrayList<Integer> doMove();
}

interface GameInterface {
	void startGame();
	void endGame();
	void declareResults();
}

interface BoardInterface {
	// void validMoves();
	boolean moveIsValid(int xcord, int ycord);
	void displayBoard();
	ArrayList<Integer> analyseBoard();
}


class TicTacGame implements GameInterface {
	
	private ArrayList<Players> participants;
	private TicTacToeBoard board;

	TicTacGame(ArrayList<Players> participants, TicTacToeBoard board) {
		this.participants = participants;
		this.board = board;
	}

	public void startGame() {
		int currIndex = 0;
		this.board.displayBoard();
		while( !board.noMovesLeft() && board.analyseBoard().get(0) == -1 ) {
			Players currPlayer = participants.get(currIndex);
			System.out.println(currPlayer.getName() + " move");

			ArrayList<Integer> currMove = currPlayer.doMove();
			while( !board.moveIsValid(currMove.get(0), currMove.get(1)) ) {
				System.out.println("Enter a valid move!!");
				currMove = currPlayer.doMove();
			}

			board.updateBoard("set", currMove.get(0), currMove.get(1), currIndex);
			this.board.displayBoard();
			currIndex = (currIndex + 1) % participants.size();
		}
		this.endGame();
	}

	public void endGame() {
		ArrayList<Integer> gameResults = this.board.analyseBoard();

		if(gameResults.get(0) == 1) {
			System.out.println("It's a draw!!");
		}
		else {
			String winnerName = participants.get(gameResults.get(1)).getName();
			System.out.println(winnerName + "You Won!!");
		}
	}

	public void declareResults() {
		System.out.println("We won, it's done");
	}
}

class Players implements PlayerInterface {
	
	private String name;
	private int playerId;
	private static int id = 1;

	Players(String name) {
		this.name = name;
		this.playerId = id;
		id = id + 1;
	}

	public String getName() {
		return this.name;
	}


	//Using ArrayList<T> instead of Pair<Integer, Integer> because javafx.util package is not present in my system
	public ArrayList<Integer> doMove() {
		ArrayList<Integer> moves;
		TicTacToe tic = new TicTacToe();
		
		int xcord = tic.sc.nextInt();
		int ycord = tic.sc.nextInt();
		moves = new ArrayList<>(List.of(xcord, ycord));
		
		return moves;
	}
}


class TicTacToeBoard implements BoardInterface {
	private int boardHeight, boardWidth, diagonalReq;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState; 

	TicTacToeBoard() {
		boardHeight = 3;
		boardWidth = 3;
		diagonalReq = 3;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
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
					results.add(-1);
					return results;
				}

				int playerIndex = boardState.get(getCoordinates(i, j));
				//checking horizontal
				if(j + diagonalReq - 1 < boardWidth) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i, j + k))) {
							se.add(-1);
							se.add(0);
							break;
						}
						int tempCoord = boardState.get(getCoordinates(i, j + k));
						se.add(tempCoord);
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
					}
				}

				//checking vertical
				if(i + diagonalReq - 1 < boardHeight) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j))) {
							se.add(-1);
							se.add(0);
							break;
						}
						int tempCoord = boardState.get(getCoordinates(i + k, j));
						se.add(tempCoord);
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
					}
				}

				//checking diagonal 
				if(j + diagonalReq - 1 < boardWidth && i + diagonalReq - 1 < boardHeight) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j + k))) {
							se.add(-1);
							se.add(0);
							break;
						}
						int tempCoord = boardState.get(getCoordinates(i + k, j + k));
						se.add(tempCoord);
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
					}
				}

				//checking diagonal backwards
				if(j - diagonalReq + 1 >= 0 && i + diagonalReq - 1 < boardHeight ) {
					Set <Integer> se = new HashSet<>();
					for(int k = 0; k < diagonalReq; k++) {
						if(!boardState.containsKey(getCoordinates(i + k, j - k))) {
							se.add(-1);
							se.add(0);
							break;
						}
						int tempCoord = boardState.get(getCoordinates(i + k, j - k));
						se.add(tempCoord);
					}
					if(se.size() == 1) {
						results.add(2);
						results.add(playerIndex);
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

	public void updateBoard(String operation, int xcord, int ycord, int playerNum) {
		int coordinates = getCoordinates(xcord, ycord);
		if(operation.toLowerCase().equals("set")) {
			boardState.put(coordinates, playerNum);
		}
	}


	private int getCoordinates(int xcord, int ycord) {
		return xcord * boardWidth + ycord;
	}
}

