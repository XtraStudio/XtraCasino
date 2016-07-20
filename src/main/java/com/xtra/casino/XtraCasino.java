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

package com.xtra.casino;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.inject.Inject;
import com.xtra.api.Core;
import com.xtra.api.command.annotation.RunAt;
import com.xtra.api.command.runnable.CommandPhase;
import com.xtra.api.command.runnable.CommandRunnable;
import com.xtra.api.command.runnable.CommandRunnableResult;
import com.xtra.casino.serializer.GsonHandler;
import com.xtra.casino.util.SlotBlockHandler;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION)
public class XtraCasino {

    private @Inject Logger logger;
    private static XtraCasino instance;
    private GsonHandler gsonHandler;
    private SlotBlockHandler blockHandler;
    private EconomyService economy;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.logger.info("Initializing XtraCasino version ", PluginInfo.VERSION);
        instance = this;
        Core.initialize(this);
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Core.createCommandHandler(this.getClass());
        Core.createHelpPaginationBuilder(this.getClass()).build();

        this.gsonHandler = new GsonHandler();
        this.blockHandler = new SlotBlockHandler();

        Core.createListenerHandler(this.getClass());
    }

    public void onPostInit(GameStartedServerEvent event) {
        if (this.economy == null) {
            this.logger.error("Economy plugin not detected! XtraCasino will not function properly!");
            Core.getCommandHandler(this.getClass()).get().getCommandRunnableHandler().addForAllCommands(new CommandRunnable() {
                @Override
                @RunAt(phase = CommandPhase.PRE)
                public CommandRunnableResult run(CommandSource source, CommandContext args) {
                    source.sendMessage(Text.of(TextColors.RED, "An economy plugin has not been installed. XtraCasino cannot function properly."));
                    return CommandRunnableResult.stop(CommandResult.empty());
                }
            });
        }
    }

    @Listener
    public void onEconomyChange(ChangeServiceProviderEvent event) {
        if (event.getService().equals(EconomyService.class)) {
            this.economy = (EconomyService) event.getNewProvider();
        }
    }

    public static XtraCasino instance() {
        return instance;
    }

    public Logger logger() {
        return this.logger;
    }
    
    public GsonHandler getGsonHandler() {
        return this.gsonHandler;
    }

    public SlotBlockHandler getBlockHandler() {
        return this.blockHandler;
    }

    public EconomyService getEconomy() {
        return this.economy;
    }
}
