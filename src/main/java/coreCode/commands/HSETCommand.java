package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HSETCommand implements Command{
    List<String> args;
    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public int run(OutputStream outputStream) throws Exception {
        /**
         * 形如 (hset) peter age 15 sex man;
         */
        if (args.size()<2||args.size()%2==0){
            Protocol.getErrMsg(outputStream,"arg error");
            return 0;
        }
        String key = args.get(0);
        Map<String,String> fields = new HashMap<>();
        for (int i=1;i<args.size();i=i+2){
            String field = args.get(i);
            String value = args.get(i+1);
            fields.put(field,value);
        }
        Database.getInstance().hset(key,fields);
        Protocol.getOKMsg(outputStream);
        return 1;
    }
}
