package in.unigps.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class TCPServer {

    public static int queueSize = 10;
    public static int minimumPoolSize = 1;
    public static int startPoolSize = 2;
    static Socket client;
    static ServerSocket ss;
    static ExecutorService executor;
    private static int port = 8082;
    public int ClientCount = 1;

    public TCPServer(int startPoolSize, int port) {
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue(queueSize);
        executor = new ThreadPoolExecutor(minimumPoolSize, startPoolSize, 0L, TimeUnit.MILLISECONDS, queue);
    }



    public static void main(String[] args) {

        TCPServer tserver = new TCPServer(startPoolSize, port);
        try {
            tserver.startserver();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void startserver() throws IOException{
        ss = new ServerSocket(port);
        System.out.println("Listening to: " + port);

        while (true){
            client = ss.accept();
            System.out.println("Client " + ClientCount + " Connected");
            executor.execute(new ClientThread(client, ClientCount));
            ClientCount++;
        }
    }


}
