package com.xtra.casino.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.xtra.casino.XtraCasino;
import com.xtra.core.command.annotation.RegisterCommand;
import com.xtra.core.command.base.CommandBase;

@RegisterCommand(childOf = CasinoCommand.class)
public class RemoveSlotCommand extends CommandBase<CommandSource> {

    @Override
    public String[] aliases() {
        return new String[] {"remove", "delete"};
    }

    @Override
    public CommandElement[] args() {
        return new CommandElement[] {GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))};
    }

    @Override
    public String description() {
        return "Removes a slot machine.";
    }

    @Override
    public String permission() {
        return "xtracasino.remove";
    }

    @Override
    public CommandResult executeCommand(CommandSource src, CommandContext args) throws Exception {
        String name = args.<String>getOne("name").get();
        if (!XtraCasino.instance().getGsonHandler().removeSlot(name)) {
            src.sendMessage(
                    Text.of(TextColors.RED, "Could not remove slot ", TextColors.BLUE, name, TextColors.RED, "! Did you spell it correctly?"));
            return CommandResult.empty();
        }
        src.sendMessage(
                Text.of(TextColors.GREEN, "Success! ", TextColors.GOLD, "Removed slot machine ", TextColors.BLUE, name, TextColors.GOLD, "!"));
        return CommandResult.success();
    }
}
