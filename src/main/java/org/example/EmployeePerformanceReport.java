package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.*;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
record Employee(int id,String name){}

@JsonIgnoreProperties(ignoreUnknown = true)
record EmployeeRatings(int employeeId, double rating, int quarter){}

public class EmployeePerformanceReport {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");) {
            MongoDataFetcher dataFetcher = new MongoDataFetcher(mongoClient);
            List<Employee> employees = dataFetcher.getData("employee", Employee.class);
            List<EmployeeRatings> employeeRatings = dataFetcher.getData("employee-ratings", EmployeeRatings.class);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}