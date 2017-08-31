package in.unigps.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    private static int port = 8082;
    private static String host = "localhost";
    private static Socket connection;
    private static boolean connected = true;
    private static Scanner scanner;

    public static void main(String[] args) throws IOException, InterruptedException {
    	try{
        	port = Integer.parseInt(args[0]);    		
    	}catch(NumberFormatException  | ArrayIndexOutOfBoundsException ex){
    	
    	}
        connection = new Socket(host, port);

        final DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
        final DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        System.out.println("Messages To Server							Messages From Server: ");
        new Thread(new Runnable(){

			@Override
			public void run() {
		        scanner = new Scanner(System.in);		        
		        while(connected){
			        String name = scanner.next();
			        try {
			        	dataOutputStream.writeUTF(name);
					} catch (IOException e) {
						System.out.println("Connection broke..closing socket");
		            	connected = false;
			            try {
			                connection.close();
			            }catch (IOException ioe){
			                ioe.printStackTrace();
			            }
						break;
					}		        	
		        }
				
			}}).start();

	        new Thread(new Runnable(){

				@Override
				public void run() {
					while(connected){
				        try {
				        	String response = dataInputStream.readUTF();
							System.out.println("											" + response);
						} catch (IOException e) {
							System.out.println("Connection broke while reading..closing socket");
			            	connected = false;
				            try {
				                scanner.close();
				                connection.close();
				            }catch (Exception ioe){
				                ioe.printStackTrace();
				            }
							break;
						}											
					}
				}}).start();
				
				while(connected){
					Thread.sleep(60000);					
				}
    
    }
}