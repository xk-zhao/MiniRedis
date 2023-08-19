package coreCode.persist;

import coreCode.Database;
import coreCode.utils.ProtostuffUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class RDBPersist implements Persist{
    private String filePath = "MiniRedis.rdb";
    public RDBPersist(){}
    public RDBPersist(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void store() throws IOException {
        Map<String,String> mapData = Database.getInstance().getMapData();
        File file = new File(filePath);
        if (!file.exists()){
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        Set<Map.Entry<String,String>> values = mapData.entrySet();
        for (Map.Entry<String,String> t:values){
            StringBuffer sb = new StringBuffer();
            sb.append(t.getKey());
            sb.append(":");
            sb.append(t.getValue());
            sb.append("\n");
            outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        }

    }

    @Override
    public void load() throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        if (file.exists()){
            try {
                String str;
                while((str = bufferedReader.readLine())!=null){
                    String[] keyandvalue = str.split(":");
                    System.out.println("正在写入 "+str);
                    Database.getInstance().set(keyandvalue[0],keyandvalue[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputStream.close();
        bufferedReader.close();
    }
}
