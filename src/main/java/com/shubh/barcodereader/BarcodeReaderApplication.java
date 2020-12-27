package com.shubh.barcodereader;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.awt.Robot;
import java.security.SecureRandom;

@SpringBootApplication
@RestController
public class BarcodeReaderApplication {
	
	private Robot robot;
	private String password;
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	public BarcodeReaderApplication(){
		password = randomStringGenerator(13);
		System.out.println("Password : "+password);
		try {
			robot = new Robot(); 
		}
		catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			System.exit(0);
		}

	}

	private String randomStringGenerator(int len){
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++){
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(BarcodeReaderApplication.class);
		builder.headless(false).run(args);
	}

	@GetMapping("/")
	public String docs() {
		return String.format("Send post request to /post-barcode with json object containg two strings with labels : content and password");
	}

	@PostMapping(value = "/post-barcode", consumes = "application/json")
	public String post_barcode(@RequestBody ReadString request) {
		if(!password.equals(request.getPassword())){
			System.out.println("Got request with mismatched password");
			return "Failure";
		}
		String req_str = request.getContent().toUpperCase();//
		for (int i = 0; i < req_str.length(); i++){
			if((req_str.charAt(i)<'0' || req_str.charAt(i)>'9') && (req_str.charAt(i)<'A' || req_str.charAt(i)>'Z') && req_str.charAt(i)!=' '){
				System.out.println("Got request with non alphanumeric characters : "+req_str);
				return "Failure";
			}
		}
		for (int i = 0; i < req_str.length(); i++){
			robot.keyPress(req_str.charAt(i)); 
			robot.keyRelease(req_str.charAt(i)); 	
		}
		robot.keyPress('\n'); 
		robot.keyRelease('\n'); 
		System.out.println("Successfully wrote : "+req_str);
		return "Success";
	}

}