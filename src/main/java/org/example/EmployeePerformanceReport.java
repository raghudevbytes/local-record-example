package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.*;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
record Employee(int id,String name){}

@JsonIgnoreProperties(ignoreUnknown = true)
record EmployeeRatings(int employeeId, double rating, int quarter){}

//Finding top3 employees by average rating
public class EmployeePerformanceReport {

    public static void main(String[] args) {

        record EmployeeAverageRating(Employee employee, double rating){}

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");) {
            MongoDataFetcher dataFetcher = new MongoDataFetcher(mongoClient);
            List<Employee> employees = dataFetcher.getData("employee", Employee.class);
            List<EmployeeRatings> employeeRatings = dataFetcher.getData("employee-ratings", EmployeeRatings.class);

            //grouping the rating by employeeId
          Map<Integer, List<Double>> employeeRatingMap =   employeeRatings.stream().collect(Collectors.groupingBy(
                    EmployeeRatings::employeeId,Collectors.mapping(EmployeeRatings::rating, Collectors.toList())
            ));
        //finding out average rating and return result in the form of local record
         List<EmployeeAverageRating> employeeAverageRatingList =    employees.stream().map(employee -> {
               double rating =  employeeRatingMap.
                       getOrDefault(employee.id(), List.of()).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
               return new EmployeeAverageRating(employee, rating);
            }).toList();
         //filtering out top 3 employees
            employeeAverageRatingList.stream().sorted(Comparator.comparing(EmployeeAverageRating::rating).reversed())
                    .limit(3)
                    .forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}