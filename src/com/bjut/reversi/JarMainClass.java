package com.bjut.reversi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * ���������
 * @author liwei
 *
 */
public class JarMainClass {


	public static void main(String[] args){
		MLog.DEBUG = false;
		Player2 player = new Player2();
		String message = "";
		String myMessage = "NO";
		
		while(true){
			message =  getMessage();//���һ����Ϣ
			 
			//������Է���������ٴ����Լ�����һ������·�
			myMessage = player.readMessage(message);
			System.out.println(myMessage);//�����Ϣ
		}//end while(true)
	}
	public static String getMessage(){
		String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//�������ʧ��
		}
		return message;
	}
}
