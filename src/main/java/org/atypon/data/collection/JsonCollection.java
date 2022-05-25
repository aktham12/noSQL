package org.atypon.data.collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.atypon.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Getter
public class JsonCollection {
    private String name;

    private final String path;
    private final String id;

    private final File file;
    private int counter;

    private final ObjectMapper mapper;

    private final FileOutputStream fileOutputStream;


    public JsonCollection(String name, String path) throws IOException {
        this.name = name;
        this.path = path + '/' + this.name + ".json";

        mapper = new ObjectMapper();
        this.fileOutputStream = new FileOutputStream(this.path, false);
        file = new File(this.path);

        id = UUID.randomUUID().toString();


    }

    public JsonCollection(String name, String path, String id) throws FileNotFoundException {
        this.name = name;
        this.id = id;
        this.name = name;
        this.path = path + '/' + name + ".json";
        file = new File(this.path);
        mapper = new ObjectMapper();
        this.fileOutputStream = new FileOutputStream(file, false);


    }

    public void insert(String json) {

        String newJson = idGenerator(json);

        try {
            JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(fileOutputStream);
            jsonGenerator.writeRaw(newJson);
            jsonGenerator.close();
        } catch (IOException e) {

        }

    }

    public void deleteCollection() {

        FileManager.deleteFile(path);
    }

    public JsonNode readCollection() throws IOException {
        return mapper.readTree(file);
    }


    private String idGenerator(String json) {
        return "{\"_id\":\"" + id + "\"," + json.substring(1);
    }
}
