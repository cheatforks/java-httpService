package com.httpServices.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MySRequest {
	private InputStream input;
	private String METHOD = "GET";
	private HashMap<String,String> POSTPARAM;
	private String HOST;
	private String URI;
	
    public MySRequest(InputStream input){  
        this.input = input; 
        getRequestContent();
    }
    
    public void getRequestContent(){
    	getRequestContent2(input);
    	/*System.out.println("------------------------");
    	System.out.println("方法->"+getMethod());
    	System.out.println("URI->"+getUri());
    	System.out.println("URL->"+getUrl());
    	if(getPostParam() !=null){	
    		System.out.println("post参数->"+getPostParam().toString());
    	}*/
    }
    
    /**
     * 获得所有请求内容
     * @throws IOException 
     */
    public StringBuffer getRequestContent(InputStream socketInput){
    	StringBuffer requestContent = new StringBuffer();
    	byte[] bs = new byte[4096];
    	int c = 0;
		try {
			c = socketInput.read(bs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for(int i = 0;i<c;i++){
    		requestContent.append((char)bs[i]);
    	}
    	return requestContent;
    }
    
	public String getRequestContent2(InputStream socketInput){//System.out.println(socketInput);
    	StringBuffer requestContent = new StringBuffer();
    	BufferedReader br=new BufferedReader(new InputStreamReader(socketInput));
    	
    	int contentLength = 0;
    	try {
			String requestHeader;
			while((requestHeader = br.readLine()) != null && !requestHeader.isEmpty()){
				requestContent.append(requestHeader).append("\r\n");//请求头
				
				if(requestHeader.startsWith("GET")){
					setMethod("GET");
					String uri = sUri(requestHeader);
					setUri(uri.trim());
					
				}else if(requestHeader.startsWith("POST")){
					setMethod("POST");
					String uri = sUri(requestHeader);
					setUri(uri.trim());
				}

                if(requestHeader.startsWith("Content-Length")){
                    int begin=requestHeader.indexOf("Content-Length:")+"Content-Length:".length();
                    String postParamterLength=requestHeader.substring(begin).trim();
                    contentLength=Integer.parseInt(postParamterLength);
                    //System.out.println("POST参数长度是："+Integer.parseInt(postParamterLength));
                }
                if(requestHeader.startsWith("Host:")){
                	int hi=requestHeader.indexOf("Host:")+"Host:".length();
                    String host=requestHeader.substring(hi).trim();
                    setHost(host);
                }
                

    		}
			
            StringBuffer postParam=new StringBuffer();
            if(contentLength>0){
                for (int i = 0; i < contentLength; i++) {
                	postParam.append((char)br.read());
                }
                setPostParam(String.valueOf(postParam));
            }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//your log
		}
    	return String.valueOf(requestContent);
    }
	//解析uri
	public String sUri(String requestHeader){
		int uriSIndex = requestHeader.indexOf(" ");
		int uriEIndex = requestHeader.indexOf("HTTP");
		String uri=requestHeader.substring(uriSIndex, uriEIndex);
		return uri;
	}
	
	public void setMethod(String m){
		METHOD = m;
	}
	public String getMethod(){
		return METHOD;
	}
	
	public void setHost(String host){
		HOST = host;
	}
	public String getHost(){
		return HOST;
	}
	
	public void setUri(String uri){
		URI = uri;
	}
	public String getUri(){
		return URI;
	}
	
	public void setUrl(){

	}
	public String getUrl(){
		return HOST+URI ;
	}
	
	public void setPostParam(String kv){
		String pkv[] = null;
		HashMap<String, String> t = new HashMap<String,String>();
		if(kv.indexOf("&") >= 0){
			String paramArray[] = kv.split("&");
			for(String paramkv:paramArray){
				pkv =  paramkv.split("=");
				int i = 0;
				String K = "";
				String V = "";
				for(String p:pkv){
					if(i==0){
						K = p;
					}else if(i==1){
						V = p;
					}
					i++;
				}
				i = 0;
				t.put(K, V);
				pkv = null;
			}
		}else{
			pkv =  kv.split("=");
			t.put(pkv[0], pkv[1]);
		}
		POSTPARAM = t;
	}
	public HashMap<String, String> getPostParam(){
		return POSTPARAM;
	}
}
