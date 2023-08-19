package coreCode.threads;

import coreCode.persist.Persist;
import coreCode.persist.RDBPersist;
import coreCode.persist.RDBPersistNeo;

import java.io.IOException;

public class PersistThread extends Thread{


    private Persist persist = new RDBPersistNeo();
    private boolean flag = true;
    private long time = 5L;


    public void setTime(long time) {
        this.time = time;
    }
    public void setPersist(Persist persist) {
        this.persist = persist;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run(){
        while(true){
            while(flag){
                try {
                    persist.store();
                    System.out.println(persist.getClass() + " 保存成功");
                    Thread.sleep(time * 1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
