package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import tdt4140.gr1816.app.api.types.User;

public class UserRepository {

    private final MongoCollection<Document> users;

    public UserRepository(MongoCollection<Document> users) {
        this.users = users;
    }
    
    public User findByUsername(String username) {
        Document doc = users.find(eq("username", username)).first();
        return user(doc);
    }
    
    public User findById(String id) {
        Document doc = users.find(eq("_id", new ObjectId(id))).first();
        return user(doc);
    }
    
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        for (Document doc : users.find()) {
            allUsers.add(user(doc));
        }
        return allUsers;
    }
    
    public User saveUser(User user) {
        Document doc = new Document();
        doc.append("id", user.getId().toString());
        doc.append("username", user.getUsername());
        doc.append("password", user.getPassword());
        doc.append("isDoctor", user.isDoctor());
        doc.append("gender", user.getGender());
        doc.append("age", user.getAge());
        users.insertOne(doc);
        return new User(
        			doc.get("_id").toString(),
        			user.getUsername(),
        			user.getPassword(),
        			user.isDoctor(),
        			user.getGender(),
        			user.getAge()
        		);
    }
    
    private User user(Document doc) {
    	if (doc == null) {
    		return null;
    	}
	    return new User(
	            doc.get("_id").toString(),
	            doc.getString("username"),
	            doc.getString("password"),
	    			doc.getBoolean("isDoctor"),
	    			doc.getString("gender"),
	    			doc.getInteger("age"));
	    }
}
