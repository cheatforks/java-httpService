package com.httpServices.service;

import java.io.OutputStream;
import java.io.PrintWriter;

public class MySResponse{
	private OutputStream SOCKETOUTPUT;
	static{
		//System.out.println("还有很多可以完善的地方，比如可以添加过滤器，自己按需完善");
	}
	public MySResponse(OutputStream socketOutput){
		SOCKETOUTPUT = socketOutput;
	}
	public void sendResponse(MySRequest mySRequest){
        PrintWriter pw=new PrintWriter(SOCKETOUTPUT);
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-type:text/html");
        pw.println();
        pw.println("<h1>访问成功233456！</h1>");
        pw.flush();
	}
}
