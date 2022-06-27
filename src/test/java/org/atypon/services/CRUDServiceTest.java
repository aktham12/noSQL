package org.atypon.services;

import org.atypon.data.collection.JsonDocument;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CRUDServiceTest {
    CRUDService service;
    @Before
    public void setUp() {
        service = new CRUDService.CRUDServiceBuilder().
                currentDatabase("logins").currentCollection("users").currentDocument("logins.json").build();
    }

    @Test
    public void testInsert() {
        String json = "{\"_id\": \"2\",\"username\":\"aktham\",\"password\":\"admin\"}";
        service.insert(
                json
        );
        assertEquals(
                "ali", service.find("username","aktham").get(0).get("username").asText()
        );
    }
    @Test
    public void testInsertMany() {
        String json = "{\"_id\": \"2\",\"username\":\"ali\",\"password\":\"admin\"}";
        String json2 = "{\"_id\": \"3\",\"username\":\"ali2\",\"password\":\"admin\"}";
        service.insertMany(
                new ArrayList<>(Arrays.asList(json, json2))
        );
        assertEquals(
                "ali", service.find("username","ali").get(0).get("username").asText()
        );
        assertEquals(
                "ali2", service.find("_id","3").get(0).get("username").asText()
        );
    }

    @Test
    public void testUpdateOne() {
        String json = "{\"_id\": \"4\",\"username\":\"khaled\",\"password\":\"admin\"}";

        service.updateOne(
                "username",
                "ali",
                json

        );
        assertEquals(
                "khaled", service.find("username","khaled").get(0).get("username").asText()
        );
    }
    @Test
    public void testUpdateMany() {
        String json1 = "{\"_id\": \"4\",\"username\":\"khaled\",\"password\":\"admin\"}";
        String json = "{\"_id\": \"2\",\"username\":\"ali\",\"password\":\"admin\"}";
        String json2 = "{\"_id\": \"3\",\"username\":\"ali\",\"password\":\"admin\"}";
        service.insertMany(
                new ArrayList<>(Arrays.asList(json, json2))
        );
        service.updateMany(
                "username",
                "ali",
                json1
        );
        assertEquals(
                new ArrayList<>(),service.find("username","ali")
        );
    }


    @Test
    public void testDeleteMany() {
        service.deleteMany(
                "password",
                "admin"
        );
        }








}