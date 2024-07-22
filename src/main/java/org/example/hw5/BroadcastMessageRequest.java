package org.example.hw5;

/**
 * {
 *   "type": "broadcastMessage",
 *   "sender": "user123",
 *   "message": "text to all users"
 * }
 */
public class BroadcastMessageRequest extends AbstractRequest {

    public static final String TYPE = "broadcastMessage";

    private String sender;
    private String message;

    public BroadcastMessageRequest() {
        setType(TYPE);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

