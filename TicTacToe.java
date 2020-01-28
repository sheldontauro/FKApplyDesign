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

		// int players = 2, count = 0;
		// HashMap<String, Integer> storeNames = new HashMap<>();

		// while(count < players) {
		// 	count = count + 1;
		// 	System.out.println("Enter the name of player " + count);
		// 	String tempName = in.next();
		// 	if(!storeNames.containsKey(tempName)) {
		// 		Players createdPlayer = new Players(tempName, new TicTacToeBoard());
		// 		playerList.add(createdPlayer);
		// 		storeNames.put(tempName, 0);
		// 	}
		// 	else {
		// 		count = count - 1;
		// 	}
		// }


		//starting a game
		int diagSize, recSize;
		System.out.println("Enter board Size and Game Size");
		diagSize = in.nextInt();
		recSize = in.nextInt();
		MetaTicGame gm = new MetaTicGame(diagSize, recSize, playerList);
		gm.addPlayers();
		int finalResults = gm.playRecGame(recSize);
		if(finalResults == -1) {
			System.out.println("It's a draw");
			gm.updateScoreBoard(diagSize);
		}
		else {
			System.out.println("Winner is player " + gm.getPlayerName(finalResults));
			gm.updateScoreBoard(finalResults, recSize);
		}
		gm.displayScoreBoard();
	}
}

interface PlayerInterface {
	ArrayList<Integer> doMove();
}

interface GameInterface {
	int startGame();
	int endGame();
}

interface BoardInterface {
	boolean moveIsValid(int xcord, int ycord);
	void displayBoard();
	ArrayList<Integer> analyseBoard();
}

class MetaTicGame {

	private int bSize;
	private int recSize;
	private ArrayList<Players> participants;

	MetaTicGame(int bSize, int recSize, ArrayList <Players> participants) {
		this.bSize = bSize;
		this.recSize = recSize;
		this.participants = participants;
	}

	void addPlayers() {

		TicTacToe tic = new TicTacToe();
		int players = 2, count = 0;
		HashMap<String, Integer> storeNames = new HashMap<>();

		while(count < players) {
			count = count + 1;
			System.out.println("Enter the name of player " + count);
			String tempName = tic.sc.next();
			if(!storeNames.containsKey(tempName)) {
				Players createdPlayer = new Players(tempName, new TicTacToeBoard(bSize));
				participants.add(createdPlayer);
				storeNames.put(tempName, 0);
			}
			else {
				count = count - 1;
			}
		}
	}

	public String getPlayerName(int index) {
		return participants.get(index).getName();
	}

	int playRecGame(int recSize) {
		if(recSize == bSize) {
			TicTacGame gm = new TicTacGame(participants, new TicTacToeBoard(bSize));
			return gm.startGame();
		}
		else {
			
			TicTacToeBoard recBoard = new TicTacToeBoard(bSize);
			for(int i = 0; i < bSize; i++) {
				for(int j = 0; j < bSize; j++) {
					int retResult = playRecGame(recSize / bSize);
					System.out.println("Print retResult " + retResult);
					if(retResult >= 0) {
						recBoard.updateBoard("set", i, j, retResult);
						System.out.println(i + " " + j + " " + retResult);
						ArrayList<Integer> tempResults = recBoard.analyseBoard();
						if(tempResults.get(0) == 2) {
							return tempResults.get(1);
						}
					}
				}
			}

			ArrayList<Integer> results = recBoard.analyseBoard();
			if(results.get(0) == -1 || results.get(0) == 1) {
				return -1;
			}
			else {
				return results.get(1);
			}
		}
	}

	void updateScoreBoard(int index, int value) {
		participants.get(index).updateScore(value);
	}

	void updateScoreBoard(int value) {
		for(Players person : participants) {
			person.updateScore(value);
		}
	}

	void displayScoreBoard() {
		for(Players person : participants) {
			System.out.println("Score for " + person.getName() + " is " + person.getScore());
		}
	}
}

class TicTacGame implements GameInterface {
	
	private ArrayList<Players> participants;
	private TicTacToeBoard board;

	TicTacGame(ArrayList<Players> participants, TicTacToeBoard board) {
		this.participants = participants;
		this.board = board;
	}

	public int startGame() {
		int currIndex = 0;
		this.board.displayBoard();
		while( !board.noMovesLeft() && board.analyseBoard().get(0) == -1 ) {
			Players currPlayer = participants.get(currIndex);
			System.out.println(currPlayer.getName() + " move");

			ArrayList<Integer> currMove = currPlayer.doMove();
			if(currMove.get(0) == -1) {
				int outcome = board.updateBoard("unset", 0, 0, 0);
				if(outcome >= 0) {
					currIndex = (currIndex - 1 + participants.size()) % participants.size();
					System.out.println("currIndex " + currIndex);
				}
				continue;
			}
			while( !board.moveIsValid(currMove.get(0), currMove.get(1)) ) {
				System.out.println("Enter a valid move!!");
				currMove = currPlayer.doMove();
			}

			board.updateBoard("set", currMove.get(0), currMove.get(1), currIndex);
			this.board.displayBoard();
			currIndex = (currIndex + 1) % participants.size();
		}
		return this.endGame();
	}

	public int endGame() {
		ArrayList<Integer> gameResults = this.board.analyseBoard();

		if(gameResults.get(0) == 1) {
			System.out.println("It's a draw!!");
			return -1;
		}
		else {
			String winnerName = participants.get(gameResults.get(1)).getName();
			System.out.println(winnerName + " You Won!!");
			return gameResults.get(1);
		}
	}
}

class Players implements PlayerInterface {
	
	private String name;
	private int playerId;
	private static int id = 1;
	private boolean isMachine;
	private TicTacToeBoard board;
	private int performanceScore;

	Players(String name, TicTacToeBoard board) {
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


class TicTacToeBoard implements BoardInterface {
	private int boardHeight, boardWidth, diagonalReq;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState;
	private int moveCount;
	private ArrayList<Integer> prevXcord, prevYcord;

	TicTacToeBoard() {
		boardHeight = 3;
		boardWidth = 3;
		diagonalReq = 3;
		moveCount = 0;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
		prevXcord = new ArrayList<Integer>();
		prevYcord = new ArrayList<Integer>();
	}

	TicTacToeBoard(int bSize) {
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
		// System.out.println();
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
						int tempCoord = boardState.get(getCoordinates(i, j + k));
						se.add(tempCoord);
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
						int tempCoord = boardState.get(getCoordinates(i + k, j));
						se.add(tempCoord);
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
						int tempCoord = boardState.get(getCoordinates(i + k, j + k));
						se.add(tempCoord);
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
						int tempCoord = boardState.get(getCoordinates(i + k, j - k));
						se.add(tempCoord);
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
			// System.out.println("Previous " + xcord + " " + ycord);
			prevXcord.add(xcord);
			prevYcord.add(ycord);
			return 1;
		}
		if(operation.toLowerCase().equals("unset")) {
			if(moveCount == 0) {
				System.out.println("No moves to undo!!");
				return -1;
			}
			// int prevXcord = this.prevXcord;
			// int prevYcord = this.prevYcord;
			coordinates = getCoordinates(prevXcord.get(moveCount - 1), prevYcord.get(moveCount - 1));
			prevXcord.remove(moveCount - 1);
			prevYcord.remove(moveCount - 1);
			moveCount--;
			// System.out.println("Coordinates " + coordinates);
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

