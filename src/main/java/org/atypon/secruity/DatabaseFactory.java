package org.atypon.secruity;

import java.io.IOException;

public class DatabaseFactory {

    private DatabaseFactory() {}

    public static DatabaseFacade getDatabase(boolean isAdmin) throws IOException {
        if(isAdmin) {
            return new AdminDatabase();
        } else {
            return new UserDatabase();
        }
    }

    // this function is used to return a new AdminDatabase if the boolean is true otherwise it will return a new UserDatabase






}
