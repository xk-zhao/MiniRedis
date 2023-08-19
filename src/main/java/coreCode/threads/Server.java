package coreCode.threads;
import coreCode.DoCommand;
import coreCode.Protocol;
import coreCode.persist.Persist;
import coreCode.persist.RDBPersist;
import coreCode.persist.RDBPersistNeo;

import java.io.*;
import java.net.Socket;

/**
 * 服务线程
 */
public class Server implements Runnable{
    private Socket client;
    private Persist persist = new RDBPersistNeo();

    public Server(Socket socket){
        this.client = socket;
    }

    @Override
    public void run(){
        try{
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();
            persist.load();
            while(true){
                String commend = Protocol.getCommand(inputStream);
                System.out.println(commend);
                int stateCode = DoCommand.exec(commend,outputStream);
                if (stateCode==1){
                    System.out.println("success do command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
