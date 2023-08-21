package coreCode;
import com.sun.org.apache.xpath.internal.operations.Bool;
import coreCode.threads.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;


public class Main {
    //private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        int port = 6379;
        Database database = Database.getInstance();
        Map<Socket, Boolean> hasClosed = new ConcurrentHashMap<>();
        //线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,1L,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ServerSocket serverSocket = new ServerSocket(port);
        //监听
        while(true){
            try{
                System.out.println("wait connect..");
                Socket client = serverSocket.accept();
                System.out.println("connect with " + client.getLocalAddress());
                threadPool.execute(new Server(client));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

}
