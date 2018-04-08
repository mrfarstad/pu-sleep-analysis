package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import graphql.GraphQLException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
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

    User u = findByUsername(user.getUsername());
    if (u != null) {
      throw new GraphQLException("Duplicate username!!");
    }
    Document doc = new Document();
    doc.append("username", user.getUsername());
    doc.append("isGatheringData", user.isGatheringData());
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
        user.getAge(),
        user.isGatheringData());
  }

  public boolean deleteUser(User user) {
    Document doc = users.find(eq("_id", new ObjectId(user.getId()))).first();
    DeleteResult result = users.deleteOne(doc);
    return result.wasAcknowledged();
  }

  public boolean editUser(String username, String newUsername, int newAge, String newGender) {
    User u = findByUsername(newUsername);
    if (u != null) {
      return false;
    }

    User user = findByUsername(username);
    Document userDoc = users.find(eq("_id", new ObjectId(user.getId()))).first();

    Document doc = new Document();
    if (!newUsername.equals("null")) {
      doc.append("username", newUsername);
    }
    if (newAge > 0) {
      doc.append("age", newAge);
    }
    if (!newGender.equals("null")) {
      doc.append("gender", newGender);
    }
    users.updateOne(
        eq("_id", new ObjectId(userDoc.get("_id").toString())), new Document("$set", doc));
    return true;
  }

  public boolean setIsGatheringData(User user, Boolean status) {
    Document userDoc = users.find(eq("_id", new ObjectId(user.getId()))).first();

    Document doc = new Document();
    doc.append("isGatheringData", status);
    users.updateOne(
        eq("_id", new ObjectId(userDoc.get("_id").toString())), new Document("$set", doc));
    return status;
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
        doc.getInteger("age"),
        doc.getBoolean("isGatheringData", true));
  }
}
