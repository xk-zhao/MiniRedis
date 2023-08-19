package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GETCommand implements Command{
    List<String> args;
    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public int run(OutputStream outputStream) throws IOException {
        if (args.size()==0){
            Protocol.getErrMsg(outputStream,"no arg");
            return 0;
        }
        String key  = args.get(0);
        String res = Database.getInstance().get(key);
        if(res.equals("not exist")){
            Protocol.getErrMsg(outputStream,res);
            return 0;
        }else{
            Protocol.getStringMsg(outputStream,res);
        }
        return 1;
    }
}
