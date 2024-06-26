package net.jeqo.blockmaster.commands.manager;

import net.jeqo.blockmaster.blocks.BlockRegistry;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import org.bukkit.Instrument;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTabCompleter implements TabCompleter {

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 0:
                return null;
            case 1:
                return Arrays.asList("add", "reload");
            default:
                if (args[0].equalsIgnoreCase("reload"))
                    return null;
                if (args[0].equalsIgnoreCase("add"))
                    return Arrays.asList(
                            BlockRegistry.getRegistry().stream().map(CustomBlock::getId).collect(Collectors.toList()),    // block id
                            Collections.singletonList("(example) 0"), // customModelData
                            Arrays.stream(Instrument.values()).map(Enum::toString).collect(Collectors.toList()),           // instruments
                            allPitchNumbers(),  // note
                            Arrays.asList("false", "true")).get(args.length - 2);
                break;
        }
        return null;
    }

    private List<String> allPitchNumbers() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 25; i++) values.add((i < 10 ? "0" : "") + i);
        return values;
    }
}
