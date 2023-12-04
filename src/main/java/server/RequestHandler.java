package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class RequestHandler implements HttpHandler {

    private final Map<String, String[]> data;
    MongoCollection<Document> recipesCollection;
    MongoCollection<Document> usersCollection;
    MongoDatabase recipe_db;
    MongoDatabase users_db;
    String uri;
    MongoClient mongoClient;

    public RequestHandler(Map<String, String[]> data) {
        this.data = data;
        uri = "mongodb+srv://admin:123@cluster0.cp02bnz.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        recipe_db = mongoClient.getDatabase("recipe_db");
        users_db = mongoClient.getDatabase("users_db");
        recipesCollection = recipe_db.getCollection("recipes");
        usersCollection = users_db.getCollection("users");

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

        //name ! type $ user = details

        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        System.out.print(postData);
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine();
        }
        String name = postData.substring(0, postData.indexOf("!"));
        String type = postData.substring(postData.indexOf("!") + 1, postData.indexOf("$"));
        String user = postData.substring(postData.indexOf("$") + 1, postData.indexOf("="));
        String details = postData.substring(postData.indexOf("=") + 1);

        System.out.print("postdata: " + postData);

        if(name.equals("login")){
            Document rec = usersCollection.find(eq("name", name)).first();
            if(rec != null){
                scanner.close();
                return "Username already taken";
            }
            Document doc = new Document("username", type);
            doc.append("password", user);
            usersCollection.insertOne(doc);
            scanner.close();
            return type;
        }

        // Store data in hashmap
        // String[] typeAndDetails = new String[]{type, details};
        // data.put(name, typeAndDetails);
        String id = new ObjectId().toString();
        Document recipe = new Document("_id", id);
        //System.out.print("NAME: " + name + ", TYPE: " + type + ", USER: " + user + ", DETAILS: " + details);
        recipe.append("name", name)
                .append("type", type)
                .append("user", user)
                .append("details", details);

        recipesCollection.insertOne(recipe);

        System.out.println(id + data.toString());
        scanner.close();

        return id;
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "default";
       // InputStream inStream = httpExchange.getRequestBody();
       // Scanner scanner = new Scanner(inStream);
        //String postData = scanner.nextLine();
       // String name = postData;
       // System.out.println(result);
        
        URI uri = httpExchange.getRequestURI();

        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            System.out.print("value: " + value);
            if (value.substring(0, 5).equals("login")) {
                Document rec = usersCollection.find(eq("username", value.substring(5))).first();
                if (rec == null) {
                    return "Incorrect username or password";
                }
                response = rec.toJson();
                return response;
            }
            //value = value.replace("-", " ");
            // Bson filter = eq("name", value);
            Document rec = recipesCollection.find(eq("name", value)).first();
            // DeleteResult result = recipesCollection.deleteOne(filter);
            response = rec.toJson();
            // System.out.println(result);
            return response;
        }
        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "";
       // InputStream inStream = httpExchange.getRequestBody();
       // Scanner scanner = new Scanner(inStream);
        //String postData = scanner.nextLine();
       // String name = postData;
       // System.out.println(result);
        
        URI uri = httpExchange.getRequestURI();

        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            //value = value.replace("-", " ");
            Bson filter = eq("name", value);
            DeleteResult result = recipesCollection.deleteOne(filter);
            response = "deleted" + filter.toString();
            System.out.println(result);
        }
        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        System.out.print(postData);
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine();
        }
        String ID = postData.substring(0, postData.indexOf("!"));
        String type = postData.substring(postData.indexOf("!") + 1, postData.indexOf("$"));
        String user = postData.substring(postData.indexOf("$") + 1, postData.indexOf("="));
        String details = postData.substring(postData.indexOf("=") + 1);

        Bson filter = and(eq("_id", ID));
        Bson updateOperation = set("details", details);
        UpdateResult updateResult = recipesCollection.updateOne(filter, updateOperation);

        System.out.print(ID + user);

        scanner.close();
        return updateResult.toString();
    }

}
