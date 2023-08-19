package coreCode.commands;

import coreCode.Database;
import coreCode.Protocol;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class KEYSCommand implements Command{
    @Override
    public void setArgs(List<String> args) {

    }

    @Override
    public int run(OutputStream outputStream) throws Exception {
        Object[] res = Database.getInstance().keys();
        List<Object> list = Arrays.asList(res);
        //StringBuffer sb = new StringBuffer();
        Protocol.getArrayMsg(outputStream,list);
        return 1;
    }
}
