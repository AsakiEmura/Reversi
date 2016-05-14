package com.bjut.reversi.reference;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.bjut.reversi.MLog;

/**
 * ���ճ���
 * http://wybwzl.iteye.com/blog/1160837
 * http://wybwzl.iteye.com/blog/1161895
 * 
 * @author 
 *
 */
public class Main   extends JFrame
	  implements Config
	{
	public static final int WHITE=0;// ��ʾ�׷�
	public static final int BLACK=1;// ��ʾ�ڷ�
	public static final int SPACE=-1;// ��ʾ��
	
	  private Graphics2D g;
	  private Graphics2D g_nowchess;
	  private Color color_background = new Color(240, 240, 143);
	  private ImageIcon chessboard = new ImageIcon(Main.class.getResource("images/chessboard.png"));
	  private ImageIcon blackchess = new ImageIcon(Main.class.getResource("images/blackchess.png"));
	  private ImageIcon whitechess = new ImageIcon(Main.class.getResource("images/whitechess.png"));
	  private int[][] box = new int[8][8];//����
	  /**����������*/
	  private int[][] check = new int[8][8];
	  private int[][] up = new int[8][8];
	  private int[][] down = new int[8][8];
	  private int[][] left = new int[8][8];
	  private int[][] right = new int[8][8];
	  private int[][] upleft = new int[8][8];
	  private int[][] upright = new int[8][8];
	  private int[][] downleft = new int[8][8];
	  private int[][] downright = new int[8][8];
	  private JPanel jp_play;//�������
	  private JPanel jp_menu;//�Ҳ�˵����
	  private int chess;//1�����壻 0������
	  private int whitenumber;//��������
	  private int blacknumber;//��������
	  private boolean end;//�Ƿ��ѽ���
	  private boolean AI;//�Ƿ�AI
	  private JPanel jp_nowchess;//��ǰ������ɫ
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
	  private int BRANCHES = 1000000;
	  private int bot_x;
	  private int bot_y;
	  private ButtonGroup btnGroup = new ButtonGroup();
	  private JRadioButton player_first;
	  private JRadioButton bot_first;
	  private JFrame choose;

	  public static void main(String[] args)
	  {
		MLog.DEBUG =true;
	    Main frame = new Main();
	    frame.init();
	  }

	  public void init()
	  {
	    setTitle("Staginner�ڰ���");
	    setResizable(false);
	    setSize(new Dimension(650, 500));
	    setLocationRelativeTo(null);
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(3);

	    this.jp_play = new JPanel()
	    {
	      public void paint(Graphics g)
	      {
	        super.paint(g);
	        MLog.i("jp_play paint");
	        g.drawImage(Main.this.chessboard.getImage(), 0, 0, getWidth(), getHeight(), null);
	        if (Main.this.box != null)
	          for (int i = 0; i < Main.this.box.length; i++)
	            for (int j = 0; j < Main.this.box[i].length; j++)
	            {
	              if (Main.this.box[i][j] < 0)
	                continue;
	              if (Main.this.box[i][j] == BLACK)
	                g.drawImage(Main.this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	              if (Main.this.box[i][j] == WHITE)
	                g.drawImage(Main.this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	            }
	      }
	    };
	    JPanel jp_back = new JPanel();
	    add(jp_back, "Center");
	    jp_back.setBackground(new Color(220, 230, 244));
	    jp_back.setLayout(new FlowLayout(1, 45, 35));

	    this.jp_play.setPreferredSize(new Dimension(400, 400));
	    this.jp_play.setBackground(this.color_background);

	    jp_back.add(this.jp_play);

	    this.jp_menu = new JPanel();
	    this.jp_menu.setBackground(new Color(160, 207, 230));
	    this.jp_menu.setPreferredSize(new Dimension(150, 500));
	    add(this.jp_menu, "East");
	    this.jp_menu.setLayout(null);

	    JLabel jlb = new JLabel("�� ǰ ִ ��");
	    this.jp_menu.add(jlb);
	    jlb.setBounds(48, 80, 70, 20);

	    this.jp_nowchess = new JPanel()
	    {
	      public void paint(Graphics g)
	      {
	        if (Main.this.chess == 1)
	          g.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	        else if (Main.this.chess == 0)
	          g.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	      }
	    };
	    this.jp_nowchess.setBackground(new Color(160, 207, 230));
	    this.jp_nowchess.setBounds(55, 110, 50, 50);
	    this.jp_menu.add(this.jp_nowchess);

	    JButton restart = new JButton("���¿�ʼ");
	    restart.setMargin(new Insets(0, 0, 0, 0));
	    restart.setBounds(40, 350, 70, 30);
	    this.jp_menu.add(restart);

	    restart.addActionListener(
	      new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        Main.this.setInit();
	        if ((Main.this.AI) && (Main.this.bot_first.isSelected()))
	        {
	          Main.this.bot_x = 3;
	          Main.this.bot_y = 2;
	          Main.this.box[Main.this.bot_x][Main.this.bot_y] = chess;
	          Main.this.g.drawImage(Main.this.blackchess.getImage(), Main.this.bot_x * 50 + 2, Main.this.bot_y * 50 + 2, 46, 46, null);
	          Main.this.box[Main.this.bot_x][(Main.this.bot_y + 1)] = chess;
	          Main.this.g.drawImage(Main.this.blackchess.getImage(), Main.this.bot_x * 50 + 2, (Main.this.bot_y + 1) * 50 + 2, 46, 46, null);
	          Main.this.countChess();
	          Main.this.chess = (1 - Main.this.chess);
	          if (Main.this.chess == 1)
	            Main.this.g_nowchess.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	          else if (Main.this.chess == 0)
	            Main.this.g_nowchess.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	          Main.this.checkLaychess();
	        }
	      }
	    });
	    JButton p_vs_b = new JButton("������Ϸ");
	    p_vs_b.setMargin(new Insets(0, 0, 0, 0));
	    p_vs_b.setBounds(40, 250, 70, 30);
	    this.jp_menu.add(p_vs_b);

	    p_vs_b.addActionListener(
	      new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        Main.this.setInit();
	        Main.this.AI = true;
	        Main.this.choose.setLocation(Main.this.jp_play.getLocationOnScreen().x + 125, Main.this.jp_play.getLocationOnScreen().y + 125);
	        Main.this.choose.setVisible(true);
	      }
	    });
	    JButton p_vs_p = new JButton("˫����Ϸ");
	    p_vs_p.setMargin(new Insets(0, 0, 0, 0));
	    p_vs_p.setBounds(40, 300, 70, 30);
	    this.jp_menu.add(p_vs_p);

	    p_vs_p.addActionListener(
	      new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        Main.this.setInit();
	        Main.this.AI = false;
	      }
	    });
	    MouseListener laychess_ml = new MouseAdapter() { private int x;
	      private int y;
	      private int r;
	      private int c;
	      private boolean flag;//��ʶ��,�Ƿ�
	      private boolean bot_continue;

	      public void mouseReleased(MouseEvent e) { if (Main.this.end)
	        {
	          MLog.i("��Ϸ�Ѿ�������");
	          return;
	        }
	        this.flag = false;
	        this.x = e.getX();
	        this.y = e.getY();
	        this.r = (this.x / 50);
	        this.c = (this.y / 50);
	        if ((this.x - this.r * 50 > 6) && ((this.r + 1) * 50 - this.x > 6) && (this.y - this.c * 50 > 6) && ((this.c + 1) * 50 - this.y > 6) && (Main.this.check[r][c] == 1))
	        {//�Ƿ��ڵ����Χ�ڣ����ӱ���6���ز��ܵ㣬�������
	        //��� �����岽 check�����Ƿ��е���ĵ�
	          if (Main.this.chess == 1)
	            Main.this.g.drawImage(Main.this.blackchess.getImage(), this.r * 50 + 2, this.c * 50 + 2, 46, 46, null);
	          else if (Main.this.chess == 0)
	            Main.this.g.drawImage(Main.this.whitechess.getImage(), this.r * 50 + 2, this.c * 50 + 2, 46, 46, null);
	          Main.this.box[this.r][this.c] = chess;

	          MLog.i("��ң�  " + this.r + "   " + this.c);
	          Main.this.flipchess(this.r, this.c);
	          Main.this.countChess();
	          Main.this.chess = (1 - Main.this.chess);

	          this.flag = true;
	        }
	        if (this.flag)
	        {
	          if (!Main.this.checkLaychess())
	          {
	            if (Main.this.chess == BLACK)
	              MLog.i("����û�еط���������");
	            else
	              MLog.i("����û�еط���������");
	            Main.this.chess = (1 - Main.this.chess);
	            if (!Main.this.checkLaychess())
	            {
	              Main.this.chess = (1 - Main.this.chess);
	              MLog.i("˫����û�еط���������");
	              if (Main.this.blacknumber > Main.this.whitenumber)
	                JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "��������ʤ��");
	              else if (Main.this.blacknumber < Main.this.whitenumber)
	                JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "��������ʤ��");
	              else
	                JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "����˫��սƽ��");
	              Main.this.end = true;
	            }
	            else if (Main.this.chess == 1) {
	              Main.this.g_nowchess.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	            } else if (Main.this.chess == 0) {
	              Main.this.g_nowchess.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	            }
	          }
	          else
	          {
	            if (Main.this.chess == 1)
	              Main.this.g_nowchess.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	            else if (Main.this.chess == 0)
	              Main.this.g_nowchess.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	            if (Main.this.AI)
	            {
	              this.bot_continue = true;
	              while (this.bot_continue)
	              {
	                Main.this.bot_judge();
	                MLog.i("bot_judge= "+Main.this.bot_x + "   " + Main.this.bot_y);
	                Main.this.checkLaychess();
	                if (Main.this.chess == 1)
	                  Main.this.g.drawImage(Main.this.blackchess.getImage(), Main.this.bot_x * 50 + 2, Main.this.bot_y * 50 + 2, 46, 46, null);
	                else if (Main.this.chess == 0)
	                  Main.this.g.drawImage(Main.this.whitechess.getImage(), Main.this.bot_x * 50 + 2, Main.this.bot_y * 50 + 2, 46, 46, null);
	                Main.this.box[bot_x][bot_y] = chess;

	                Main.this.flipchess(Main.this.bot_x, Main.this.bot_y);
	                Main.this.countChess();
	                Main.this.chess = (1 - Main.this.chess);
	                if (!Main.this.checkLaychess())
	                {
	                  if (Main.this.chess == 1)
	                    MLog.i("����û�еط���������");
	                  else
	                    MLog.i("����û�еط���������");
	                  Main.this.chess = (1 - Main.this.chess);
	                  if (Main.this.checkLaychess())
	                    continue;
	                  this.bot_continue = false;
	                  MLog.i("˫����û�еط���������");
	                  if (Main.this.blacknumber > Main.this.whitenumber)
	                    JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "��������ʤ��");
	                  else if (Main.this.blacknumber < Main.this.whitenumber)
	                    JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "��������ʤ��");
	                  else
	                    JOptionPane.showMessageDialog(null, "���ս����:����" + Main.this.blacknumber + "��������" + Main.this.whitenumber + "����˫��սƽ��");
	                  Main.this.end = true;
	                }
	                else
	                {
	                  if (Main.this.chess == 1)
	                    Main.this.g_nowchess.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	                  else if (Main.this.chess == 0)
	                    Main.this.g_nowchess.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	                  drawCheckPoint();
	                  this.bot_continue = false;
	                }
	              }
	            }
	          }
	        }
	      }
	    };
	    this.jp_play.addMouseListener(laychess_ml);

	    this.choose = new JFrame();
	    this.choose.setSize(new Dimension(60, 130));
	    this.choose.setTitle("������Ϸ����");
	    this.choose.setResizable(false);
	    this.choose.setDefaultCloseOperation(2);
	    this.choose.setLayout(new FlowLayout(1));
	    this.choose.setVisible(false);

	    this.player_first = new JRadioButton("�������");
	    this.bot_first = new JRadioButton("��������");
	    this.btnGroup.add(this.player_first);
	    this.btnGroup.add(this.bot_first);
	    this.player_first.setSelected(true);

	    this.choose.add(this.player_first);
	    this.choose.add(this.bot_first);

	    JButton sure = new JButton("ȷ��");
	    sure.setMargin(new Insets(0, 0, 0, 0));
	    sure.setPreferredSize(new Dimension(40, 25));
	    this.choose.add(sure);

	    sure.addActionListener(
	      new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        Main.this.choose.setVisible(false);
	        if (Main.this.bot_first.isSelected())
	        {
	          Main.this.bot_x = 3;
	          Main.this.bot_y = 2;
	          Main.this.box[Main.this.bot_x][Main.this.bot_y] = chess;
	          Main.this.g.drawImage(Main.this.blackchess.getImage(), Main.this.bot_x * 50 + 2, Main.this.bot_y * 50 + 2, 46, 46, null);
	          Main.this.box[Main.this.bot_x][(Main.this.bot_y + 1)] = chess;
	          Main.this.g.drawImage(Main.this.blackchess.getImage(), Main.this.bot_x * 50 + 2, (Main.this.bot_y + 1) * 50 + 2, 46, 46, null);
	          Main.this.countChess();
	          Main.this.chess = (1 - Main.this.chess);
	          if (Main.this.chess == 1)
	            Main.this.g_nowchess.drawImage(Main.this.blackchess.getImage(), 0, 0, 46, 46, null);
	          else if (Main.this.chess == 0)
	            Main.this.g_nowchess.drawImage(Main.this.whitechess.getImage(), 0, 0, 46, 46, null);
	          Main.this.checkLaychess();
	          drawCheckPoint();
	        }
	      }
	    });
	    setVisible(true);
	    this.g = ((Graphics2D)this.jp_play.getGraphics());
	    this.g_nowchess = ((Graphics2D)this.jp_nowchess.getGraphics());

	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++)
	        this.box[i][j] = SPACE;
	    this.box[4][4] = this.box[3][3] = WHITE;
	    this.box[4][3] = this.box[3][4] = BLACK;
	    this.chess = 1;

	    this.AI = true;
	    this.end = true;
	    
	  }

	  public void setInit()
	  {
	    this.end = false;
	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++)
	        this.box[i][j] = SPACE;
	    this.box[4][4] = this.box[3][3] = WHITE;
	    this.box[4][3] = this.box[3][4] = BLACK;
	    this.chess = BLACK;
	    countChess();
	    checkLaychess();

	    this.g.drawImage(this.chessboard.getImage(), 0, 0, this.jp_play.getWidth(), this.jp_play.getHeight(), null);
	    for (int i = 0; i < this.box.length; i++) {
	      for (int j = 0; j < this.box[i].length; j++)
	      {
	        if (this.box[i][j] < 0)
	          continue;
	        if (this.box[i][j] == BLACK)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        if (this.box[i][j] == WHITE) {
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        }
	      }
	    }
	    this.g_nowchess.drawImage(this.blackchess.getImage(), 0, 0, 46, 46, null);
	  }

	  /**
	   * ����ǿ������壬���ÿ���������{@link #check}
	   * @return �Ƿ������
	   */
	  public boolean checkLaychess()
	  {
	    boolean ok = false;
	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++)
	      {
	        this.check[i][j] = 0;
	        if (this.box[i][j] != SPACE)
	        {//��Ϊ������
	          continue;
	        }

	        if (i != 0)
	        {//�жϸõ���������Ƿ����
	          for (int k = i - 1; (k > 0) && (this.box[k][j] == 1 - this.chess); ){
	        	  k--;
	         
	          if ((k != i - 1) && (this.box[k][j] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.up[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.up[i][j] = 0;
	          }
	          }
	        }
	        if (i != 7)
	        {//�жϸõ���������Ƿ����
	          for (int k = i + 1; (k < 7) && (this.box[k][j] == 1 - this.chess); ){
	        	  k++;
	          if ((k != i + 1) && (this.box[k][j] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.down[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.down[i][j] = 0;
	          }
	        }
	        }
	        if (j != 0)
	        {//�жϸõ��������Ƿ����
	          for (int k = j - 1; (k > 0) && (this.box[i][k] == 1 - this.chess); ){
	        	  k--;
	          if ((k != j - 1) && (this.box[i][k] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.left[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.left[i][j] = 0;
	          }
	          }
	        }
	        if (j != 7)
	        {//�жϸõ���Ҳ����Ƿ����
	          for (int k = j + 1; (k < 7) && (this.box[i][k] == 1 - this.chess); ){
	        	  k++;
	          if ((k != j + 1) && (this.box[i][k] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.right[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.right[i][j] = 0;
	          }
	        }
	        }
	        if ((i != 0) && (j != 0))
	        {
	          int k = i - 1; 
	          for (int m = j - 1; (k > 0) && (m > 0) && (this.box[k][m] == 1 - this.chess);){
	        	 k--;
	        	 m--;
	          if ((k != i - 1) && (this.box[k][m] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.upleft[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.upleft[i][j] = 0;
	          }
	        }
	        }
	        if ((i != 0) && (j != 7))
	        {
	          int k = i - 1; 
	          for (int m = j + 1; (k > 0) && (m < 7) && (this.box[k][m] == 1 - this.chess); ){
	        	  k--;
	        	  m++;
	          if ((k != i - 1) && (this.box[k][m] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.upright[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.upright[i][j] = 0;
	          }
	        }
	        }
	        if ((i != 7) && (j != 0))
	        {//�жϸõ���������Ƿ����
	          int k = i + 1; for (int m = j - 1; (k < 7) && (m > 0) && (this.box[k][m] == 1 - this.chess); ){
	        	  k++;
	        	  m--;
	          if ((k != i + 1) && (this.box[k][m] == this.chess))
	          {
	            this.check[i][j] = 1;
	            this.downleft[i][j] = 1;
	            ok = true;
	          }
	          else {
	            this.downleft[i][j] = 0;
	          }
	        }
	        }
	        if ((i == 7) || (j == 7))
	          continue;
	        int k = i + 1; for (int m = j + 1; (k < 7) && (m < 7) && (this.box[k][m] == 1 - this.chess); ){
	        	k++;
	        	m++;
	        if ((k != i + 1) && (this.box[k][m] == this.chess))
	        {
	          this.check[i][j] = 1;
	          this.downright[i][j] = 1;
	          ok = true;
	        }
	        else {
	          this.downright[i][j] = 0;
	        }
	      }
	      }
	    return ok;
	  }
	  /**
	   * ��ת����
	   * @param r ѡ�е�x����
	   * @param c ѡ�е�Y����
	   */
	  public void flipchess(int r, int c)
	  {
	    if (this.up[r][c] == 1)
	    {
	      for (int i = r - 1; this.box[i][c] != this.chess; i--)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, c * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, c * 50 + 2, 46, 46, null);
	        this.box[i][c] = this.chess;
	      }
	    }

	    if (this.down[r][c] == 1)
	    {
	      for (int i = r + 1; this.box[i][c] != this.chess; i++)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, c * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, c * 50 + 2, 46, 46, null);
	        this.box[i][c] = this.chess;
	      }
	    }

	    if (this.left[r][c] == 1)
	    {
	      for (int j = c - 1; this.box[r][j] != this.chess; j--)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), r * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), r * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[r][j] = this.chess;
	      }
	    }

	    if (this.right[r][c] == 1)
	    {
	      for (int j = c + 1; this.box[r][j] != this.chess; j++)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), r * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), r * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[r][j] = this.chess;
	      }
	    }

	    if (this.upleft[r][c] == 1)
	    {
	      int i = r - 1; for (int j = c - 1; this.box[i][j] != this.chess; j--)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[i][j] = this.chess;

	        i--;
	      }

	    }

	    if (this.upright[r][c] == 1)
	    {
	      int i = r - 1; for (int j = c + 1; this.box[i][j] != this.chess; j++)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[i][j] = this.chess;

	        i--;
	      }

	    }

	    if (this.downleft[r][c] == 1)
	    {
	      int i = r + 1; for (int j = c - 1; this.box[i][j] != this.chess; j--)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[i][j] = this.chess;

	        i++;
	      }

	    }

	    if (this.downright[r][c] == 1)
	    {
	      int i = r + 1; for (int j = c + 1; this.box[i][j] != this.chess; j++)
	      {
	        if (this.chess == 1)
	          this.g.drawImage(this.blackchess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        else if (this.chess == 0)
	          this.g.drawImage(this.whitechess.getImage(), i * 50 + 2, j * 50 + 2, 46, 46, null);
	        this.box[i][j] = this.chess;

	        i++;
	      }
	    }
	  }
	  /**
	   * ��������
	   */
	  public void countChess()
	  {
	    this.blacknumber = this.whitenumber = 0;
	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++)
	      {
	        if (this.box[i][j] == 1)
	          this.blacknumber += 1;
	        else if (this.box[i][j] == 0)
	          this.whitenumber += 1;
	      }
	    MLog.i("���ں�����" + this.blacknumber + "����������" + this.whitenumber + "��");
	  }
	  /**
	   * �ж��Ƿ������ (i,j)������
	   * @param i
	   * @param j
	   * @param chessboard
	   * @param color
	   * @return �ж��Ƿ���� ����
	   */
	  public boolean judgeLaychess(int i, int j, int[][] chessboard, int color)
	  {
	    boolean ok = false;
	    if (chessboard[i][j] != SPACE) {
	      return ok;
	    }
	    if (i != 0)
	    {
	      for (int k = i - 1; (k > 0) && (chessboard[k][j] == 1 - color); ){
	    	  k--;
	      if ((k != i - 1) && (chessboard[k][j] == color))
	      {//�жϸõ���������Ƿ����
	        this.up[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.up[i][j] = 0;
	      }
	      }
	    }
	    if (i != 7)
	    {
	      for (int k = i + 1; (k < 7) && (chessboard[k][j] == 1 - color); ){
	    	  k++;
	      if ((k != i + 1) && (chessboard[k][j] == color))
	      {//�жϸõ���������Ƿ����
	        this.down[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.down[i][j] = 0;
	      }
	      }
	    }
	    if (j != 0)
	    {
	      for (int k = j - 1; (k > 0) && (chessboard[i][k] == 1 - color); ){
	    	  k--;
	      if ((k != j - 1) && (chessboard[i][k] == color))
	      {
	        this.left[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.left[i][j] = 0;
	      }
	      }
	    }
	    if (j != 7)
	    {
	      for (int k = j + 1; (k < 7) && (chessboard[i][k] == 1 - color); ){
	    	  k++;
	      if ((k != j + 1) && (chessboard[i][k] == color))
	      {
	        this.right[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.right[i][j] = 0;
	      }
	      }
	    }
	    if ((i != 0) && (j != 0))
	    {
	      int k = i - 1; 
	      for (int m = j - 1; (k > 0) && (m > 0) && (chessboard[k][m] == 1 - color); ){
	    	  k--;
	    	  m--;
	      if ((k != i - 1) && (chessboard[k][m] == color))
	      {
	        this.upleft[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.upleft[i][j] = 0;
	      }
	    }
	    }
	    if ((i != 0) && (j != 7))
	    {
	      int k = i - 1; 
	      for (int m = j + 1; (k > 0) && (m < 7) && (chessboard[k][m] == 1 - color);){
	    	  k--;
	    	  m++;
	      if ((k != i - 1) && (chessboard[k][m] == color))
	      {
	        this.upright[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.upright[i][j] = 0;
	      }
	    }
	    }
	    if ((i != 7) && (j != 0))
	    {
	      int k = i + 1; for (int m = j - 1; (k < 7) && (m > 0) && (chessboard[k][m] == 1 - color); ){
	    	  k++;
	    	  m--;
	      if ((k != i + 1) && (chessboard[k][m] == color))
	      {
	        this.downleft[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.downleft[i][j] = 0;
	      }
	    }
	    }
	    if ((i != 7) && (j != 7))
	    {
	      int k = i + 1; for (int m = j + 1; (k < 7) && (m < 7) && (chessboard[k][m] == 1 - color); ){
	    	  k++;
	    	  m++;
	      if ((k != i + 1) && (chessboard[k][m] == color))
	      {
	        this.downright[i][j] = 1;
	        ok = true;
	      }
	      else {
	        this.downright[i][j] = 0;
	      }
	    }
	    }
	    return ok;
	  }

	  /**
	   * �жϾ���<br/>
	   * �����Լ������������Ƿ����������Է������������Ƿ�������<br>
	   * �˴��Ƿ���Ż�Ϊ ��Χ8��λ�ö��жϣ���Ϊб��Ҳ�ǿ�������
	   * @param chessboard
	   * @return ���ؾ���
	   */
	  public int judgeStatic(int[][] chessboard)
	  {
	    int ans = 0;

	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++)
	      {
	        boolean flag = false;
	        if (chessboard[i][j] == this.chess)
	        {
	        //TODO �˴�ֻ�ж����������ң��Ƿ���Ҫ�ж���Χ8�������Ϊ׼ȷ
	          if ((i > 0) && (chessboard[(i - 1)][j] == SPACE))
	            flag = true;
	          else if ((i < 7) && (chessboard[(i + 1)][j] == SPACE))
	            flag = true;
	          else if ((j > 0) && (chessboard[i][(j - 1)] == SPACE))
	            flag = true;
	          else if ((j < 7) && (chessboard[i][(j + 1)] == SPACE))
	            flag = true;
	          if (flag)
	            ans--;
	        } else {
	          if (chessboard[i][j] != 1 - this.chess)
	            continue;
	          if ((i > 0) && (chessboard[(i - 1)][j] == SPACE))
	            flag = true;
	          else if ((i < 7) && (chessboard[(i + 1)][j] == SPACE))
	            flag = true;
	          else if ((j > 0) && (chessboard[i][(j - 1)] == SPACE))
	            flag = true;
	          else if ((j < 7) && (chessboard[i][(j + 1)] == SPACE))
	            flag = true;
	          if (flag)
	            ans++;
	        }
	      }
	    return ans;
	  }

	 /**
	  * �ؼ�����<br>
	  * �ж������������<br>
	  * ���ݹ�ʽ  ans = p1 * a1 + p2 * a2;<br>
	  * p1 = 3;   p2 = 7;<br>
	  * a1 ÿ��λ�õ�Ȩֵ��<br>
	  * a2  ��Ե�ӵ�����<br>
	  * @param color ������ɫ
	  * @param branches ��֧��
	  * @param chessboard ����
	  * @param stop �Ƿ�����
	  * @param a ��Сֵ
	  * @param b ���ֵ
	  * @param fa �²�����ɫ
	  * @return ��ֵ
	  */
	  public int dfs(int color, int branches, int[][] chessboard, boolean stop, int a, int b, int fa)
	  {
	    if (branches == 0)
	    {//��֧��Ϊ0 ���� ��ʽ���� ��ֵ
	      int a1 = 0;//ÿ��λ�õ�Ȩֵ��
	      for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++)
	        {
	          if (chessboard[i][j] == this.chess)
	            a1 += this.cellpoints[i][j];
	          else if (chessboard[i][j] == 1 - this.chess) {
	            a1 -= this.cellpoints[i][j];
	          }
	        }
	      }
	      int a2 = judgeStatic(chessboard);//��Ե�ӵ�����

	      int p1 = 3; int p2 = 7;

	      int ans = p1 * a1 + p2 * a2;

	      return ans;
	    }
	    //��֦�㷨
	    int min = this.INF;
	    int max = -this.INF;

	    int rear = 0;//�����µ�����
	    int[] q_x = new int[64];//������x��
	    int[] q_y = new int[64];//������y��
	    int[] q_up = new int[64];
	    int[] q_down = new int[64];
	    int[] q_left = new int[64];
	    int[] q_right = new int[64];
	    int[] q_upleft = new int[64];
	    int[] q_upright = new int[64];
	    int[] q_downleft = new int[64];
	    int[] q_downright = new int[64];

	    //�����Ǽ������п����µ����
	    for (int i = 0; i < 8; i++) {
	      for (int j = 0; j < 8; j++)
	      {
	        if (chessboard[i][j] != SPACE) {
	          continue;
	        }
	        boolean ok = false;

	        if (i != 0)
	        {
	          for (int k = i - 1; (k > 0) && (chessboard[k][j] == 1 - color); ){
	        	  k--;
	          if ((k != i - 1) && (chessboard[k][j] == color))
	          {
	            q_up[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_up[rear] = 0;
	          }
	          }
	        }
	        if (i != 7)
	        {
	          for (int k = i + 1; (k < 7) && (chessboard[k][j] == 1 - color); ){
	        	  k++;
	          if ((k != i + 1) && (chessboard[k][j] == color))
	          {
	            q_down[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_down[rear] = 0;
	          }
	          }
	        }
	        if (j != 0)
	        {
	          for (int k = j - 1; (k > 0) && (chessboard[i][k] == 1 - color); ){
	        	  k--;
	          if ((k != j - 1) && (chessboard[i][k] == color))
	          {
	            q_left[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_left[rear] = 0;
	          }
	          }
	        }
	        if (j != 7)
	        {
	          for (int k = j + 1; (k < 7) && (chessboard[i][k] == 1 - color); ){
	        	  k++;
	          if ((k != j + 1) && (chessboard[i][k] == color))
	          {
	            q_right[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_right[rear] = 0;
	          }
	          }
	        }
	        if ((i != 0) && (j != 0))
	        {
	          int k = i - 1; 
	          for (int m = j - 1; (k > 0) && (m > 0) && (chessboard[k][m] == 1 - color);){
	        	  k--;
	        	  m--;
	          if ((k != i - 1) && (chessboard[k][m] == color))
	          {
	            q_upleft[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_upleft[rear] = 0;
	          }
	          }
	        }
	        if ((i != 0) && (j != 7))
	        {
	          int k = i - 1; 
	          for (int m = j + 1; (k > 0) && (m < 7) && (chessboard[k][m] == 1 - color);){
	        	  k--;
	        	  m++;
	          if ((k != i - 1) && (chessboard[k][m] == color))
	          {
	            q_upright[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_upright[rear] = 0;
	          }
	          }
	        }
	        if ((i != 7) && (j != 0))
	        {
	          int k = i + 1; 
	          for (int m = j - 1; (k < 7) && (m > 0) && (chessboard[k][m] == 1 - color); ){
	        	  k++;
	        	  m--;
	          if ((k != i + 1) && (chessboard[k][m] == color))
	          {
	            q_downleft[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_downleft[rear] = 0;
	          }
	        }
	        }
	        if ((i != 7) && (j != 7))
	        {
	          int k = i + 1; 
	          for (int m = j + 1; (k < 7) && (m < 7) && (chessboard[k][m] == 1 - color); ){
	        	  k++;
	        	  m++;
	          if ((k != i + 1) && (chessboard[k][m] == color))
	          {
	            q_downright[rear] = 1;
	            ok = true;
	          }
	          else {
	            q_downright[rear] = 0;
	          }
	        }
	        }
	        if (!ok)
	          continue;
	        q_x[rear] = i;
	        q_y[rear] = j;
	        rear++;
	      }

	    }
	    int index=0;
	    //�ж�ÿ�����������С��ֵ
	    for (int q = 0; q < rear; q++)
	    {
	      int[][] board = new int[8][8];
	      for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++)
	          board[i][j] = chessboard[i][j];
	      }
	      int i = q_x[q]; int j = q_y[q];

	      board[i][j] = color;

	      if (q_up[q] == 1) {
	        int r = i - 1; for (int c = j; board[r][c] != color; r--)
	          board[r][c] = color;
	      }
	      if (q_down[q] == 1) {
	        int r = i + 1; for (int c = j; board[r][c] != color; r++)
	          board[r][c] = color;
	      }
	      if (q_left[q] == 1) {
	        int r = i; for (int c = j - 1; board[r][c] != color; c--)
	          board[r][c] = color;
	      }
	      if (q_right[q] == 1) {
	        int r = i; for (int c = j + 1; board[r][c] != color; c++)
	          board[r][c] = color;
	      }
	      if (q_upleft[q] == 1) {
	        int r = i - 1; for (int c = j - 1; board[r][c] != color; c--) {
	          board[r][c] = color;

	          r--;
	        }
	      }
	      if (q_upright[q] == 1) {
	        int r = i - 1; for (int c = j + 1; board[r][c] != color; c++) {
	          board[r][c] = color;

	          r--;
	        }
	      }
	      if (q_downleft[q] == 1) {
	        int r = i + 1; for (int c = j - 1; board[r][c] != color; c--) {
	          board[r][c] = color;

	          r++;
	        }
	      }
	      if (q_downright[q] == 1) {
	        int r = i + 1; for (int c = j + 1; board[r][c] != color; c++) {
	          board[r][c] = color;

	          r++;
	        }
	      }
	      int temp = dfs(1 - color, branches / rear, board, false, a, b, color);
	      if (fa == this.chess)
	      {
	        if (color == 1 - this.chess)
	        {
	          if (temp < b)
	          {
	            if (temp <= a)
	              return temp;
	            b = temp;
	          }
	          if (temp < min)
	            min = temp;
	        } else {
	          if (color != this.chess)
	            continue;
	          if (temp > a)
	          {
	            if (temp > b)
	              b = temp;
	            a = temp;
	          }
	          if (temp > max)
	            max = temp;
	        }
	      } else {
	        if (fa != 1 - this.chess)
	          continue;
	        if (color == this.chess)
	        {
	          if (temp > a)
	          {
	            if (temp >= b)
	              return temp;
	            a = temp;
	          }
	          if (temp > max)
	            max = temp;
	        } else {
	          if (color != 1 - this.chess)
	            continue;
	          if (temp < b)
	          {
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
	    //����������
	    if (rear == 0)
	    {//�ж϶Է�����
	      if (!stop) {
	        return dfs(fa, branches, chessboard, true, a, b, fa);
	      }
	      //�������ʱ���ж�˫��������
	      int bot_chess = 0; int player_chess = 0;
	      for (int i = 0; i < 8; i++)
	        for (int j = 0; j < 8; j++)
	        {
	          if (chessboard[i][j] == this.chess)
	            bot_chess++;
	          else if (chessboard[i][j] == 1 - this.chess)
	            player_chess++;
	        }
	      if (bot_chess > player_chess){
	        return this.INF / 10;
	      }else{
	        return -this.INF / 10;
	      }
	    }
	    //����Լ�����ȡ��󣬶Է���ȡ��С��ֵ
	    int ans;
	    if (color == 1 - this.chess)
	      ans = min;
	    else
	      ans = max;
	    return ans;
	  }

	  /**
	   * AI �ж� �ĸ�����ֵ���
	   */
	  public void bot_judge()
	  {
	    int max_value = -this.INF;
	    int a = -this.INF; int b = this.INF;
	    this.bot_x = (this.bot_y = SPACE);
	    int index=0;
	    for (int i = 0; i < 8; i++)
	      for (int j = 0; j < 8; j++) {
	        if (!judgeLaychess(i, j, this.box, this.chess))
	          continue;
	        int[][] board = new int[8][8];
	        for (int p = 0; p < 8; p++)
	          for (int q = 0; q < 8; q++)
	            board[p][q] = this.box[p][q];
	        int color = this.chess;

	        board[i][j] = color;

	        if (this.up[i][j] == 1) {
	          int r = i - 1; for (int c = j; board[r][c] != color; r--)
	            board[r][c] = color;
	        }
	        if (this.down[i][j] == 1) {
	          int r = i + 1; for (int c = j; board[r][c] != color; r++)
	            board[r][c] = color;
	        }
	        if (this.left[i][j] == 1) {
	          int r = i; for (int c = j - 1; board[r][c] != color; c--)
	            board[r][c] = color;
	        }
	        if (this.right[i][j] == 1) {
	          int r = i; for (int c = j + 1; board[r][c] != color; c++)
	            board[r][c] = color;
	        }
	        if (this.upleft[i][j] == 1) {
	          int r = i - 1; for (int c = j - 1; board[r][c] != color; c--) {
	            board[r][c] = color;

	            r--;
	          }
	        }
	        if (this.upright[i][j] == 1) {
	          int r = i - 1; for (int c = j + 1; board[r][c] != color; c++) {
	            board[r][c] = color;

	            r--;
	          }
	        }
	        if (this.downleft[i][j] == 1) {
	          int r = i + 1; for (int c = j - 1; board[r][c] != color; c--) {
	            board[r][c] = color;

	            r++;
	          }
	        }
	        if (this.downright[i][j] == 1) {
	          int r = i + 1; for (int c = j + 1; board[r][c] != color; c++) {
	            board[r][c] = color;

	            r++;
	          }
	        }
	        int temp = dfs(1 - color, 10, board, false, a, b, color);
	        MLog.i("1branches:"+index+" ans="+temp+" x:"+i+" y:"+j+" color:"+(color==BLACK?"��":"��"));
	        index++;
	        if (temp > a)
	          a = temp;
	        if (temp <= max_value)
	          continue;
	        max_value = temp;
	        this.bot_x = i;
	        this.bot_y = j;
	        
	      }
	  }
	  
	  private void drawCheckPoint(){
		     for(int i=0;i<8;i++){{
		    	 for(int j=0;j<8;j++){
		    		 if(check[i][j]==1){
		    			 g.setColor(Color.green);
		    			 g.fillOval(i* 50 + 20, j* 50 + 20, 10, 10);
		    		 }
		    		 
		    	 }
		     }
         }
	  }
	  
	}