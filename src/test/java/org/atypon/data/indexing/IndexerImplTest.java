package org.atypon.data.indexing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexerImplTest {
    Indexer indexer;
    ArrayNode nodes;
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        indexer = new BTreeIndexer();
        mapper = new ObjectMapper();

        nodes = mapper.createArrayNode();

        String json = "{\"_id\": \"2\",\"name\":\"aktham\",\"age\":\"25\"}";

        String json2 = "{\"_id\": \"1\",\"name\":\"khaled\",\"age\":\"21\"}";

        nodes.add(mapper.readTree(json));
        nodes.add(mapper.readTree(json2));


        indexer.makeIndexOn("age", nodes);


    }

    @Test
    public void makeIndexOn() {
        indexer.makeIndexOn("name", nodes);
        assertEquals("khaled", indexer.get("name", "khaled").get(0).get("name").asText());
        assertEquals("aktham", indexer.get("name", "aktham").get(0).get("name").asText());

    }


    @Test
    public void MakeIndexOnTest1() {
        indexer.makeIndexOn("name", nodes);

        assertEquals("aktham", indexer.get("name", "aktham").get(0).get("name").asText());
        assertEquals("khaled", indexer.get("name", "khaled").get(0).get("name").asText());
    }

    @Test
    public void MakeIndexOnTest2() {
        assertEquals("25", indexer.get("age", "25").get(0).get("age").asText());
        assertEquals("21", indexer.get("age", "21").get(0).get("age").asText());
    }


    @Test
    public void get() {
        indexer.makeIndexOn("name", nodes);

        assertEquals("aktham", indexer.get("name", "aktham").get(0).get("name").asText());
        assertEquals("khaled", indexer.get("name", "khaled").get(0).get("name").asText());
        assertEquals("25", indexer.get("age", "25").get(0).get("age").asText());
        assertEquals("21", indexer.get("age", "21").get(0).get("age").asText());
        assertNotEquals("aktham", indexer.get("name", "khaled").get(0).get("name").asText());
        assertNotEquals("25", indexer.get("age", "21").get(0).get("age").asText());
    }

    @Test
    public void get2() {
        assertNull(indexer.get("_id", "aktham"));
        assertNull(indexer.get("_id", "1"));
        indexer.makeIndexOn("_id", nodes);

        assertNotNull(indexer.get("_id", "2"));
    }

    @Test
    public void addToIndexed() throws JsonProcessingException {
        indexer.makeIndexOn("name", nodes);

        indexer.addToIndexed("name", mapper.readTree("{\"_id\": \"5\",\"name\":\"ahmad\",\"age\":\"25\"}"));
        assertEquals("ahmad", indexer.get("name", "ahmad").get(0).get("name").asText());


    }

    @Test
    public void addToAllIndexed() throws JsonProcessingException {
        indexer.makeIndexOn("name", nodes);

        JsonNode node;
        String json = "{\"_id\": \"2\",\"name\":\"ali\",\"age\":\"25\"}";
        node = mapper.readTree(json);
        indexer.addToAllIndexed(node);
        assertEquals("ali", indexer.get("name", "ali").get(0).get("name").asText());
        assertEquals("25", indexer.get("age", "25").get(0).get("age").asText());
    }

    @Test
    public void getKeys() throws JsonProcessingException {
        String[] strings = indexer.getAllPropertyIndexed();


    }
}