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

import java.net.URL;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.xtra.casino.PluginInfo;
import com.xtra.core.command.annotation.RegisterCommand;
import com.xtra.core.command.base.CommandBase;

@RegisterCommand(childOf = CasinoCommand.class)
public class VersionCommand extends CommandBase<CommandSource> {

    @Override
    public String[] aliases() {
        return new String[] {"version", "v"};
    }

    @Override
    public CommandElement[] args() {
        return null;
    }

    @Override
    public String description() {
        return "Gets the version as well as other various info.";
    }

    @Override
    public String permission() {
        return "xtracasino.version";
    }

    @Override
    public CommandResult executeCommand(CommandSource src, CommandContext args) throws Exception {
        String url = "https://github.com/XtraStudio/XtraCasino";
        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, "XtraCasino"))
                .padding(Text.of(TextColors.GOLD, "-="))
                .contents(Text.of(TextColors.GREEN, "Version: ", TextColors.BLUE, PluginInfo.VERSION),
                        Text.of(TextColors.GREEN, "Author: ", TextColors.BLUE, "12AwesomeMan34"),
                        Text.of(TextColors.GREEN, "GitHub: ", TextActions.openUrl(new URL(url)), TextColors.BLUE, url))
                .sendTo(src);
        return CommandResult.success();
    }
}
