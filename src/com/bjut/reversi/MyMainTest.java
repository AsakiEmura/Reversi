package com.bjut.reversi;

/**
 * �Լ��Ĳ��Թ���
 * @author liwei
 *
 */
public class MyMainTest {

	public static void main(String[] args) {
		MLog.DEBUG =true;
			Board board  = new Board();
			IPlayer player1 = new Player4();
			IPlayer player2 = new Player7();
			board.setPlayer1(player1);
			board.setPlayer2(player2);
//			board.readMessage("BLACK");
//			board.readMessage("WHITE");
//			board.readMessage(player2.readMessage("BLACK"));
	}

}
