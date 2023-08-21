package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.OutputStream;
import java.util.List;

public class PERSISTCommand implements Command{

    List<String> args;
    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public int run(OutputStream outputStream) throws Exception {
        if(args.size()==0){
            Protocol.getErrMsg(outputStream,"args error");
            return 0;
        }
        String Strategy = args.get(0);
        Database.getInstance().setPersist(Strategy);
        Protocol.getOKMsg(outputStream);
        return 1;
    }

    @Override
    public void run() {
        String Strategy = args.get(0);
        Database.getInstance().setPersist(Strategy);
    }
}
