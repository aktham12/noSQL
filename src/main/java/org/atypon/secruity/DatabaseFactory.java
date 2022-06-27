package org.atypon.secruity;

import java.io.IOException;

public class DatabaseFactory {

    private DatabaseFactory() {}

    public static DatabaseOperation getDatabase(boolean isAdmin){
        if(isAdmin) {
            return new AdminDatabase();
        } else {
            return new UserDatabase();
        }
    }







}
