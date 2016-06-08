package com.bjut.reversi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * AI����
 * 
 * @author liwei
 *
 */
public class Player6 implements IPlayer {

	private int board[][] = new int[8][8];// ��������
	private ArrayList<int[]> check = new ArrayList<int[]>();// ��������
	private int myColor = WHITE;

	private int[][] cellpoints = {
			{ 100, -5, 10, 5, 5, 10, -5, 100 },
			{ -5, -45, 1, 1, 1, 1, -45, -5 },
			{ 10, 1, 3, 2, 2, 3, 1, 10 },
			{ 5, 1, 2, 1, 1, 2, 1, 5 },
			{ 5, 1, 2, 1, 1, 2, 1, 5 },
			{ 10, 1, 3, 2, 2, 3, 1, 10 },
			{ -5, -45, 1, 1, 1, 1, -45, -5 },
			{ 100, -5, 10, 5, 5, 10, -5, 100 } };

	private int INF = 10000000;

	public Player6() {
		boardInit();
	}

	@Override
	public String readMessage(String message) {
		String myMessage = "NO";

		if (message.equals("BLACK")) {// �����BLACK�����ʾ�ǿ���ִ��
			this.myColor = BLACK;
		} else if (message.equals("WHITE")) {// �����WHITE�����ʾ�ǿ���ִ��
			this.myColor = WHITE;
			return "NO";
		} else if (message.equals("NO")) {
			// �Է���һ���������
		} else {// ��ͨ����
			int xOpp = message.charAt(0) - '1', yOpp = message.charAt(1) - 'A';
			this.pieceLegalJudge(xOpp, yOpp, -myColor, true);// �ж϶Է�����������
		}

		// ������Է���������ٴ����Լ�����һ������·�
		myMessage = botJudge();
		return myMessage;
	}

	@Override
	public boolean isBlack() {
		return myColor == BLACK;
	}

	@Override
	public void setIsBlack(boolean isBlack) {
		myColor = (isBlack ? BLACK : WHITE);
	}

	@Override
	public void boardInit() {
		myColor = WHITE;
		for (int i = 0; i < 8; i++) {// ���̳�ʼ����0��ʾ�գ�1��ʾ�ף�-1��ʾ��
			for (int j = 0; j < 8; j++) {
				board[i][j] = SPACE;
			}
		}
		board[4][4] = WHITE;
		board[3][3] = WHITE;
		board[4][3] = BLACK;
		board[3][4] = BLACK;
	}

	public boolean pieceLegalJudge(int x, int y, int color, boolean modifyOrNot) {
		return pieceLegalJudge(x, y, color, modifyOrNot, board);
	}

	/**
	 * @description �ж�ĳ��λ����ĳ����ɫ�������Ƿ�Ϸ���ͬʱ�ܹ�ѡ�����޸�����
	 * @param x
	 *            ���ж�λ�õ�������
	 * @param y
	 *            ���ж�λ�õ�������
	 * @param color
	 *            �жϷ���ɫ
	 * @param modifyOrNot
	 *            ��ʾ���жϹ������Ƿ�ͬʱ�������̵��޸�
	 * @param board
	 * @return �Ϸ��򷵻�true�����򷵻�false
	 */
	public boolean pieceLegalJudge(int x, int y, int color, boolean modifyOrNot, int[][] board) {
		boolean flag = false;
		if (isInBounds(x, y) && SPACE == board[x][y]) {// ��Խ����Ϊ��
			int xStep = 0, yStep = 0;
			for (int i = 0; i < 8; i++) {// �����˸�����ȷ����λ���Ƿ�Ϸ�
				switch (i)// ����ȷ������
				{
				case 0: {
					xStep = -1;
					yStep = 0;
				}
					;
					break;// ��
				case 1: {
					xStep = -1;
					yStep = 1;
				}
					;
					break;// ����
				case 2: {
					xStep = 0;
					yStep = 1;
				}
					;
					break;// ��
				case 3: {
					xStep = 1;
					yStep = 1;
				}
					;
					break;// ����
				case 4: {
					xStep = 1;
					yStep = 0;
				}
					;
					break;// ��
				case 5: {
					xStep = 1;
					yStep = -1;
				}
					;
					break;// ����
				case 6: {
					xStep = 0;
					yStep = -1;
				}
					;
					break;// ��
				case 7: {
					xStep = -1;
					yStep = -1;
				}
					;
					break;// ����
				default:
					;
				}
				if (oneDirectionJudge(x, y, color, xStep, yStep, modifyOrNot, board)) {
					flag = true;
				}
			}
		}
		if (flag && modifyOrNot) {// ����Ϸ�����Ҫ�޸����̣�����һ��Ҳ�޸�
			board[x][y] = color;
		}
		return flag;
	}

	/**
	 * @description �ж�ĳ��λ�õ�ĳ�������Ƿ��ܹ���ת�������ӣ�ͬʱ�ܹ�ѡ���Ե��޸�����
	 * @param x
	 *            �������������
	 * @param y
	 *            �������������
	 * @param color
	 *            �жϷ�����ɫ
	 * @param xStep
	 *            ���������з����ϵĵ�Ԫ����
	 * @param yStep
	 *            ���������з����ϵĵ�Ԫ����
	 * @param modifyOrNot
	 *            ��ʾ���жϹ������Ƿ�ͬʱ�������̵��޸�
	 * @param board
	 * @return �ڴ˷����ܹ���ת���������򷵻�true�����򷵻�false
	 */
	public boolean oneDirectionJudge(int x, int y, int color, int xStep, int yStep, boolean modifyOrNot,
			int[][] board) {
		int xMv, yMv;
		xMv = x + xStep;
		yMv = y + yStep;
		while (true) {
			if (!isInBounds(xMv, yMv)) {// ���Խ����ֹͣ����λ�ò��Ϸ�
				break;
			} else if (SPACE == board[xMv][yMv]) {// ���Ϊ����ֹͣ����λ�ò��Ϸ�
				break;
			} else if (color == board[xMv][yMv]) {// ���ͬɫ
				if (xMv == x + xStep && yMv == y + yStep)
					break; // ����ǵ�һ����ͬɫ��ֹͣ�����Ϸ�
				else {
					if (modifyOrNot)
						lineModify(x + xStep, y + yStep, xMv - xStep, yMv - yStep, color, board);
					return true;
				}
			}
			xMv += xStep;
			yMv += yStep;
		}
		return false;
	}

	/**
	 * @description ��һ�����ϵ������޸�Ϊָ����ɫ
	 * @param xS
	 *            ��ʼλ��������
	 * @param yS
	 *            ��ʼλ��������
	 * @param xE
	 *            ��ֹλ��������
	 * @param yE
	 *            ��ֹλ��������
	 * @param color
	 *            ��Ҫ�޸ĳɵ���ɫ
	 * @param board
	 */
	public void lineModify(int xS, int yS, int xE, int yE, int color, int[][] board) {// ����Ҫ�޸ĵ���ʼ����ֹλ�ã��Լ�Ҫ�ĳɵ���ɫ�����������������޸�һ����
		// ��̬ȷ��xMv��yMv������
		int xMoveUnit = xS > xE ? -1 : xS < xE ? 1 : 0;
		int yMoveUnit = yS > yE ? -1 : yS < yE ? 1 : 0;

		int xMv = xS, yMv = yS;// ��ʼλ��Ϊ��һ��Ҫ�޸ĵ�λ��
		while (board[xMv][yMv] != color) {// ����ǰҪ�޸ĵ�λ�õ���ɫ���ǡ���ֹ��ɫ��ʱ��ѭ������
			board[xMv][yMv] = color;// �Ȱѵ�ǰλ�õ���ɫ�޸���
			xMv += xMoveUnit;// λ��ָ���ƶ�����һ��λ��
			yMv += yMoveUnit;
		}

	}

	public String changeCoordinateForm(int x, int y) {
		String str = "";
		str = Integer.toString(x + 1) + (char) (y + 'A');
		return str;
	}

	public boolean isInBounds(int x, int y) {// �Ƿ��ڽ����ڲ���true��ʾ���ڲ���false��ʾ����

		if (x >= 0 && x < 8 && y >= 0 && y < 8) {
			return true;
		}
		return false;
	}

	/**
	 * AI�ж�
	 * 
	 * @return
	 */
	public String botJudge() {
		String myMessage = "NO";
		int x = -1, y = -1;
		int maxValue = -this.INF;
		int a = -this.INF;
		int b = this.INF;

		check = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (pieceLegalJudge(i, j, myColor, false)) {
					check.add(new int[] { i, j });
				}
		int branches = check.size();
		for (int i = 0; i < branches; i++) {
			int[][] tmpBoard = new int[8][8];
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++)
					tmpBoard[j][k] = board[j][k];
			}
			pieceLegalJudge(check.get(i)[0], check.get(i)[1], myColor, true, tmpBoard);
			int temp = dfs(-myColor, INF, tmpBoard, false, a, b, myColor);
//			 MLog.i("1branches:"+i+" ans="+temp+" x:"+check.get(i)[0]+" y:"+check.get(i)[1]+" color:"+(myColor==BLACK?"��":"��"));
			if (temp > a)
				a = temp;
			if (temp <= maxValue)
				continue;
			maxValue = temp;
			x = check.get(i)[0];
			y = check.get(i)[1];
		}

		if (branches != 0 && x != -1 && y != -1) {
			pieceLegalJudge(x, y, myColor, true);
			myMessage = this.changeCoordinateForm(x, y);
			// MLog.i("bot_judge= "+x + " " + y);
		}
		return myMessage;
	}

	/**
	 * �ؼ�����<br>
	 * �ж������������<br>
	 * ���ݹ�ʽ ans = p1 * a1 + p2 * a2;<br>
	 * p1 = 3; p2 = 7;<br>
	 * a1 ÿ��λ�õ�Ȩֵ��<br>
	 * a2 ��Ե�ӵ�����<br>
	 * 
	 * @param color
	 *            ������ɫ
	 * @param branches
	 *            ��֧��
	 * @param chessboard
	 *            ����
	 * @param stop
	 *            �Ƿ�����
	 * @param a
	 *            ��Сֵ
	 * @param b
	 *            ���ֵ
	 * @param fa
	 *            �²�����ɫ
	 * @return ��ֵ
	 */
	public int dfs(int color, int branches, int[][] chessboard, boolean stop, int a, int b, int fa) {
		if (branches == 0) {// ��֧��Ϊ0 ���� ��ʽ���� ��ֵ
			int a1 = 0;// ÿ��λ�õ�Ȩֵ�ͣ�Ȩ��
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (chessboard[i][j] == myColor)
						a1 += this.cellpoints[i][j];
					else if (chessboard[i][j] == -myColor) {
						a1 -= this.cellpoints[i][j];
					}
				}
			}
			int[] bwCount = resultCount(chessboard);
			int a2 = judgeStatic(chessboard);// ��Ե�ӵ��������ж���
			int a3 = myColor==BLACK?bwCount[0]:bwCount[1];//������
//			int p1 = 3;//Ȩ��
//			int p2 = 7;//�ж���
			int p1 = 128/ (Math.abs(40 - bwCount[2])+1)+10;//Ȩ��
			int p2 = 64-p1+60;//�ж���
			int p3 = 64/ (64 - bwCount[2]+1);//������
			int ans = p1 * a1 + p2 * a2 + p3*a3;
//			MLog.i(this.getClass().getSimpleName()+" branches "+a1+"*"+p1+" + "+a2+"*"+p2+"="+ans);
			return ans;
		}
		// ��֦�㷨
		int min = this.INF;
		int max = -this.INF;

		ArrayList<int[]> check = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieceLegalJudge(i, j, color, false, chessboard)) {
					check.add(new int[] { i, j });
				}
			}
		}
		int rear = check.size();// �����µ�����
		for (int i = 0; i < rear; i++) {
			int[][] tmpBoard = new int[8][8];
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++)
					tmpBoard[j][k] = chessboard[j][k];
			}
			pieceLegalJudge(check.get(i)[0], check.get(i)[1], color, true, tmpBoard);
			int temp = dfs(-color, branches / rear, tmpBoard, false, a, b, color);
			if (fa == myColor) {
				if (color == -myColor) {
					if (temp < b) {
						if (temp <= a)
							return temp;
						b = temp;
					}
					if (temp < min)
						min = temp;
				} else {
					if (color != myColor)
						continue;
					if (temp > a) {
						if (temp > b)
							b = temp;
						a = temp;
					}
					if (temp > max)
						max = temp;
				}
			} else {
				if (fa != -myColor)
					continue;
				if (color == myColor) {
					if (temp > a) {
						if (temp >= b)
							return temp;
						a = temp;
					}
					if (temp > max)
						max = temp;
				} else {
					if (color != -myColor)
						continue;
					if (temp < b) {
						if (temp < a)
							a = temp;
						b = temp;
					}
					if (temp < min) {
						min = temp;
					}
				}
			}
		}
		// ����������
		if (rear == 0) {
			// �ж϶Է�����
			if (!stop) {
				return dfs(fa, branches, chessboard, true, a, b, fa);
			}
			// �������ʱ���ж�˫��������
			int bot_chess = 0;
			int player_chess = 0; 
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					if (chessboard[i][j] == myColor)
						bot_chess++;
					else if (chessboard[i][j] == -myColor)
						player_chess++;
				}
			if (bot_chess > player_chess) {
				return this.INF / 10;
			} else {
				return -this.INF / 10;
			}
		}

		// ����Լ�����ȡ��󣬶Է���ȡ��С��ֵ
		int ans;
		if (color == -myColor)
			ans = min;
		else
			ans = max;
		return ans;
	}

	/**
	 * �жϾ���<br/>
	 * �����Լ������������Ƿ����������Է������������Ƿ�������<br>
	 * �˴��Ƿ���Ż�Ϊ ��Χ8��λ�ö��жϣ���Ϊб��Ҳ�ǿ�������
	 * 
	 * @param chessboard
	 * @return ���ؾ���
	 */
	public int judgeStatic(int[][] chessboard) {
		int ans = 0;
		int xStep = 0;
		int yStep = 0;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j] == myColor||chessboard[i][j] == -myColor)
				for (int k = 0; k < 8; k++) {// �����˸�����ȷ����λ���Ƿ�Ϸ�
					switch (k)// ����ȷ������
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
					if (isInBounds(i + xStep, j + yStep) && chessboard[i + xStep][j + yStep] == SPACE) {
						if (chessboard[i][j] == myColor)
							ans--;
						else
							ans++;
					}
				}
		return ans;
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
}
