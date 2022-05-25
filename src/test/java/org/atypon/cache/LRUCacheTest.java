package org.atypon.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LRUCacheTest {
    Cache<String, JsonNode> cache;
    List<JsonNode> nodes;
    ObjectMapper mapper;


    @Before
    public void setUp() throws JsonProcessingException {
        cache = new LRUCache<>(4);
        mapper = new ObjectMapper();

        nodes = new ArrayList<>();

        String json = "{\"_id\": \"2\",\"name\":\"aktham\",\"age\":\"25\"}";

        String json2 = "{\"_id\": \"1\",\"name\":\"khaled\",\"age\":\"21\"}";

        nodes.add(mapper.readTree(json));
        nodes.add(mapper.readTree(json2));


    }

    @Test
    public void testPuT() {


    }



}