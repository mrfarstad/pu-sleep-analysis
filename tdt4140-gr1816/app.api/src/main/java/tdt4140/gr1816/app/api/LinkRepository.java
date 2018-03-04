package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.Link;

public class LinkRepository {

  private final MongoCollection<Document> links;

  public LinkRepository(MongoCollection<Document> links) {
    this.links = links;
  }

  public Link findById(String id) {
    Document doc = links.find(eq("_id", new ObjectId(id))).first();
    return link(doc);
  }

  public List<Link> getAllLinks() {
    List<Link> allLinks = new ArrayList<>();
    for (Document doc : links.find()) {
      allLinks.add(link(doc));
    }
    return allLinks;
  }

  public void saveLink(Link link) {
    Document doc = new Document();
    doc.append("url", link.getUrl());
    doc.append("description", link.getDescription());
    doc.append("postedBy", link.getUserId());
    links.insertOne(doc);
  }

  private Link link(Document doc) {
    return new Link(
        doc.get("_id").toString(),
        doc.getString("url"),
        doc.getString("description"),
        doc.getString("postedBy"));
  }
}
