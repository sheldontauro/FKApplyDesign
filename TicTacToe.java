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


