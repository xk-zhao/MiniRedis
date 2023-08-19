package coreCode;

import coreCode.commands.Command;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 执行命令
 */
public class DoCommand {
    //目前实现的命令
    private static String[] AllCommand = {"SET","GET","DEL","KEYS","HGET","HSET","EXPIRE"};
    public static int exec(String command,OutputStream outputStream) throws Exception {
        //命令全部
        String[] commands = command.split(" ");
        String trueCommand = commands[0];
        if (!Arrays.asList(AllCommand).contains(trueCommand.toUpperCase())){
            Protocol.getErrMsg(outputStream,"no this command");
            return 0;
        }
        //
        /**
         * 反射获取实例
         */
        String className = String.format("coreCode.commands.%sCommand",trueCommand.toUpperCase());
        Class<?> cls = Class.forName(className);
        List<String> args = new ArrayList<>();
        for (int i=1;i< commands.length;i++){
            args.add(commands[i]);
        }
        Command commandInstance = (Command) cls.newInstance();
        commandInstance.setArgs(args);

        /**
         * 执行
         */
        int stateCode = commandInstance.run(outputStream);

        return stateCode;
    }
}
