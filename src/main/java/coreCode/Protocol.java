package coreCode;

import com.google.common.io.ByteStreams;
import com.sun.org.apache.bcel.internal.generic.RET;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议解析
 *
 * */
public class Protocol {
    public static String getCommand(InputStream is) throws Exception {//通过此类取得命令后的字符串hello 1
        /*
         * 假如在客户端输入的是lpush hello 1，通过Redis的协议将其变为*3\r\n$5\r\nlpush\r\n$5\r\nhello\r\n$1\r\n1\r\n
         * 给服务器的传入的参数是数组List格式.然后通过read(is)将其解析为lpush hello 1，返回Object类型。
         * 将第一个元素取出并删掉。将它变成大写字母，方便匹配类名。
         * 通过命令名LPUSH反射获取类的class对象。判断class对象表示的类是否和Command的子类相同。
         * 是，则返回类的实例化对象。
         * */
        int count = 0;
        while (count == 0) {
            count = is.available();
        }
        byte[] b = new byte[count];
        is.read(b);
        String s = new String(b);
        String[] temp = s.split("\\r?\\n");
        List<String> commands = new ArrayList<>();
        for (int i=0;i<temp.length;i++){
            if(temp[i].charAt(0)=='*'||temp[i].charAt(0)=='$'||temp[i].charAt(0)=='+'||temp[i].charAt(0)=='-'||temp[i].charAt(0)==':'){
                continue;
            }else{
                commands.add(temp[i]);
            }
        }
        StringBuffer tt = new StringBuffer();
        for (int i = 0;i<commands.size();i++){
            tt.append(commands.get(i) + " ");
        }
        return tt.toString();
    }
    public static void getErrMsg(OutputStream outputStream,String message) throws IOException {
        outputStream.write('-');
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
    public static void getOKMsg(OutputStream outputStream) throws IOException {
        outputStream.write('+');
        outputStream.write("OK".getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
    public static void getIntMsg(OutputStream outputStream,long message) throws IOException {
        outputStream.write(':');
        outputStream.write(String.valueOf(message).getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
    public static void getArrayMsg(OutputStream outputStream,List<?> message) throws Exception {
        outputStream.write('*');
        outputStream.write(String.valueOf(message.size()).getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
        for (Object o:message){
            if(o instanceof Integer || o instanceof Long){
                getIntMsg(outputStream,(long)o);
            }else if(o instanceof String){
                getStringMsg(outputStream,(String) o);
            }else {
                throw new Exception("错误的类型");
            }
        }
    }
    public static void getStringMsg(OutputStream outputStream,String message) throws IOException {
        byte[] buf = message.getBytes();
        outputStream.write('$');
        outputStream.write(String.valueOf(buf.length).getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(buf);
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
}
