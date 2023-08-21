package coreCode.commands;

import java.io.OutputStream;
import java.util.List;

public interface Command {
    //<key,values>

    /**
     * for example: "key":key "value":value
     * @param args
     */
    void setArgs(List<String> args);
    int run(OutputStream outputStream) throws Exception;
    void run();
}
