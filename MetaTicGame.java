
package javaDesign;

import java.util.*;


public class MetaTicGame {

	private int bSize;
	private int recSize;
	private ArrayList<Players> participants;
	private int decide;

	public MetaTicGame(int bSize, int recSize, ArrayList <Players> participants, int decide) {
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

	public int playRecGame(int recSize) {
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

	public void updateScoreBoard(int index, int value) {
		participants.get(index).updateScore(value);
	}

	public void updateScoreBoard(int value) {
		for(Players person : participants) {
			person.updateScore(value);
		}
	}

	public void displayScoreBoard() {
		for(Players person : participants) {
			System.out.println("Score for " + person.getName() + " is " + person.getScore());
		}
	}
}