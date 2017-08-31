package in.unigps.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

    private Socket client;
    private int clientCount;
    private boolean connected = true;
    private Scanner scanner;

    public ClientThread(Socket client, int clientCount) {

        this.client = client;
        this.clientCount = clientCount;
    }

    public void run(){
        try {
            final DataInputStream dataInputStream = new DataInputStream(this.client.getInputStream());
            final DataOutputStream dataOutputStream = new DataOutputStream(this.client.getOutputStream());
            System.out.println("Messages To Client							Messages From Client: ");
            Thread t = new Thread(new Runnable(){
    			@Override
    			public void run() {
    		        scanner = new Scanner(System.in);
    		        while(connected){
//    		        	System.out.println("To Client:");
    			        try {
        			        String name = scanner.next();
        			        if(name.equals("quit")){
        			        	throw new Exception("");
        			        }
    			        	dataOutputStream.writeUTF(name);
    					} catch (Exception e) {
							System.out.println("Connection broke..closing socket");
							connected = false;
				            try {
				                client.close();
				            }catch (IOException ioe){
//				                ioe.printStackTrace();
				            }
    						break;
    					}		        	
    		        }	
    		        System.out.println("Exiting client writer thread:"+clientCount);
    			}});
            t.setDaemon(false);
              t.start();

    	        new Thread(new Runnable(){
    				@Override
    				public void run() {
    					while(true){
    				        try {
    				        	String response = dataInputStream.readUTF();
    							System.out.println("										" + response);
    						} catch (IOException e) {
    							System.out.println("Connection broke while reading..closing socket");
    							connected = false;
    				            try {
    								scanner = null;
    								System.out.println("Scanner is closed");
    				                client.close();
    				                t.interrupt();
    				                t.join();
    				            }catch (Exception ioe){
//    				                ioe.printStackTrace();
    				            }
    							break;
    						}											
    					}
    					System.out.println("Exiting client reader thread:"+clientCount);
    				}}).start();
				while(connected){
					Thread.sleep(5000);					
				}
				System.out.println("Exiting client thread: "+clientCount);
    	        
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }finally {
            try {
                this.client.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
