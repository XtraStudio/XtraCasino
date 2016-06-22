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
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TextMessageException;

import com.xtra.casino.XtraCasino;
import com.xtra.casino.api.slot.SlotMachine;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult.Type;
import com.xtra.core.command.annotation.RegisterCommand;
import com.xtra.core.command.base.CommandBase;
import com.xtra.core.world.direction.DirectionHandler;

import ninja.leaping.configurate.ConfigurationNode;

@RegisterCommand(childOf = CasinoCommand.class)
public class MoveSlotCommand extends CommandBase<Player> {

    @Override
    public String[] aliases() {
        return new String[] {"move", "migrate"};
    }

    @Override
    public CommandElement[] args() {
        return new CommandElement[] {GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))};
    }

    @Override
    public String description() {
        return "Moves a specified slot machine to your current standing position.";
    }

    @Override
    public String permission() {
        return "xtracasino.move";
    }

    @Override
    public CommandResult executeCommand(Player src, CommandContext args) throws Exception {
        String name = args.<String>getOne("name").get();
        Optional<Map<ConfigurationNode, SlotMachine>> optional = XtraCasino.instance().getGsonHandler().getSlot(name);
        if (!optional.isPresent()) {
            throw new TextMessageException(Text.of(TextColors.RED, "Could not find a slot machine with the name ", TextColors.BLUE, name,
                    TextColors.RED, "! Did you spell it correctly?"));
        }
        Map<ConfigurationNode, SlotMachine> map = optional.get();
        SlotMachine machine = map.values().iterator().next();
        XtraCasino.instance().getBlockHandler().removeBlocks(machine);
        machine.setPosition(src.getLocation().getPosition().floor());
        BlockSlotTransactionResult transaction =
                XtraCasino.instance().getBlockHandler().generateBase(machine, DirectionHandler.getCardinalDirectionFromYaw(src.getRotation().getY()));
        if (transaction.getType().equals(Type.FAILURE_WORLD_NOT_FOUND)) {
            throw new TextMessageException(Text.of(TextColors.RED, "Could not find the world!"));
        }
        machine.setSlotBlocks(transaction.getBlocks());
        machine.setSlots(transaction.getSlots());
        XtraCasino.instance().getGsonHandler().overwrite(map.keySet().iterator().next(), machine);
        XtraCasino.instance().getGsonHandler().save();
        src.sendMessage(
                Text.of(TextColors.GREEN, "Success! ", TextColors.GOLD, "Moved slot ", TextColors.BLUE, name, TextColors.GOLD,
                        " to a new location!"));
        return CommandResult.success();
    }
}
