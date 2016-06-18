package com.xtra.casino.command;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.xtra.casino.XtraCasino;
import com.xtra.casino.api.slot.SlotMachine;
import com.xtra.core.command.annotation.RegisterCommand;
import com.xtra.core.command.base.CommandBase;

import ninja.leaping.configurate.ConfigurationNode;

@RegisterCommand(childOf = CasinoCommand.class)
public class InfoSlotCommand extends CommandBase<CommandSource> {

    @Override
    public String[] aliases() {
        return new String[] {"info"};
    }

    @Override
    public CommandElement[] args() {
        return new CommandElement[] {GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))};
    }

    @Override
    public String description() {
        return "Gets information about a slot machine.";
    }

    @Override
    public String permission() {
        return "xtracasino.info";
    }

    @Override
    public CommandResult executeCommand(CommandSource src, CommandContext args) throws Exception {
        String name = args.<String>getOne("name").get();
        Optional<Map<ConfigurationNode, SlotMachine>> optional = XtraCasino.instance().getGsonHandler().getSlot(name);
        if (!optional.isPresent()) {
            src.sendMessage(Text.of(TextColors.RED, "Could not find a slot with the name ", TextColors.BLUE, name, TextColors.RED, "!"));
            return CommandResult.empty();
        }
        SlotMachine machine = optional.get().values().iterator().next();
        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, name))
                .padding(Text.of(TextColors.GOLD, "-="))
                .contents(Text.of(TextColors.GREEN, "Slot type: ", TextColors.BLUE, machine.getType()))
                .sendTo(src);
        return CommandResult.success();
    }
}
