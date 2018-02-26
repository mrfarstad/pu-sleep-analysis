package tdt4140.gr1816.app.api.types;

public class Message {

	private int messageID;
	private 	Person sender;
	private Person receiver;
	private String content;
	private String message;
	
	public Message(int id, Person sender, Person receiver, String content) {
		this.messageID = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}

	public int getMessageID() {
		return messageID;
	}

	public Person getSender() {
		return sender;
	}

	public Person getReceiver() {
		return receiver;
	}

	public String getContent() {
		return content;
	}

	public String getMessage() {
		return message;
	}
	
	
	
	
	
}

