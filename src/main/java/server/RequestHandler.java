package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class RequestHandler implements HttpHandler {

    private final Map<String, String[]> data;

    public RequestHandler(Map<String, String[]> data) {
        this.data = data;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String name = postData.substring(0,postData.indexOf("~"));
        String type = postData.substring(postData.indexOf("~"), postData.indexOf("#"));
        String details = postData.substring(postData.indexOf("#") + 1);

        // Store data in hashmap
        String[] typeAndDetails = new String[]{type, details};
        data.put(name, typeAndDetails);

        String uri = "mongodb+srv://admin:123@cluster0.cp02bnz.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            //recipe_db = mongoClient.getDatabase("recipe_db");
            //recipesCollection = recipe_db.getCollection("recipes"); 
        
        }

        String response = "Posted entry {" + name + ", " + type + "}";
        System.out.println(response + data.toString());
        scanner.close();

        return response;
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        /* String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String year = data.get(value); // Retrieve data from hashmap
            if (year != null) {
                response = year;
                System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "No data found for " + value;
            }
        }*/
        
        return "HI";
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        /* URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String year = data.get(value); // Retrieve data from hashmap
            if (year != null) {
                data.remove(value);
                response = "Deleted Entry {" + value + "," + year + "}";
                System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "No data found for " + value;
            }
        } */
        String response = "Invalid GET request";
        
        
        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        /*
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String language = postData.substring(
                0,
                postData.indexOf(",")), year = postData.substring(postData.indexOf(",") + 1);

        // Store data in hashmap
        String response;
        if (data.containsKey(language)) {
            String previous = data.get(language);
            response = "Updated entry {" + language + ", " + year + "} (previous year: " + previous + ")";
        } else {
            response = "Added entry {" + language + ", " + year + "}";
        }

        data.put(language, year);
        System.out.println(response);
        scanner.close();
         */

        return "HI";
    }

}
