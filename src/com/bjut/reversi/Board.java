package com.bjut.reversi;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
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

public class Board extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int WHITE=1;//1��ʾ�׷�
	public static final int BLACK=-1;//-1��ʾ�ڷ�
	public static final int SPACE=0;//0��ʾ��
	private int playColor = WHITE;//ѡ����ɫ
	private Player player1;
	private String myMessage = "NO";//������������
	
	private Component[][] boardView = new Component[9][9];//��P
	private int board[][] = new int[9][9];
	
	public Board() {
		 this.setLayout(new GridLayout(9, 9));
		 this.setTitle("�ڰ���");
		 this.setBounds(100, 100, 600, 600);  
		 initBoard();
		 initBoardView();
		 this.setVisible(true); 
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	}
	public void setPlayer1(Player player){
		this.player1 = player;
	}
	
/*	public static void main(String[] args) {
		Board board = new Board();
		board.readMessage("");
	}*/
	
	public void readMessage(String message){
		System.out.println("player = "+message);
		if(message.equals("BLACK")){//�����BLACK�����ʾ�ǿ���ִ��
			this.playColor = BLACK;
			showDialog("ִ�������ߣ�");
		}else if(message.equals("WHITE")){//�����WHITE�����ʾ�ǿ���ִ��
			this.playColor = WHITE;
			sendMessage("NO");
		}else if(message.equals("NO")){
			//�Է���һ���������
		}else{//��ͨ����
			int xOpp=message.charAt(0)-'1'+1, yOpp = message.charAt(1)-'A'+1;
			if(!this.player1.amIBlack){
				//player1.modifyBoard(xOpp, yOpp, 1);
				if(!this.pieceLegalJudge(xOpp, yOpp, WHITE, true))sendMessage("NO");
			}
			else{
				//player1.modifyBoard(xOpp, yOpp, -1);
				if(!this.pieceLegalJudge(xOpp, yOpp, BLACK, true))sendMessage("NO");
			}
		}
	}
	/**
	 * ������Ϣ
	 * @return
	 */
	public String getMessage(){
		String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//�������ʧ��
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("board = "+message);//�����Ϣ
		readMessage(player1.readMessage(message));
	}
	
	public void initBoard(){
		for(int i=0;i<9;i++){//���̳�ʼ����0��ʾ�գ�1��ʾ�ף�-1��ʾ��
			for(int j=0;j<9;j++){
				board[i][j] = SPACE;
			}
		}
		board[4][4] = WHITE;
		board[5][5] = WHITE;
		board[4][5] = BLACK;
		board[5][4] = BLACK;
	}
	/**
	 * ��ʼ����P
	 */
	private void initBoardView(){
		Label space = new Label();
		this.add(space);
		boardView[0][0]=space;
		for(int i =1;i<9;i++){
			Label label = new Label("   "+(char)(i-1+'A'));  
			this.add(label);
			boardView[0][i]=label;
		}
		
		for(int i =1;i<9;i++){
		 for(int j =0;j<9;j++){
		  if(j==0){
			Label label = new Label(""+i); 
			this.add(label); 
			boardView[i][0]=label;
		  }else{
			JButton btn = new JButton("");  
			btn.setActionCommand(i+"_"+j);
			btn.setBackground(Color.GRAY);
			btn.addActionListener(this);
			this.add(btn);  
			boardView[i][j]=btn;
			}
		 }
		}
		boardView[4][4].setBackground(Color.WHITE);
		boardView[5][5].setBackground(Color.WHITE);
		boardView[4][5].setBackground(Color.BLACK);
		boardView[5][4].setBackground(Color.BLACK);
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
			boardView[x][y].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
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
			boardView[xMv][yMv].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
			xMv += xMoveUnit;//λ��ָ���ƶ�����һ��λ��
			yMv += yMoveUnit;
		}
		
	}
	
	public String changeCoordinateForm(int x, int y){
		String str="";
		str = Integer.toString(x) + (char)(y+'A'-1);
		return str;
	}
	

	public boolean isInBounds(int x, int y){//�Ƿ��ڽ����ڲ���true��ʾ���ڲ���false��ʾ����
		
		if(x>=1&&x<=8 && y>=1&&y<=8){
			return true;
		}
		return false;
	}
	
	//����¼�
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		String[] xyStr = actionCommand.split("_");
		int x = Integer.valueOf(xyStr[0]);
		int y = Integer.valueOf(xyStr[1]);
		boardView[x][y].setBackground(playColor==WHITE?Color.WHITE:Color.BLACK);
		this.pieceLegalJudge(x, y, playColor, true);
		String outMessage = changeCoordinateForm(x, y);
		sendMessage(outMessage);
	}

	public Component[][] getBoard() {
		return boardView;
	}

	public void setBoard(Component[][] board) {
		this.boardView = board;
	}
	/**
	 * ������
	 * @param content
	 */
	public void showDialog(String content){
		JDialog dialog = new JDialog(this);
		dialog.setTitle("��ʾ");
		dialog.getContentPane().add(new Label(content));
		dialog.setLocation((int)(this.getLocation().getX()+this.getWidth())/2, (int)(getLocation().getY()+getHeight())/2);
		dialog.setSize(200, 200);
		dialog.setVisible(true);
	}

	public int getPlayColor() {
		return playColor;
	}

	public void setPlayColor(int playColor) {
		this.playColor = playColor;
	}
}
