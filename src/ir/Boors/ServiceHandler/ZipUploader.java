package ir.Boors.ServiceHandler;
import ir.Boors.Model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ZipUploader implements HttpHandler{
	final static int bufferSize = 1024;

	private static void isToFile(InputStream is , String fileName) throws IOException{
		byte[] buffer = new byte[bufferSize];
		OutputStream os = new FileOutputStream(fileName);
		int bytesRead ;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}	    
		System.out.println("succfully create " + fileName);
		os.close();
	}

	private static void unzip(InputStream is)throws IOException{
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ze = zis.getNextEntry();
		while(ze!=null){
			if(ze.isDirectory()){
				System.out.println("skip " + ze.getName() +" because it is folder");
				continue;
			}
			String currFileName = ze.getName();
			isToFile(zis, currFileName);
			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
		System.out.println("newFiles added successfully");

	}

	@Override
	public void handle(HttpExchange req) throws IOException {
		boolean isSuccess;
		try{
			unzip(req.getRequestBody());
			isSuccess = true ;
		}catch(IOException e){
			e.printStackTrace();
			isSuccess = false ;
		}
		int responseCode;
		String responseMessage;
		if(isSuccess){
			responseCode = 200;
			responseMessage = "successful reconfiguration" ;
		}else{
			responseCode = 404;
			responseMessage = "<h1>" + responseCode+" "+"error while reconfiguration"+"</h1>";
		}
		req.sendResponseHeaders(responseCode,responseMessage.length());
		Headers headers = req.getResponseHeaders();
        headers.add("Date", Calendar.getInstance().getTime().toString());
        headers.add("Content-Type", "text/html");
        OutputStream resOs = req.getResponseBody();
        resOs.write(responseMessage.getBytes());
        resOs.close();
	}
}
