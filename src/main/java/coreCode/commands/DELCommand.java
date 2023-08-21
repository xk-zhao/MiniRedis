package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DELCommand implements Command{
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
        for (int i=0;i<args.size();i++){
            String key  = args.get(i);
            int res = Database.getInstance().del(key);
            if (res == 0){
                Protocol.getErrMsg(outputStream, String.format("not exist:%s", key));
                outputStream.flush();
                return 0;
            }

        }
        Protocol.getOKMsg(outputStream);
        return 1;
    }

    @Override
    public void run() {

    }
}
