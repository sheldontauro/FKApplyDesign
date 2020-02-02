package javaDesign;

import java.util.*;

public class TicTacGame implements GameInterface {
	
	private ArrayList<Players> participants;
	private BoardInterface board;

	public TicTacGame(ArrayList<Players> participants, ConnectFour board) {
		this.participants = participants;
		this.board = board;
	}

	public TicTacGame(ArrayList<Players> participants, TicTacToeBoard board) {
		this.participants = participants;
		this.board = board;
	}

	public TicTacGame(ArrayList<Players> participants, HexBoard board) {
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