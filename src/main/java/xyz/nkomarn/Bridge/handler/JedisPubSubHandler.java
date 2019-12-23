package xyz.nkomarn.Bridge.handler;

import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPubSub;
import xyz.nkomarn.Bridge.event.PubSubMessageEvent;

public class JedisPubSubHandler extends JedisPubSub {
    private final ProxyServer proxy;
    private final Logger logger;

    public JedisPubSubHandler(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Override
    public void onMessage(final String channel, final String message) {
        if (message.trim().length() <= 0) return;
        proxy.getEventManager().fireAndForget(
                new PubSubMessageEvent(channel, message)
        );
    }
}
