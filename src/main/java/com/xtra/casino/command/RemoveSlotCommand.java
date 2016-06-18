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
