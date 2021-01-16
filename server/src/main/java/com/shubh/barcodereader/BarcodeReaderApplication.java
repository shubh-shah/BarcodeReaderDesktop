package com.shubh.barcodereader;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Robot;

@SpringBootApplication
@Controller
public class BarcodeReaderApplication {
	
	private Robot robot;
	private String password;
	private final int password_length = 13;
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	public String QRPath;

	public BarcodeReaderApplication(){
		QRPath =  System.getProperty("user.home")+"/QRImage.png";
		StringBuilder sb = new StringBuilder(password_length);
		for(int i = 0; i < password_length; i++){
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		password = sb.toString();
		try {
			robot = new Robot(); 
		}
		catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(BarcodeReaderApplication.class);
		builder.headless(false).run(args);
	}

	@PostMapping("/shutdown")
    public void shutdownContext() {
		File QRFile = new File(QRPath); 
		if (QRFile.delete()) { 
			System.out.println("Deleted the file: " + QRFile.getName());
		} else {
			System.out.println("Failed to delete the file.");
		} 
		System.out.println("Exiting System");
		System.exit(0);
	}

	@CrossOrigin
	@ResponseBody
	@GetMapping(value="/")
	public String homepage(){
		return "{\"message\":\"Barcode Reader\"}";
	}

	@ResponseBody
	@PostMapping(value = "/post-barcode", consumes = "application/json")
	public String postBarcode(@RequestBody ReadString request) {
		String req_str = request.getContent().toUpperCase();
		//Check if input is valid
		if(!password.equals(request.getPassword())){
			System.out.println("Got request with mismatched password");
			return "Incorrect Password";
		}
		for (int i = 0; i < req_str.length(); i++){
			if((req_str.charAt(i)<'0' || req_str.charAt(i)>'9') && (req_str.charAt(i)<'A' || req_str.charAt(i)>'Z') && req_str.charAt(i)!=' '){
				System.out.println("Got request with non alphanumeric characters : "+req_str);
				return "Input Invalid";
			}
		}
		//For password check
		if(req_str.equals("")){
			System.out.println("Got a ping");
			return "Success";
		}
		//To recieve keystrokes
		for (int i = 0; i < req_str.length(); i++){
			robot.keyPress(req_str.charAt(i)); 
			robot.keyRelease(req_str.charAt(i)); 	
		}
		robot.keyPress('\n'); 
		robot.keyRelease('\n'); 
		System.out.println("Successfully wrote : "+req_str);
		return "Success";
	}

	@CrossOrigin
	@ResponseBody
	@GetMapping(value = "/generate-qr", produces = "application/json")
	public PairOutput generateQR(){
		PairOutput out = new PairOutput();
		out.path="";
		try{
			//Construct string
			String QRString = "{\"url\":[";
			Enumeration<NetworkInterface> listNI= NetworkInterface.getNetworkInterfaces();
			while(listNI.hasMoreElements()){
				Enumeration<InetAddress> listIP = listNI.nextElement().getInetAddresses();
				while(listIP.hasMoreElements()){
					QRString+="\""+((InetAddress)listIP.nextElement()).getHostAddress()+"\",";
				}
			}
			if(QRString.endsWith(",")){
				QRString = QRString.substring(0, QRString.length()-1);
			}
			QRString+="],\"password\":\""+password+"\"}";
			
			//Construct QR Code
			QRCodeWriter barcodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = barcodeWriter.encode(QRString, BarcodeFormat.QR_CODE, 200, 200);
			
			//Write OR to file
			File outputfile = new File(QRPath);
			out.path = outputfile.getAbsolutePath();
			BufferedImage img = MatrixToImageWriter.toBufferedImage(bitMatrix);
			out.height=img.getHeight();
			out.width=img.getWidth();
			ImageIO.write(img, "png", outputfile);	
			out.status=0;
			out.error="NoError";			
		}
		catch(SocketException e){
			System.out.println("Couldn't find IPs");
			e.printStackTrace();
			out.status=1;
			out.error=e.getClass().getSimpleName();
		}
		catch(WriterException e){
			System.out.println("Couldn't generate QR");
			e.printStackTrace();
			out.status=1;
			out.error=e.getClass().getSimpleName();
		}
		catch(IOException e){
			System.out.println("Couldn't write QR to file");
			e.printStackTrace();
			out.status=1;
			out.error=e.getClass().getSimpleName();
		}
		return out;
	}

}

class PairOutput{
	public int status;
	public String error;
	public String path;
	public int height;
	public int width;
}