package com.bjut.reversi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * ģ����������й���
 * @author icmonkey
 *
 */
public class TestPlatform {

	public static void main(String[] args) {
		try{
		String path1= "E:\\��Ŀ\\�ڰ���\\10005-��ΰ_v1.4.jar";//���Գ���·��
		String path2= "E:\\��Ŀ\\�ڰ���\\���Գ���ʹ��˵��\\���Գ���ʹ��˵��\\FoolPlayer.jar";//���Գ���·��
		Runtime runtime = Runtime.getRuntime();  
		Process process = runtime.exec("java -jar "+path1);
		//ȡ��������������� 
		InputStream fis = process.getInputStream(); 
		//��һ�����������ȥ��    
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));   
		String message = "BLACK";
		BufferedWriter fout1 = new BufferedWriter(new OutputStreamWriter(process.getOutputStream())); 
		writeMessage(fout1, message);
		
		runtime = Runtime.getRuntime();  
		process = runtime.exec("java -jar "+path2);
		//ȡ��������������� 
		fis = process.getInputStream(); 
		//��һ�����������ȥ��    
		BufferedReader br2 = new BufferedReader(new InputStreamReader(fis));   
		message = "WHITE";
		BufferedWriter fout2 = new BufferedWriter(new OutputStreamWriter(process.getOutputStream())); 
		writeMessage(fout2, message);
		br2.readLine();
		BufferedReader tBr = br1;
		BufferedWriter tFout = fout2;
		String line;
		String lastLine="";
		boolean black = true;
		Board board  = new Board();
		while(true){
			//���ж�ȡ���������̨    
			
			 line = tBr.readLine();
				 System.out.println((black?"�ڣ�":"�ף�")+line); 
			 if(!"NO".equals(line)){
			 int x=line.charAt(0)-'1', y = line.charAt(1)-'A';
				 if(!board.pieceLegalJudge(x, y, black?IPlayer.BLACK:IPlayer.WHITE, true)){
					 System.out.println("������"); 
					 break;
				 }
			 }else{
				 boolean flag = false;
				 for(int i=0;i<8;i++){
						for(int j=0;j<8;j++){
							if(board.pieceLegalJudge(i, j, black?IPlayer.BLACK:IPlayer.WHITE, false)){
								flag = true;
								System.out.println("���õ㣺"+board.changeCoordinateForm(i, j)); 
							}
						}
					}
				 if(flag){
					 System.out.println("������"); 
					 break; 
				 }
			 }
			 if("NO".equals(lastLine)&&"NO".equals(line))break;
			 writeMessage(tFout,line);
			 tBr = tBr==br1?br2:br1;
			 tFout = tFout == fout1?fout2:fout1;
			 black = !black;
			 lastLine = line;
			 Thread.sleep(500);
		}
		board.showResult();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void writeMessage(BufferedWriter fout,String s){
		try {
			fout.write(s);
			fout.newLine();
			fout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
