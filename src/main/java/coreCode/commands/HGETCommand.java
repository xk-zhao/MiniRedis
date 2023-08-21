package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HGETCommand implements Command{
    List<String> args;
    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public int run(OutputStream outputStream) throws Exception {
        if (args.size()==0){
            Protocol.getErrMsg(outputStream,"no args");
        }
        if (args.size()>2){
            Protocol.getErrMsg(outputStream,"too much args");
        }
        /**
         * 形如 hget key 获取所有的field
         */
        if (args.size()==1){
            List<String> message = new ArrayList<>();
            String key = args.get(0);
            Set<Map.Entry<String,String>> fields = Database.getInstance().hget(key);
            if (fields==null){
                Protocol.getErrMsg(outputStream,"no this key");
                return 0;
            }
            for (Map.Entry<String,String> field : fields){
                String temp = field.getKey() + ":" + field.getValue();
                message.add(temp);
            }
            Protocol.getArrayMsg(outputStream,message);
            return 1;
        }else if(args.size()==2){
            String key = args.get(0);
            String field = args.get(1);
            String res = Database.getInstance().hget(key,field);
            if (res==null){
                Protocol.getErrMsg(outputStream,"no this key or no this field");
                return 0;
            }
            Protocol.getStringMsg(outputStream,res);
            return 1;
        }

        return 0;
    }

    @Override
    public void run() {

    }
}
