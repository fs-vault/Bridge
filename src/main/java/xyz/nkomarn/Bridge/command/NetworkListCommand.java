package xyz.nkomarn.Bridge.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.Bridge.Bridge;
import xyz.nkomarn.Bridge.api.BridgeAPI;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkListCommand implements Command {
    @Override
    public void execute(final CommandSource sender, final String[] strings) {
        Map<String, Integer> proxies = new HashMap<>();
        try (final Jedis jedis = RedisDatabase.getResource()) {
            jedis.keys("proxy:*:online").forEach(proxy -> {
                final Pattern pattern = Pattern.compile("proxy:(.*?):online");
                final Matcher matcher = pattern.matcher(proxy);
                if (matcher.find()) proxies.put(matcher.group(1), Math.toIntExact(jedis.scard(proxy)));
            });
        } finally {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("\u00A76\u00A7l\u00BB \u00A77There are %s total players online:", BridgeAPI.onlinePlayers));
            for (String proxy : proxies.keySet()) {
                builder.append(String.format("\n%s players are on %s.", proxies.get(proxy), proxy));
            }
            sender.sendMessage(TextComponent.of(builder.toString()).color(TextColor.GRAY));
        }
    }
}
