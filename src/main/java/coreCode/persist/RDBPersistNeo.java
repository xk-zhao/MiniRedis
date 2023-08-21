package coreCode.persist;

import coreCode.Database;
import coreCode.utils.EntityProto;
import java.io.*;
import java.util.Map;
import java.util.Set;



public class RDBPersistNeo implements Persist{
    private String filePath = "MiniRedisNeo.rdb";
    public RDBPersistNeo(){}
    public RDBPersistNeo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void store() throws IOException {
        Map<String,String> mapData = Database.getMapData();
        File file = new File(filePath);
        if (!file.exists()){
            file.createNewFile();
        }
        Set<Map.Entry<String,String>> values = mapData.entrySet();
        EntityProto.Entities.Builder entitysBuilder= EntityProto.Entities.newBuilder();
        for (Map.Entry<String,String> tt:values){
            EntityProto.Entity entity = EntityProto.Entity.newBuilder()
                    .setKey(tt.getKey())
                    .setValue(tt.getValue())
                    .build();
            entitysBuilder.addEntities(entity);
        }
        EntityProto.Entities entities = entitysBuilder.build();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            entities.writeTo(fos);
        }
    }

    @Override
    public void load() throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        EntityProto.Entities deserializedEntities;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            deserializedEntities = EntityProto.Entities.parseFrom(fis);
        }
        System.out.println("entitys reload");
        for (EntityProto.Entity entity:deserializedEntities.getEntitiesList()){
            System.out.println(entity.getKey()+":"+entity.getValue());
            Database.getInstance().set(entity.getKey(),entity.getValue());
        }
    }
}
