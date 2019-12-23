package xyz.nkomarn.Bridge.event;

/**
 * Represents a Pub/Sub message that has been received
 */
public class PubSubMessageEvent {
    private final String channel;
    private final String message;

    public PubSubMessageEvent(final String channel, final String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }
}
