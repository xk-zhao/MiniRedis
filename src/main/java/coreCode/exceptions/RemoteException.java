package coreCode.exceptions;

public class RemoteException extends Exception{
    public RemoteException(){

    }
    public RemoteException(String msg){
        super(msg);
    }

    public RemoteException(String msg, Throwable cause){
        super(msg,cause);
    }


    public RemoteException(Throwable cause) {
        super(cause);
    }

    public RemoteException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
