package xyz.nkomarn.Bridge.listener;

import redis.clients.jedis.JedisPubSub;
import xyz.nkomarn.Bridge.Bridge;
import xyz.nkomarn.Bridge.event.PubSubMessageEvent;

public class PubSubMessageHandler extends JedisPubSub {
    @Override
    public void onMessage(final String channel, final String message) {
        if (message.trim().length() <= 0) return;
        Bridge.getProxy().getEventManager().fireAndForget(
                new PubSubMessageEvent(channel, message));
    }
}
