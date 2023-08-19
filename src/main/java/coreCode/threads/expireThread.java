package coreCode.threads;

import coreCode.expireStrategy.Expire;

public class expireThread extends Thread{
    public boolean exit = false;
    private Expire expire;
    private long TIME = 1000;
    public void setTIME(long TIME) {
        this.TIME = TIME;
    }
    public expireThread(Expire expire){
        this.expire = expire;
    }
    @Override
    public void run(){
        while(true){
            while(!exit){
                try {
                    expire.run();
                    Thread.sleep(TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
