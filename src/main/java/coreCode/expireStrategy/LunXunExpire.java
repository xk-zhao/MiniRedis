package coreCode.expireStrategy;

import coreCode.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LunXunExpire implements Expire{

    @Override
    public void refreshDatabase(Database database) {
        if (database.getkeyAndExpireTime().isEmpty()){
            return;
        }
        List<Map.Entry<String,Long>> mapSet = new ArrayList<>(database.getkeyAndExpireTime().entrySet());
        //排序后，小的在前
        mapSet.sort((o1, o2) -> (int) (o1.getValue()-o2.getValue()));
        for (int i = 0;i < mapSet.size();i++){
            long time = mapSet.get(i).getValue();
            if (time <= System.currentTimeMillis()){
                database.getkeyAndExpireTime().remove(mapSet.get(i).getKey());
                database.del(mapSet.get(i).getKey());
            }else {
                break;
            }
        }

    }
    @Override
    public void run(){
        refreshDatabase(Database.getInstance());
    }
}
