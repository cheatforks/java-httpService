package com.httpServices.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MySHttpService {
	private static 	int PORT;
	private  boolean START = true;
	public static void main(String[] args) {
		MySHttpService service = new  MySHttpService();
		service.StartService();
	}

	@SuppressWarnings("resource")
	private void StartService() {
		int port = 8681;
		InputStream socketInput;
		OutputStream socketOutput; 
		setPort(port);
		try {
			ServerSocket serviceSocket = new ServerSocket(PORT);
			while(START){
				 Socket socket = serviceSocket.accept();
				 socketInput = socket.getInputStream();//获取socket输入流 即request
				 MySRequest mySRequest = new MySRequest(socketInput);
				 
	             //发送response
				 socketOutput = socket.getOutputStream();
				 MySResponse mySResponse = new MySResponse(socketOutput);
				 mySResponse.sendResponse(mySRequest);
				 socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("serviceSocket.....");
		}
	}
	
	public void setPort(int port){
		PORT = port;
	}
}
