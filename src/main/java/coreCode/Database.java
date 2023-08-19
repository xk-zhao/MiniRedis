package coreCode;
import coreCode.expireStrategy.Expire;
import coreCode.expireStrategy.LazyDeletionExpire;
import coreCode.expireStrategy.LunXunExpire;
import coreCode.persist.AOFPersist;
import coreCode.persist.Persist;
import coreCode.persist.RDBPersist;
import coreCode.threads.PersistThread;
import coreCode.threads.expireThread;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库
 */
public class Database {
    private static volatile Database instance = null;
    private Map<String,Long> keyAndExpireTime = new ConcurrentHashMap<>();
    private static Set<String> keys = new HashSet<>();



    private static Map<String,String> mapData = new ConcurrentHashMap<>();
    private static Map<String,Map<String,String>> keyFieldData = new ConcurrentHashMap<>();
    private static Expire expireLazy = new LazyDeletionExpire();
    //惰性删除的标识
    private boolean lazyFlag = false;
    private final expireThread t;
    Persist persist = new RDBPersist();
    private final PersistThread persistThread;

    private Database(){
        t = new expireThread(new LunXunExpire());
        t.start();
        persistThread = new PersistThread();
        persistThread.start();
    }





    /**
     * 单例数据库对象
     * 0.0.1
     */

    public static Database getInstance(){
        if(instance==null){
            synchronized (Database.class){
                instance = new Database();
            }
        }
        return instance;
    }

    /**
     * 基本操作
     * 对于 (key,value) 的操作，仅限于value为简单数据
     * 0.0.1
     * */
    public Object[] keys(){
        return keys.toArray();
    }
    public int set(String key,String value){
        mapData.put(key, value);
        keys.add(key);
        return 1;
    }
    public int set(String key,String value,long expireTime){
        mapData.put(key, value);
        keys.add(key);
        keyAndExpireTime.put(key,System.currentTimeMillis() + expireTime);
        return 1;
    }
    public String get(String key){
        if(lazyFlag){
            expireLazy.refreshDatabase(this);
        }
        return mapData.getOrDefault(key, "not exist");
    }
    public int del(String key){
        if (mapData.containsKey(key)){
            mapData.remove(key);
            keys.remove(key);
        }else{
            return 0;
        }
        return 1;
    }
    /**
     * 对于keyFieldValue的操作
     * 0.0.1
     */
    public int hset(String key,Map<String,String> fields){
        if (keyFieldData.containsKey(key)){
            Map<String,String> nowfields = keyFieldData.get(key);
            for (Map.Entry<String,String> field : fields.entrySet()){
                String fieldkey  = field.getKey();
                String fieldvalue = field.getValue();
                nowfields.put(fieldkey,fieldvalue);
            }
        }else {
            keyFieldData.put(key, fields);
            keys.add(key);
        }

        return 1;
    }
    public String hget(String key,String field){
        Map<String,String> fields = keyFieldData.get(key);
        if(fields==null){
            return null;
        }else {
            String res = fields.get(field);
            return res;
        }
    }
    public Set<Map.Entry<String,String>> hget(String key){
        Map<String,String> fields = keyFieldData.get(key);
        if(fields==null){
            return null;
        }else {
            return fields.entrySet();
        }
    }

    /**
     * 0.0.2 过期策略
     */
    public  Map<String,Long> getkeyAndExpireTime(){
        return keyAndExpireTime;
    }

    /**
     * 设置策略 {LAZY,LUNXUN}
     * @param strategy
     * @return
     */
    public Database setStrategy(String strategy) {
        if (strategy.equalsIgnoreCase("LAZY")){
            System.out.println("过期策略已经设置为Lazy");
            lazyFlag = true;
            t.exit = true;
        }else if(strategy.equalsIgnoreCase("Lunxun")){
            lazyFlag = false;
            System.out.println("过期策略已经设置为Lunxun");
            if(t.exit){
                t.exit = false;
            }
        }
        return this;
    }



    /**
     * 持久化
     * 0.0.3
     */
    public static Map<String, String> getMapData() {
        return mapData;
    }
    public Database setPersist(String name){
        if (name.equalsIgnoreCase("rdb")){
            System.out.println("持久化策略已经设置为rdb");
            persistThread.setPersist(new RDBPersist());
        }else if(name.equalsIgnoreCase("aof")){
            lazyFlag = false;
            System.out.println("持久化策略已经设置为aof");
            persistThread.setPersist(new AOFPersist());

        }else if(name.equalsIgnoreCase("mix")){
            System.out.println("持久化策略已经设置为mix");
        }
        return this;
    }


}
