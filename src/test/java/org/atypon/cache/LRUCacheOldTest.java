package org.atypon.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LRUCacheOldTest {
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
    public void test() {
        cache.put("1",nodes.get(0));
        cache.put("2",nodes.get(1));
        cache.put("3",nodes.get(0));
        cache.put("4",nodes.get(1));
        cache.get("1");
        cache.put("5", nodes.get(0));
        for(String s : cache.keySet())
        {
            System.out.println(s);
        }
        System.out.println(cache.get("3"));

    }


}