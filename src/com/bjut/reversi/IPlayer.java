package com.bjut.reversi;

public interface IPlayer {
	public static final int WHITE=1;//1��ʾ�׷�
	public static final int BLACK=-1;//-1��ʾ�ڷ�
	public static final int SPACE=0;//0��ʾ��
	
	public String readMessage(String message);
	
	public boolean isBlack();

	public void boardInit();
}
