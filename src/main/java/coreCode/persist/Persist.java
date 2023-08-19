package coreCode.persist;

import coreCode.Database;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Persist {
    void store() throws IOException;
    void load() throws IOException;
}
