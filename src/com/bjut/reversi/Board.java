package com.bjut.reversi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * ���� ������
 * @author liwei
 *
 */
public class Board extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int WHITE=1;//1��ʾ�׷�
	public static final int BLACK=-1;//-1��ʾ�ڷ�
	public static final int SPACE=0;//0��ʾ��
	private int playColor = BLACK;//ѡ����ɫ
	
	private boolean end;//�Ƿ��ѽ���
	private boolean isBVB;//�Ƿ�AI�Ծ�
	private IPlayer player1;
	private IPlayer player2;
	private String myMessage = "NO";//������������
	private JPanel boradLayout;//��岼��
	private JButton jpNowchess;//��ǰ������ɫ
	private JLabel jpResult;//��ǰ����
	private JLabel jpResult2;//��ǰ����
	
	private Component[][] boardView = new Component[9][9];//��P
	private int board[][] = new int[8][8];//��������
	
	
	
	public Board() {
		 this.setLayout(new BorderLayout());
		 this.setTitle("�ڰ���");
		 this.setBounds(100, 100, 780, 600);  
		 boradLayout = new JPanel(new GridLayout(9, 9));
		 initBoard();
		 initBoardView();
		 initMenu();
		 this.add(boradLayout,"Center");
		 this.setVisible(true); 
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	}
	
	public void setPlayer1(IPlayer player){
		this.player1 = player;
	}
	public void setPlayer2(IPlayer player){
		this.player2 = player;
	}
	
	
/*	public static void main(String[] args) {
		Board board = new Board();
		board.readMessage("");
	}*/
	
	public void readMessage(String message){
		MLog.i("player = "+message);
		if(message.equals("BLACK")){//�����BLACK�����ʾ�ǿ���ִ��
			this.playColor = BLACK;
			showDialog("ִ�������ߣ�");
		}else if(message.equals("WHITE")){//�����WHITE�����ʾ�ǿ���ִ��
			this.playColor = WHITE;
			sendMessage("NO");
		}else if(message.equals("NO")){
			//�Է���һ���������
		}else{//��ͨ����
			int xOpp=message.charAt(0)-'1', yOpp = message.charAt(1)-'A';
			if(this.player1.isBlack()){
				//player1.modifyBoard(xOpp, yOpp, 1);
				if(!this.pieceLegalJudge(xOpp, yOpp,BLACK, true))sendMessage("NO");
			}
			else{
				//player1.modifyBoard(xOpp, yOpp, -1);
				if(!this.pieceLegalJudge(xOpp, yOpp, WHITE, true))sendMessage("NO");
			}
		}
	}
	 

	/**
	 * ������Ϣ
	 * @return
	 */
	public String getMessage(String message){
	/*	String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//�������ʧ��
		}*/
		readMessage(player1.readMessage(message));
		return message;
	}
	/**
	 * �����Ϣ
	 * @param message
	 */
	public void sendMessage(String message){
		/*try {
			System.in.read((message+"\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		MLog.i("board = "+message);//�����Ϣ
		readMessage(player1.readMessage(message));
	}
	
	public void initBoard(){
		for(int i=0;i<8;i++){//���̳�ʼ����0��ʾ�գ�1��ʾ�ף�-1��ʾ��
			for(int j=0;j<8;j++){
				board[i][j] = SPACE;
			}
		}
		board[4][4] = WHITE;
		board[3][3] = WHITE;
		board[4][3] = BLACK;
		board[3][4] = BLACK;
	}
	/**
	 * ��ʼ����P
	 */
	private void initBoardView(){
		Label space = new Label();
		boradLayout.add(space);
		boardView[0][0]=space;
		for(int i =1;i<9;i++){
			Label label = new Label("   "+(char)(i-1+'A'));  
			boradLayout.add(label);
			boardView[0][i]=label;
		}
		
		for(int i =1;i<9;i++){
		 for(int j =0;j<9;j++){
		  if(j==0){
			Label label = new Label(""+i); 
			boradLayout.add(label); 
			boardView[i][0]=label;
		  }else{
			JButton btn = new JButton("");  
			btn.setActionCommand(i+"_"+j);
			btn.setBackground(Color.GRAY);
			btn.addActionListener(this);
			boradLayout.add(btn);  
			boardView[i][j]=btn;
			}
		 }
		}
		boardView[4][4].setBackground(Color.WHITE);
		boardView[5][5].setBackground(Color.WHITE);
		boardView[4][5].setBackground(Color.BLACK);
		boardView[5][4].setBackground(Color.BLACK);
	}
	private void resetBoard(){
		initBoard();
		for(int i =1;i<9;i++){
			 for(int j =0;j<9;j++){
			  if(j!=0){
				boardView[i][j].setBackground(Color.GRAY);;
				}
			 }
		}
		boardView[4][4].setBackground(Color.WHITE);
		boardView[5][5].setBackground(Color.WHITE);
		boardView[4][5].setBackground(Color.BLACK);
		boardView[5][4].setBackground(Color.BLACK);
		player1.boardInit();
	}
	
	private void initMenu() {
		JPanel jpMenu = new JPanel();
		jpMenu.setBackground(new Color(160, 207, 230));
		jpMenu.setPreferredSize(new Dimension(150, 500));
		jpMenu.setLayout(null);
	    add(jpMenu, "East");
	    JLabel jlb = new JLabel("�� ǰ ִ ��");
	    jpMenu.add(jlb);
	    jlb.setBounds(48, 80, 70, 20);
	    jpNowchess = new  JButton("");  
	    jpNowchess.setBackground(Color.BLACK);
	    jpNowchess.setBounds(55, 110, 50, 50);
	    jpMenu.add(jpNowchess);
	    
	    jpResult = new JLabel();
	    jpResult.setBounds(20, 160, 100, 50);
	    jpMenu.add(jpResult);
	    jpResult2 = new JLabel();
	    jpResult2.setBounds(20, 180, 100, 50);
	    jpMenu.add(jpResult2);
	    
	    JButton restart = new JButton("���¿�ʼ");
	    restart.setBounds(40, 350, 70, 30);
	    jpMenu.add(restart);
	    restart.addActionListener(restartListener);
	    
	    JButton bvb = new JButton("BVB");
	    bvb.setBounds(40, 250, 70, 30);
	    jpMenu.add(bvb);
	    bvb.addActionListener(bvbListener);
	    
	    JButton pass = new JButton("PASS");
	    pass.setBounds(40, 300, 70, 30);
	    jpMenu.add(pass);
	    pass.addActionListener(passListener);
	}
	/*
	 * ���ð�ť
	 */
	ActionListener restartListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			showDialog("��Ϸ���¿�ʼ");
			resetBoard();
//			playColor = BLACK;
			playColor = WHITE;
			readMessage(player1.readMessage("BLACK"));
			endGame =true;
		}
	};
	/*
	 * pass
	 */
	ActionListener passListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage("NO");
		}
	};
	/*
	 * bvb
	 */
	ActionListener bvbListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			MLog.i("��Ϸ��ʼ");
			new Thread(new Runnable() {
				@Override
				public void run() {
					bvb();
				}
			}).start();
			
		}
	};
	private boolean endGame;
	
	/**
	 * Ĭ��BVB player1 �������
	 */
	private void bvb(){
		endGame =false;
		player1.boardInit();
		player2.boardInit();
		IPlayer playerC = player2;
		 player1.readMessage("WHITE");
		 String message = player2.readMessage("BLACK");
		while(!endGame){
			
			MLog.i((playerC.isBlack()?"�ڷ���":"�׷���")+message);
			if(!"NO".equals(message)){
				int x=message.charAt(0)-'1', y = message.charAt(1)-'A';
				if(!this.pieceLegalJudge(x, y, playerC.isBlack()?BLACK:WHITE, true)){
					MLog.i("�����쳣");
				}
			}
			playerC= playerC==player1?player2:player1;
			jpNowchess.setBackground(playerC.isBlack()?Color.BLACK:Color.WHITE);
			int[] result = resultCount(board);
			jpResult.setText("�ڷ�:"+result[0]+" �׷�:"+result[1]);
			jpResult2.setText("����:"+(64-result[2])+" ʣ:"+result[2]);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String message2 = playerC.readMessage(message);
			if("NO".equals(message)&&"NO".equals(message2)){
				endGame =true;
				MLog.i("player "+(playerC.isBlack()?"�ڷ���":"�׷���")+message);
			}
			message = message2;
		}
		MLog.i("��Ϸ����");
		this.showResult();
	}
	/**
	 * @description �ж�ĳ��λ����ĳ����ɫ�������Ƿ�Ϸ���ͬʱ�ܹ�ѡ�����޸�����
	 * @param x ���ж�λ�õ�������
	 * @param y ���ж�λ�õ�������
	 * @param color �жϷ���ɫ
	 * @param modifyOrNot ��ʾ���жϹ������Ƿ�ͬʱ�������̵��޸�
	 * @return �Ϸ��򷵻�true�����򷵻�false
	 */
	public boolean pieceLegalJudge(int x, int y, int color,boolean modifyOrNot){
		boolean flag = false;
		
		if(isInBounds(x,y) && SPACE==board[x][y]){//��Խ����Ϊ��
			int xStep=0,yStep=0;
			for(int i=0; i<8; i++){//�����˸�����ȷ����λ���Ƿ�Ϸ�
				switch(i)//����ȷ������
				{
					case 0 : {xStep=-1; yStep= 0;}; break;//��
					case 1 : {xStep=-1; yStep= 1;}; break;//����
					case 2 : {xStep= 0; yStep= 1;}; break;//��
					case 3 : {xStep= 1; yStep= 1;}; break;//����
					case 4 : {xStep= 1; yStep= 0;}; break;//��
					case 5 : {xStep= 1; yStep=-1;}; break;//����
					case 6 : {xStep= 0; yStep=-1;}; break;//��
					case 7 : {xStep=-1; yStep=-1;}; break;//����
					default:;
				}
				if(oneDirectionJudge(x,y,color,xStep,yStep,modifyOrNot)){
					flag = true;
				}
			}
		}
		if(flag && modifyOrNot){//����Ϸ�����Ҫ�޸����̣�����һ��Ҳ�޸�
			board[x][y] = color;
			boardView[x+1][y+1].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
		}
		return flag;
	}

	/**
	 * @description �ж�ĳ��λ�õ�ĳ�������Ƿ��ܹ���ת�������ӣ�ͬʱ�ܹ�ѡ���Ե��޸�����
	 * @param x �������������
	 * @param y �������������
	 * @param color �жϷ�����ɫ
	 * @param xStep ���������з����ϵĵ�Ԫ����
	 * @param yStep ���������з����ϵĵ�Ԫ����
	 * @param modifyOrNot ��ʾ���жϹ������Ƿ�ͬʱ�������̵��޸�
	 * @return �ڴ˷����ܹ���ת���������򷵻�true�����򷵻�false
	 */
	public boolean oneDirectionJudge(int x, int y, int color, int xStep, int yStep,boolean modifyOrNot){
		int xMv,yMv;
		xMv = x + xStep; yMv = y +yStep;
		while(true){
			if(!isInBounds(xMv,yMv)){//���Խ����ֹͣ����λ�ò��Ϸ�
				break;
			}
			else if(0 == board[xMv][yMv]){//���Ϊ����ֹͣ����λ�ò��Ϸ�
				break;
			}
			else if(color == board[xMv][yMv]){//���ͬɫ
				if(xMv==x+xStep && yMv==y+yStep)  break; //����ǵ�һ����ͬɫ��ֹͣ�����Ϸ�	
				else{
					if(modifyOrNot) lineModify(x+xStep,y+yStep,xMv-xStep,yMv-yStep,color);
					return true;
				}
			}
			xMv += xStep; yMv += yStep;
		}
		return false;
	}
	

	/**
	 * @description ��һ�����ϵ������޸�Ϊָ����ɫ
	 * @param xS ��ʼλ��������
	 * @param yS ��ʼλ��������
	 * @param xE ��ֹλ��������
	 * @param yE ��ֹλ��������
	 * @param color ��Ҫ�޸ĳɵ���ɫ
	 */
	public void lineModify(int xS,int yS,int xE,int yE,int color){//����Ҫ�޸ĵ���ʼ����ֹλ�ã��Լ�Ҫ�ĳɵ���ɫ�����������������޸�һ����
		//��̬ȷ��xMv��yMv������
		int xMoveUnit = xS>xE ? -1	:
						xS<xE ? 1	: 0;
		int yMoveUnit = yS>yE ? -1	:
						yS<yE ? 1	: 0;
		
		int xMv = xS, yMv = yS;//��ʼλ��Ϊ��һ��Ҫ�޸ĵ�λ��
		while(board[xMv][yMv] != color){//����ǰҪ�޸ĵ�λ�õ���ɫ���ǡ���ֹ��ɫ��ʱ��ѭ������
			board[xMv][yMv] = color;//�Ȱѵ�ǰλ�õ���ɫ�޸���
			boardView[xMv+1][yMv+1].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
			xMv += xMoveUnit;//λ��ָ���ƶ�����һ��λ��
			yMv += yMoveUnit;
		}
		
	}
	
	public String changeCoordinateForm(int x, int y){
		String str="";
		str = Integer.toString(x+1) + (char)(y+'A');
		return str;
	}
	

	public boolean isInBounds(int x, int y){//�Ƿ��ڽ����ڲ���true��ʾ���ڲ���false��ʾ����
		
		if(x>=0&&x<8 && y>=0&&y<8){
			return true;
		}
		return false;
	}
	
	//����¼�
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		String[] xyStr = actionCommand.split("_");
		int x = Integer.valueOf(xyStr[0])-1;
		int y = Integer.valueOf(xyStr[1])-1;
		
		if(pieceLegalJudge(x, y, playColor, true)){
			board[x][y] = playColor;
			boardView[x+1][y+1].setBackground(playColor==WHITE?Color.WHITE:Color.BLACK);
			String outMessage = changeCoordinateForm(x, y);
			sendMessage(outMessage);
		}
		
	}

	public Component[][] getBoard() {
		return boardView;
	}

	public void setBoard(Component[][] board) {
		this.boardView = board;
	}
	/**
	 * ��ȡ��ǰ�ڰ�����
	 * @return
	 */
	public int[] resultCount(int[][] board){
		int bCount =0;
		int wCount =0;
		int sCount =0;
		for(int i=0;i<8;i++){ 
			for(int j=0;j<8;j++){
				if(board[i][j] == BLACK){
					bCount++;
				}else if(board[i][j]==WHITE){
					wCount++;
				}else{
					sCount++;
				}
			}
		}
		return new int[]{bCount,wCount,sCount};
	}
	public void showResult(){
		int[] result = resultCount(board);
		String winner ="";
		int winnerColor=SPACE;
		String winnerPlayer="";
		if(result[0]>result[1]){
			winnerColor =BLACK;
			winner="�ڷ�";
		}else{
			winnerColor =WHITE;
			winner ="�׷�";
		}
		if(winnerColor==BLACK&&player1.isBlack()||winnerColor!=BLACK&&!player1.isBlack()){
				winnerPlayer = player1.getClass().getSimpleName();
		}else if(player2!=null){
				winnerPlayer = player2.getClass().getSimpleName();
		}
		MLog.i("�ڷ���"+result[0]+"���׷���"+result[1]+"��ʤ����"+winner+" winnerPlayer:"+winnerPlayer);
		showDialog("�ڷ���"+result[0]+"���׷���"+result[1]+"��ʤ����"+winner);
	}
	
	/**
	 * ������
	 * @param content
	 */
	public void showDialog(String content){
		/*JDialog dialog = new JDialog(this);
		dialog.setTitle("��ʾ");
		dialog.getContentPane().add(new Label(content));
		dialog.setLocation((int)(this.getLocation().getX()+this.getWidth())/2, (int)(getLocation().getY()+getHeight())/2);
		dialog.setSize(200, 200);
		dialog.setVisible(true);*/
		JOptionPane.showMessageDialog(null, content);
	}

	public int getPlayColor() {
		return playColor;
	}

	public void setPlayColor(int playColor) {
		this.playColor = playColor;
	}
	
}
