package org.atypon.services;

import org.atypon.data.collection.Database;
import org.junit.Test;



public class DocumentServiceTest {

    @Test
    public void testAddDocuemt() throws InterruptedException {
        CRUDService.CRUDServiceBuilder builder = new CRUDService.CRUDServiceBuilder();
        builder.currentDatabase("database1");
        builder.currentCollection("collection1");
        builder.currentDocument("document1.json");
        CRUDService crudService = builder.build();
        System.out.println(    crudService.find("name","khaled"));
    ;

    }

}

