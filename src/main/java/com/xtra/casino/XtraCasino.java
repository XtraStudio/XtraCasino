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
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;
import com.xtra.casino.serializer.GsonHandler;
import com.xtra.core.Core;
import com.xtra.core.command.CommandHandler;
import com.xtra.core.text.HelpPaginationHandler;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION)
public class XtraCasino {

    private @Inject Logger logger;
    private static XtraCasino instance;
    private GsonHandler gsonHandler;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        logger.info("Initializing XtraCasino version ", PluginInfo.VERSION);
        instance = this;
        Core.initialize(this);
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        CommandHandler.create();
        HelpPaginationHandler.create();
        this.gsonHandler = new GsonHandler();
    }

    public static XtraCasino instance() {
        return instance;
    }

    public Logger logger() {
        return this.logger;
    }
    
    public GsonHandler getGsonHandler() {
        return this.gsonHandler; // TODO: remove when gson stuff is in XtraCore
    }
}
