package org.atypon.data.collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.atypon.data.indexing.BTreeIndexer;
import org.atypon.data.indexing.Indexer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class JsonCollection {

    private final String collectionName;
    private final ObjectMapper mapper;
    private final String path;
    private final File collectionFile;

    private String id;
    private ArrayNode nodesArray;

    private  final Indexer indexer;
    private int i=0;



    public JsonCollection(String collectionName,String path) {
        this.collectionName = collectionName;
        this.mapper = new ObjectMapper();
        this.path = path +"/"+ collectionName + ".json";
        collectionFile = new File(this.path);
        this.id = UUID.randomUUID().toString();
        nodesArray = mapper.createArrayNode();
        indexer = new BTreeIndexer();
    }

    public JsonCollection(String collectionName,String path,String id) {
        this.collectionName = collectionName;
        this.mapper = new ObjectMapper();
        this.path = path +"/"+ collectionName + ".json";
        collectionFile = new File(this.path);
        this.id = id;
        nodesArray = mapper.createArrayNode();
        indexer = new BTreeIndexer();
    }

    public void insert(String json) throws IOException {


        if(!collectionFile.exists()) {
            collectionFile.createNewFile();
        }
        JsonNode node = mapper.readTree(wrapId(json));
        nodesArray.add(node);
        this.commitToFile();



        this.updateIndex(node);
    }

    public void insertMany(ArrayList<String> jsons) throws IOException {
        for(String s : jsons) {
            insert(s);
        }
    }




    public void updateOne(String property, String propertyValue, String newValue) throws IOException {
        for(JsonNode nodes : nodesArray) {
            if(nodes.get(property).asText().equals(propertyValue)) {
                ObjectNode temp = (ObjectNode) nodes;
                temp.put(property,newValue);
                this.commitToFile();
                this.updateIndex(nodes);
                break;
            }
        }
    }

    public void updateMany(String property, String propertyValue, String newValue) throws IOException {
        for(JsonNode nodes : nodesArray) {
            if(nodes.get(property).asText().equals(propertyValue)) {
                ObjectNode temp = (ObjectNode) nodes;
                temp.put(property,newValue);
                this.updateIndex(nodes);
            }
        }
        this.commitToFile();

    }

    public void delete(String property, String propertyValue) throws IOException {
        int i=0;
        for(JsonNode nodes : nodesArray) {

            if(nodes.get(property).asText().equals(propertyValue)) {
                nodesArray.remove(i);
                this.removeFromIndexed(nodes);
                this.commitToFile();
                break;
            }
            i++;
        }
    }

    public void deleteMany(String property, String propertyValue) throws IOException {

        ArrayNode temp = mapper.createArrayNode();
        for(JsonNode nodes : nodesArray) {
            if(!nodes.get(property).asText().equals(propertyValue)) {
              temp.add(nodes);
            }
            else
                this.removeFromIndexed(nodes);
        }
        this.nodesArray = temp;
        this.commitToFile();
    }


    public ArrayList<JsonNode> find(String property,String propertyValue) {
        ArrayList<JsonNode> temp = new ArrayList<>();
        if(indexer.has(property)) {
            temp = indexer.get(property,propertyValue);
        }else {
            temp = this.findForNonIndex(property,propertyValue);
            return temp;


        }
        return temp;
    }

    public ArrayList<JsonNode> findForNonIndex(String property,String propertyValue) {
        ArrayList<JsonNode>temp = new ArrayList<JsonNode>();
        for(JsonNode nodes : nodesArray) {
            if(nodes.get(property).equals(propertyValue)) {

                temp.add(nodes);
            }
        }
        if(temp.size() > 0) {
            return temp;
        }
        return null;


    }

    private String wrapId(String json) {
        return "{\"_id\":\"" + id + "\"," + json.substring(1);
    }


    private void commitToFile() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(collectionFile,nodesArray);
    }

    private ArrayList<JsonNode> getNodes() {
        ArrayList<JsonNode> temp = new ArrayList<>();
        for(JsonNode node : nodesArray) {
            temp.add(node);
        }
        return temp;
    }

    private void updateIndex(JsonNode newValue) throws IOException {
        if(i ==0){
            i++;
            indexer.makeIndexOn("_id",this.getNodes());
        }
        else {
            for (String property : indexer.getAllPropertyIndexed()) {
                indexer.addToIndexed(property, newValue);
            }
        }
    }

    private void removeFromIndexed(JsonNode value) {
        for(String property : indexer.getAllPropertyIndexed()) {
            if(value.has(property)) {
                indexer.deleteFromIndexed(property,value);
            }
        }
    }







}
