package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDataFetcher {
    private MongoClient mongoClient;
    private ObjectMapper objectMapper;

    public MongoDataFetcher(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.objectMapper = new ObjectMapper();
    }

    public  <T> List<T> getData(String collectionName, Class<T> clazz) {
        try{
            var mongoDatabase = mongoClient.getDatabase("records-learning");
            var collection = mongoDatabase.getCollection(collectionName);
            var documentFindIterable = collection.find();
            List<T> list = new ArrayList<>();
            for(Document document: documentFindIterable){
                var json = document.toJson();
                T employee = objectMapper.readValue(json, clazz);
                list.add(employee);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
