package coreCode.test;

import coreCode.persist.AOFPersist;
import coreCode.persist.Persist;
import coreCode.persist.RDBPersist;
import coreCode.persist.RDBPersistNeo;
import coreCode.threads.PersistThread;
import coreCode.utils.EntityProto;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        RDBPersistNeo rdbPersistNeo = new RDBPersistNeo();
        //rdbPersistNeo.store();
        rdbPersistNeo.load();

    }

}
