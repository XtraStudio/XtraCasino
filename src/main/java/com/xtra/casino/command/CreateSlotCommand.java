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
import com.xtra.casino.api.slot.SlotState;
import com.xtra.casino.api.slot.SlotType;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult.Type;
import com.xtra.core.command.annotation.RegisterCommand;
import com.xtra.core.command.base.CommandBase;
import com.xtra.core.world.direction.DirectionHandler;

@RegisterCommand(childOf = CasinoCommand.class)
public class CreateSlotCommand extends CommandBase<Player> {

    @Override
    public String[] aliases() {
        return new String[] {"create", "c"};
    }

    @Override
    public CommandElement[] args() {
        return new CommandElement[] {GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                GenericArguments.onlyOne(GenericArguments.string(Text.of("type"))),
                GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("cost")))};
    }

    @Override
    public String description() {
        return "Creates a slot machine!";
    }

    @Override
    public String permission() {
        return "xtracasino.create";
    }

    @Override
    public CommandResult executeCommand(Player src, CommandContext args) throws Exception {
        String name = args.<String>getOne("name").get();
        String type = args.<String>getOne("type").get();
        double cost = args.<Double>getOne("cost").get();
        Optional<SlotType> slotType = SlotType.getType(type);
        if (!slotType.isPresent()) {
            throw new TextMessageException(Text.of(TextColors.RED, "Slot type ", TextColors.GOLD, type, TextColors.RED, " not found!"));
        }
        if (name.length() > 15) {
            throw new TextMessageException(Text.of(TextColors.RED, "The name ", TextColors.BLUE, name, TextColors.RED, " is too long!"));
        }
        if (XtraCasino.instance().getGsonHandler().isSlotNameAlreadyInUse(name)) {
            throw new TextMessageException(
                    Text.of(TextColors.RED, "A slot with the name ", TextColors.BLUE, name, TextColors.RED, " already exists!"));
        }
        SlotMachine machine =
                new SlotMachine(name, slotType.get(), src.getLocation().getPosition().floor(), SlotState.ACTIVE, src.getWorld().getUniqueId(), cost);
        BlockSlotTransactionResult transaction =
                XtraCasino.instance().getBlockHandler().generateBase(machine, DirectionHandler.getCardinalDirectionFromYaw(src.getRotation().getY()));
        if (transaction.getType().equals(Type.FAILURE_WORLD_NOT_FOUND)) {
            throw new TextMessageException(Text.of(TextColors.RED, "Could not find the world!"));
        }
        XtraCasino.instance().getGsonHandler().saveSlot(machine.setSlotBlocks(transaction.getBlocks()).setSlots(transaction.getSlots()));
        src.sendMessage(
                Text.of(TextColors.GREEN, "Success! ", TextColors.GOLD, "Created slot machine ", TextColors.BLUE, name, TextColors.GOLD, "!"));
        return CommandResult.success();
    }
}
