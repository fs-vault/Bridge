package xyz.nkomarn.Bridge.api;

import xyz.nkomarn.Bridge.handler.JedisPubSubHandler;
import xyz.nkomarn.Kerosene.velocity.Kerosene;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class BridgeAPI {
    private static JedisPubSubHandler pubSubHandler;
    private static Set<String> subscribedChannels = new HashSet<>();
    public static final String PROXY_KEY = String.format("proxy:%s:online", Kerosene.getServerId());
    public static AtomicInteger onlinePlayers = new AtomicInteger(0);

    public BridgeAPI(final JedisPubSubHandler handler) {
        pubSubHandler = handler;
    }

    /**
     * Return current subscribed channels
     */
    public static Set<String> getSubscribedChannels() {
        return subscribedChannels;
    }

    /**
     * Subscribe to channel(s)
     * @param channels Channels to subscribe to
     */
    public static void subscribeChannels(final String... channels) {
        subscribedChannels.addAll(Arrays.asList(channels));
        pubSubHandler.subscribe(channels);
    }

    /**
     * Unsubscribe from channel(s)
     * @param channels Channels to unsubscribe from
     */
    public static void unSubscribeChannels(final String... channels) {
        subscribedChannels.removeAll(Arrays.asList(channels));
        pubSubHandler.unsubscribe(channels);
    }

    /**
     * Unsubscribe from all channels
     */
    public static void destroy() {
        subscribedChannels.clear();
        pubSubHandler.unsubscribe();
    }
}
