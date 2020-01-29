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

		//starting a game
		System.out.println("Press 0 for HexGame\nPress 1 for TicTacToe Game\nPress 2 for Connect Four Game");
		int decide = in.nextInt();
		int diagSize, recSize;
		System.out.println("Enter board Size and Recursive Board Size");
		diagSize = in.nextInt();
		recSize = in.nextInt();
		while(decide == 2 && diagSize != recSize) {
			System.out.println("board Size and Game Size should be same!!, please re-enter inputs");
			diagSize = in.nextInt();
			recSize = in.nextInt();
		}
		MetaTicGame gm = new MetaTicGame(diagSize, recSize, playerList, decide);
		gm.addPlayers();
		
		while(true) {
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
	int getHeight();
	int getWidth();
	int updateBoard(String operation, int xcord, int ycord, int playerNum);
	boolean noMovesLeft();
}

class MetaTicGame {

	private int bSize;
	private int recSize;
	private ArrayList<Players> participants;
	private int decide;

	MetaTicGame(int bSize, int recSize, ArrayList <Players> participants, int decide) {
		this.bSize = bSize;
		this.recSize = recSize;
		this.participants = participants;
		this.decide = decide;
	}

	//decide == 2 indicates Connect Four Board
	//decide == 1 indicates TicTacToeBoard
	//decide == 0 indicates Hex Board
	void addPlayers() {

		TicTacToe tic = new TicTacToe();
		int players = 2, count = 0;
		HashMap<String, Integer> storeNames = new HashMap<>();

		while(count < players) {
			count = count + 1;
			System.out.println("Enter the name of player " + count);
			String playerName = tic.sc.next();
			if(!storeNames.containsKey(playerName)) {
				Players createdPlayer;
				if(decide == 1) {
					createdPlayer = new Players(playerName, new TicTacToeBoard(bSize) );
				}
				else if(decide == 2) {
					createdPlayer = new Players(playerName, new ConnectFour() );
				}
				else {
					createdPlayer = new Players(playerName, new HexBoard(bSize) );
				}
				participants.add(createdPlayer);
				storeNames.put(playerName, 0);
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
			TicTacGame gm;
			if(decide == 1) {
				gm = new TicTacGame(participants, new TicTacToeBoard(bSize));
			}
			else if(decide == 2) {
				gm = new TicTacGame(participants, new ConnectFour(bSize));
			}
			else {
				gm = new TicTacGame(participants, new HexBoard(bSize));
			}
			return gm.startGame();
		}
		else {
			BoardInterface recBoard;
			if(decide == 1) {
				recBoard = new TicTacToeBoard(bSize);
			}
			else if(decide == 2) {
				recBoard = new ConnectFour(bSize);
			}
			else {
				recBoard = new HexBoard(bSize);
			}
			for(int i = 0; i < bSize; i++) {
				for(int j = 0; j < bSize; j++) {
					int retResult = playRecGame(recSize / bSize);
					System.out.println("Print retResult " + retResult);
					if(retResult >= 0) {
						
						recBoard.updateBoard("set", i, j, retResult);
						System.out.println(i + " " + j + " " + retResult);
						ArrayList<Integer> nestedBoardResults = recBoard.analyseBoard();

						if(nestedBoardResults.get(0) == 2) {
							return nestedBoardResults.get(1);
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
	private BoardInterface board;

	TicTacGame(ArrayList<Players> participants, ConnectFour board) {
		this.participants = participants;
		this.board = board;
	}

	TicTacGame(ArrayList<Players> participants, TicTacToeBoard board) {
		this.participants = participants;
		this.board = board;
	}

	TicTacGame(ArrayList<Players> participants, HexBoard board) {
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

			//handle undo
			if(currMove.get(0) == -1) {
				int outcome = board.updateBoard("unset", 0, 0, 0);
				if(outcome >= 0) {
					currIndex = (currIndex - 1 + participants.size()) % participants.size();
					System.out.println("currIndex " + currIndex);
				}
				continue;
			}

			//check for valid move
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
	private BoardInterface board;
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

	Players(String name, HexBoard board) {
		this.name = name;
		if(name.equals("machine")) {
			isMachine = true;
		}
		this.playerId = id;
		id = id + 1;
		this.board = board;
		this.performanceScore = 0;
	}

	Players(String name, ConnectFour board) {
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
						se.add(boardState.get(getCoordinates(i, j + k)));
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
						se.add(boardState.get(getCoordinates(i + k, j - k)));
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


class HexBoard implements BoardInterface {

	private int diagReq, distCenter;
	private int boardHeight, boardWidth;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState;
	private int totalCells, moveCount;
	private ArrayList<Integer> prevXcord, prevYcord;

	HexBoard(int distCenter) {
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


class ConnectFour implements BoardInterface {
	private int boardHeight, boardWidth, diagonalReq;
	private ArrayList<String> letters;
	private HashMap<Integer, Integer> boardState;
	private int moveCount;
	private ArrayList<Integer> prevXcord, prevYcord;

	ConnectFour() {
		boardHeight = 4;
		boardWidth = 4;
		diagonalReq = 4;
		moveCount = 0;
		letters = new ArrayList<String>(List.of("X", "0"));
		boardState = new HashMap<>();
		prevXcord = new ArrayList<Integer>();
		prevYcord = new ArrayList<Integer>();
	}

	ConnectFour(int bSize) {
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
