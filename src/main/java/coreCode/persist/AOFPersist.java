package coreCode.persist;

import coreCode.Database;
import coreCode.DoCommand;
import coreCode.utils.CommandProto;

import java.io.*;
import java.util.List;

public class AOFPersist implements Persist{
    private String filePath = "MiniRedis.aof";
    private String loadPath = "MiniRedis.aof";
    public AOFPersist(){}
    public AOFPersist(String loadPath){this.loadPath = loadPath;}
    @Override
    public void store() throws IOException {
        List<String> Commands = Database.getInstance().getCommands();
        File file = new File(filePath);
        if (!file.exists()){
            file.createNewFile();
        }
        CommandProto.CommandList.Builder commandsBuilder = CommandProto.CommandList.newBuilder();
        for (String command:Commands){
            CommandProto.Command tt = CommandProto.Command.newBuilder()
                    .setCommand(command)
                    .build();
            commandsBuilder.addCommand(tt);
        }
        CommandProto.CommandList commandList = commandsBuilder.build();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            commandList.writeTo(fos);
        }
    }
    @Override
    public void load() throws Exception {
        File file = new File(loadPath);
        if(!file.exists()){
            file.createNewFile();
        }
        CommandProto.CommandList commandList;
        try (FileInputStream fis = new FileInputStream(file)) {
            commandList = CommandProto.CommandList.parseFrom(fis);
        }
        System.out.println("commands reload");

        for (CommandProto.Command tt:commandList.getCommandList()){
            DoCommand.exec(tt.getCommand());
        }
        System.out.println("hello this is aof load");
    }
}
