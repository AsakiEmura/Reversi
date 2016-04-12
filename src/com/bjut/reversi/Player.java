package com.bjut.reversi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Player{
	
	public boolean amIBlack;
	int board[][];
	
	public Player(){
		board = new int[9][9];//9 X 9���飬����8 X 8����
		boardInit();
	}
		
	public String readMessage(String message){
		String myMessage = "NO";
		
			if(message.equals("BLACK")){//�����BLACK�����ʾ�ǿ���ִ��
				this.amIBlack = true;
			}
			else if(message.equals("WHITE")){//�����WHITE�����ʾ�ǿ���ִ��
				this.amIBlack = false;
				return "NO";
			}
			else if(message.equals("NO")){
				//�Է���һ���������
			}
			else{//��ͨ����
				int xOpp=message.charAt(0)-'1'+1, yOpp = message.charAt(1)-'A'+1;
				if(this.amIBlack){
					//this.modifyBoard(xOpp, yOpp, 1);
					this.pieceLegalJudge(xOpp, yOpp, 1, true);
				}
				else{
					//this.modifyBoard(xOpp, yOpp, -1);
					this.pieceLegalJudge(xOpp, yOpp, -1, true);
				}
			}
			
			//������Է���������ٴ����Լ�����һ������·�
			myMessage = "NO";
			boolean flag = true;
			for(int x=1; x<=8 && flag; x++){//���������򵥲��ԣ���������������һ���Ϸ�����λ�������
				for(int y=1; y<=8 && flag; y++){
					if(this.amIBlack){
						if(this.pieceLegalJudge(x, y, -1, true)){//��ǰλ�úϷ�
							flag = false;
							//this.modifyBoard(x, y, -1);//��һ�ź���
							myMessage = this.changeCoordinateForm(x,y);
						}
					}
					else{
						if(this.pieceLegalJudge(x, y, 1, true)){//��ǰλ�úϷ�
							flag = false;
							//this.modifyBoard(x, y, 1);//��һ�Ű���
							myMessage = this.changeCoordinateForm(x,y);
						}
					}
				}
			}
			return myMessage;
	}
	/**
	 * �����Ϣ
	 * @param message
	 */
	public void sendMessage(String message){
	/*	try {
			System.in.read((message+"\n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(message);//�����Ϣ
	}
	
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

	public void boardInit(){
		amIBlack = false;
		for(int i=0;i<9;i++){//���̳�ʼ����0��ʾ�գ�1��ʾ�ף�-1��ʾ��
			for(int j=0;j<9;j++){
				board[i][j] = 0;
			}
		}
		board[4][4] = 1;
		board[5][5] = 1;
		board[4][5] = -1;
		board[5][4] = -1;
	}

	/**
	 * @description �жϵ�ǰ�����Ƿ��ܹ�����
	 * @return �ܹ���������true�����򷵻�false
	 */
	public boolean contJudge(){
		return (oneColorContJudge(-1) || oneColorContJudge(1));
	}
	
	/**
	 * @description �ж�ĳ���Ƿ��ܹ�����
	 * @param color ��Ҫ�жϷ���������ɫ��1��ʾ�׷���-1��ʾ�ڷ�
	 * @return �ܹ���������true�����򷵻�false
	 */
	public boolean oneColorContJudge(int color){//�ж�ĳһ����ɫ�ڵ�ǰ״̬���Ƿ��ܼ���,����true��ʾ�ܼ�����false��ʾ���ܼ���	
		for(int x=1; x<=8; x++){
			for(int y=1; y<=8; y++){
				if(pieceLegalJudge(x,y,color,false)){
					return true;
				}
			}
		}
		return false;
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
	 * @description �ж�ĳ��λ����ĳ����ɫ�������Ƿ�Ϸ���ͬʱ�ܹ�ѡ�����޸�����
	 * @param x ���ж�λ�õ�������
	 * @param y ���ж�λ�õ�������
	 * @param color �жϷ���ɫ
	 * @param modifyOrNot ��ʾ���жϹ������Ƿ�ͬʱ�������̵��޸�
	 * @return �Ϸ��򷵻�true�����򷵻�false
	 */
	public boolean pieceLegalJudge(int x, int y, int color,boolean modifyOrNot){
		boolean flag = false;
		if(isInBounds(x,y) && 0==board[x][y]){//��Խ����Ϊ��
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
		}
		return flag;
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
			xMv += xMoveUnit;//λ��ָ���ƶ�����һ��λ��
			yMv += yMoveUnit;
		}
		
	}
	
	
	public int getNumberOfOneColor(int color){
		int result = 0;
		for(int x=1; x<=8; x++){
			for(int y=1; y<=8; y++){
				if(color == board[x][y]){
					result++;
				}
			}
		}	
		return result;
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

}
