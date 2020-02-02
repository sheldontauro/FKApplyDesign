package testJavaDesign;

import java.util.*;
import javaDesign.*;

public class UnitTest {
	public static void main(String[] args) {
		TicTacToeBoard ticTest = new TicTacToeBoard();

		System.out.println("Starting with verification of tic-tac-toe board");
		//verifying that default size of a tictactoe board = 3 X 3
		assert(ticTest.getHeight() == 3);
		assert(ticTest.getWidth() == 3);
		System.out.println("Test 1, 2 -> Verified length and width of the board");

		// verifying the move is valid function
		System.out.println("3 -> Starting test to verify move functionality");
		assert(ticTest.moveIsValid(0, 1) == true);
		assert(ticTest.moveIsValid(-1, -1) == false);
		assert(ticTest.moveIsValid(3, 3) == false);
		System.out.println("3 -> verified the functionality of moveIsValid function");

		//verifying whether a move is being set
		System.out.println("4 -> Starting test to verify move setting");
		ticTest.updateBoard("set", 0, 0, 0);
		assert(ticTest.moveIsValid(0, 0) == false);
		System.out.println("4 -> Verified that a move is being set");


		//verifying whether a move is being unset(undo)
		System.out.println("5 -> Starting undo test");
		ticTest.updateBoard("unset", 0, 0, 0);
		assert(ticTest.moveIsValid(0, 0) == true);
		System.out.println("5 -> Completing undo test");

		//verifying game logic
		System.out.println("6 -> Start verifying game logic for win(1)");
		ticTest = new TicTacToeBoard();
		ticTest.updateBoard("set", 0, 0, 0);
		ticTest.updateBoard("set", 0, 1, 0);
		ticTest.updateBoard("set", 0, 2, 0);
		ArrayList <Integer> arrResults = ticTest.analyseBoard();
		assert(arrResults.get(0) == 2 && arrResults.get(1) == 0); // indicating 0th player won
		System.out.println("6 -> Complete verifying game logic for win(1)");

		System.out.println("7 -> Start verifying game logic for win(2)");
		ticTest = new TicTacToeBoard();
		ticTest.updateBoard("set", 0, 0, 1);
		ticTest.updateBoard("set", 1, 1, 1);
		ticTest.updateBoard("set", 2, 2, 1);
		arrResults = ticTest.analyseBoard();
		assert(arrResults.get(0) == 2 && arrResults.get(1) == 1); // indicating 1st player won
		System.out.println("7 -> Complete verifying game logic for win(2)");

		System.out.println("8 -> Start verifying game logic for win(3)");
		ticTest = new TicTacToeBoard();
		ticTest.updateBoard("set", 0, 1, 1);
		ticTest.updateBoard("set", 1, 1, 1);
		ticTest.updateBoard("set", 2, 1, 1);
		arrResults = ticTest.analyseBoard();
		assert(arrResults.get(0) == 2 && arrResults.get(1) == 1); // indicating 1st player won
		System.out.println("8 -> Complete verifying game logic for win(3)");

		System.out.println("9 -> Start verifying game logic for draw");
		ticTest = new TicTacToeBoard();
		ticTest.updateBoard("set", 0, 0, 1);
		ticTest.updateBoard("set", 0, 1, 0);
		ticTest.updateBoard("set", 0, 2, 1);
		ticTest.updateBoard("set", 1, 0, 0);
		ticTest.updateBoard("set", 1, 1, 1);
		ticTest.updateBoard("set", 1, 2, 0);
		ticTest.updateBoard("set", 2, 0, 0);
		ticTest.updateBoard("set", 2, 1, 1);
		ticTest.updateBoard("set", 2, 2, 0);
		arrResults = ticTest.analyseBoard();
		assert(arrResults.get(0) == 1); // indicating a draw between 1st and 0th player
		System.out.println("9 -> Complete verifying game logic for draw");

		System.out.println("10 -> Start verifying game logic for displaying an incomplete game");
		ticTest = new TicTacToeBoard();
		ticTest.updateBoard("set", 0, 0, 1);
		ticTest.updateBoard("set", 0, 1, 0);
		ticTest.updateBoard("set", 0, 2, 1);
		ticTest.updateBoard("set", 1, 0, 0);
		ticTest.updateBoard("set", 1, 1, 1);
		ticTest.updateBoard("set", 1, 2, 0);
		ticTest.updateBoard("set", 2, 0, 0);
		ticTest.updateBoard("set", 2, 1, 1);
		arrResults = ticTest.analyseBoard();
		assert(arrResults.get(0) == -1); // indicating an incomplete game between 1st and 0th player
		System.out.println("10 -> Complete verifying game logic for displaying an incomplete game");


		//Creation of Players
		System.out.println("11 -> Start Creation of player and verify name");
		ticTest = new TicTacToeBoard();
		Players playTest = new Players("sheldon", ticTest);
		assert(playTest.getName().equals("sheldon"));
		System.out.println("11 -> Complete verifying name");

		System.out.println("12 -> Start Verify initial score");
		assert(playTest.getScore() == 0); 
		System.out.println("12 -> Complete Verify initial score");

		//Updating player score
		System.out.println("13 -> Start Updating player score from game engine");
		ArrayList<Players> participants = new ArrayList<>();
		Players playTest2 = new Players("mala", new TicTacToeBoard());
		participants.add(playTest);
		participants.add(playTest2);
		MetaTicGame metaTest = new MetaTicGame(3, 3, participants, 1);
		metaTest.updateScoreBoard(0, 5);
		assert(playTest.getScore() == 5);
		metaTest.updateScoreBoard(10);
		assert(playTest.getScore() == 15);
		assert(playTest2.getScore() == 10);
		System.out.println("13 -> Complete Updating player score from game engine");


		HexBoard hexTest = new HexBoard(2);

		System.out.println("Starting with verification of hex board");
		//verifying that default size of a hex board = 3 X 3
		assert(hexTest.getHeight() == 3);
		assert(hexTest.getWidth() == 3);
		System.out.println("14 -> Verified length and width of the board");

		// verifying the move is valid function
		System.out.println("15 -> Starting test to verify move functionality");
		assert(hexTest.moveIsValid(0, 1) == true);
		assert(hexTest.moveIsValid(-1, -1) == false);
		assert(hexTest.moveIsValid(3, 3) == false);
		System.out.println("15 -> verified the functionality of moveIsValid function");

		//verifying whether a move is being set
		System.out.println("16 -> Starting test to verify move setting");
		hexTest.updateBoard("set", 0, 0, 0);
		assert(hexTest.moveIsValid(0, 0) == false);
		System.out.println("16 -> Verified that a move is being set");


		//verifying whether a move is being unset(undo)
		System.out.println("17 -> Starting undo test");
		hexTest.updateBoard("unset", 0, 0, 0);
		assert(hexTest.moveIsValid(0, 0) == true);
		System.out.println("17 -> Completing undo test");

		//verifying game logic
		System.out.println("18 -> Start verifying game logic for win(1)");
		hexTest.updateBoard("set", 1, 0, 0);
		hexTest.updateBoard("set", 1, 1, 0);
		hexTest.updateBoard("set", 1, 2, 0);
		ArrayList <Integer> hexResults = hexTest.analyseBoard();
		assert(hexResults.get(0) == 2 && hexResults.get(1) == 0); // indicating 0th player won
		System.out.println("18 -> Complete verifying game logic for win(1)");

	}
}