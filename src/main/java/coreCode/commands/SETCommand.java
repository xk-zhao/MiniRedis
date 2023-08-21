package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SETCommand implements Command{
    private List<String>  args;
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
        if(args.size()==2){
            String key = (String) args.get(0);
            String value =(String) args.get(1);
            int stateCode = Database.getInstance().set(key,value);
            if (stateCode==1){
                Protocol.getOKMsg(outputStream);
            }else{
                Protocol.getErrMsg(outputStream,"error");
            }
            return stateCode;
        }else if(args.size()==3){
            String key = (String) args.get(0);
            String value =(String) args.get(1);
            int stateCode = 0;
            long time = 0L;
            try{
                time = Long.parseLong(args.get(2));
                stateCode = Database.getInstance().set(key,value,time*1000);
            }catch (Exception e){
                //不能转化
                Protocol.getErrMsg(outputStream,"expiretime should be a number");
                return 1;
            }
            if (stateCode==1){
                Protocol.getOKMsg(outputStream);
            }else{
                Protocol.getErrMsg(outputStream,"error");
            }
            return stateCode;
        }else{
            Protocol.getErrMsg(outputStream,"args error");
        }
        return 0;
    }

    @Override
    public void run() {
        if(args.size()==2){
            String key = (String) args.get(0);
            String value =(String) args.get(1);
            Database.getInstance().set(key,value);
        }else if(args.size()==3){
            String key = (String) args.get(0);
            String value =(String) args.get(1);
            long time = 0L;
            try{
                time = Long.parseLong(args.get(2));
                Database.getInstance().set(key,value,time*1000);
            }catch (Exception e){
                return;
            }
        }
    }
}
