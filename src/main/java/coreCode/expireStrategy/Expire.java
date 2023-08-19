package coreCode.expireStrategy;

import coreCode.Database;

import java.io.IOException;

public interface Expire{
    void refreshDatabase(Database database);
    void run();
}
