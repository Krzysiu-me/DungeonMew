package dungeonmew.feature;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static dungeonmew.DungeonMewClient.systemText;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public final class DisplayArmorBarCommand {
    // usage: displayArmorBar (get|set (true|false))
    public static void register(String name, LiteralCommandNode<FabricClientCommandSource> rootNode) {
        var displayArmorBarNode = literal(name).build();

        var getNode = literal("get")
                .executes(DisplayArmorBarCommand::getState)
                .build();

        var setNode = literal("set").build();
        var valueNode = argument("value", BoolArgumentType.bool())
                .executes(context -> setState(context, BoolArgumentType.getBool(context, "value")))
                .build();

        setNode.addChild(valueNode);

        displayArmorBarNode.addChild(getNode);
        displayArmorBarNode.addChild(setNode);

        rootNode.addChild(displayArmorBarNode);
    }

    private static int getState(CommandContext<FabricClientCommandSource> ctx) {
        sendStateMessage(ctx.getSource(), Features.DISPLAY_ARMOR_BAR.getValue());

        return Command.SINGLE_SUCCESS;
    }

    private static int setState(CommandContext<FabricClientCommandSource> ctx, boolean value) {
        Features.DISPLAY_ARMOR_BAR.bind(value);
        sendStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

    private static void sendStateMessage(FabricClientCommandSource source, boolean value) {
        if (value)
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.display_armor_bar.enabled")));
        else
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.display_armor_bar.disabled")));
    }
}
