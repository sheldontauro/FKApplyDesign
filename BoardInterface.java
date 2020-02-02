package javaDesign;

import java.util.*;

public interface BoardInterface {
	boolean moveIsValid(int xcord, int ycord);
	void displayBoard();
	ArrayList<Integer> analyseBoard();
	int getHeight();
	int getWidth();
	int updateBoard(String operation, int xcord, int ycord, int playerNum);
	boolean noMovesLeft();
}