package coreCode.persist;

import java.io.IOException;

public class AOFPersist implements Persist{
    @Override
    public void store() throws IOException {
        System.out.println("hello this is aof store");
    }
    @Override
    public void load() throws IOException {
        System.out.println("hello this is aof load");
    }
}
