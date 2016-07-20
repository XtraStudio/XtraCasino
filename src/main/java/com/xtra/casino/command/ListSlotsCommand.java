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

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.TextMessageException;

import com.xtra.api.command.annotation.RegisterCommand;
import com.xtra.api.command.base.CommandBase;
import com.xtra.casino.XtraCasino;
import com.xtra.casino.api.slot.SlotMachine;

@RegisterCommand(childOf = CasinoCommand.class)
public class ListSlotsCommand extends CommandBase<CommandSource> {

    @Override
    public String[] aliases() {
        return new String[] {"list"};
    }

    @Override
    public String permission() {
        return "xtracasino.list";
    }

    @Override
    public String description() {
        return "Lists all of the registered slot machines.";
    }

    @Override
    public CommandElement[] args() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public CommandResult executeCommand(CommandSource src, CommandContext args) throws Exception {
        List<SlotMachine> machines = XtraCasino.instance().getGsonHandler().getAllSlots();
        if (machines.isEmpty()) {
            throw new TextMessageException(Text.of(TextColors.RED, "No slot machines found."));
        }
        List<Text> slotText = new ArrayList<>();
        for (SlotMachine machine : machines) {
            slotText.add(Text.of(TextActions.runCommand("/casino info " + machine.getName()), TextColors.BLUE, TextStyles.BOLD, machine.getName(),
                    " - ", TextStyles.RESET, TextColors.GREEN, machine.getType()));
        }

        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, "Slots"))
                .padding(Text.of(TextColors.GOLD, "-="))
                .contents(slotText)
                .sendTo(src);
        return CommandResult.success();
    }
}
