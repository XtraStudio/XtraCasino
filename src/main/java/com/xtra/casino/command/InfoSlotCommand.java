/**
 * This file is part of XtraCasino, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 - 2016 XtraStudio <https://github.com/XtraStudio>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

import com.flowpowered.math.vector.Vector3d;
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
        Vector3d vector = machine.getPosition();
        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, name))
                .padding(Text.of(TextColors.GOLD, "-="))
                .contents(Text.of(TextColors.GREEN, "Slot type: ", TextColors.BLUE, machine.getType()),
                        Text.of(TextColors.GREEN, "Slot location: ", TextColors.BLUE, vector.getX(), ", ", vector.getY(), ", ", vector.getZ()),
                        Text.of(TextColors.GREEN, "Slot state: ", TextColors.BLUE, machine.getState()))
                .sendTo(src);
        return CommandResult.success();
    }
}
