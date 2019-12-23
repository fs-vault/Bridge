package xyz.nkomarn.Bridge.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

public class PingCommand implements Command {
    @Override
    public void execute(final CommandSource sender, final String[] strings) {
        if (!(sender instanceof Player)) return;
        final Player player = (Player) sender;

        sender.sendMessage(TextComponent.of(String.format(
                "\u00A76\u00A7l\u00BB \u00A77Your ping is %sms.", Math.max(0, player.getPing())
        )));
    }
}
