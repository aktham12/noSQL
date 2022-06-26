package org.atypon.services;

import org.junit.Test;

import static org.junit.Assert.*;

public class CRUDServiceTest {

    @Test
    public void testSex() {
        CRUDService service = new CRUDService.CRUDServiceBuilder().
                currentDatabase("database1").currentCollection("collection1").currentDocument("document1.json").build();
        String json = "{\"_id\": \"2\",\"name\":\"aktham\",\"age\":\"25\"}";
        service.insert(json);

    }

}