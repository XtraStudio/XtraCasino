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

package com.xtra.casino.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.xtra.casino.api.slot.SlotMachine;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult;
import com.xtra.casino.api.slot.transaction.BlockSlotTransactionResult.Type;

public class SlotBlockHandler {

    public BlockSlotTransactionResult generateBase(SlotMachine machine, Direction direction) {
        Optional<World> optional = Sponge.getServer().getWorld(machine.getWorldUUID());
        if (!optional.isPresent()) {
            return new BlockSlotTransactionResult(Type.FAILURE_WORLD_NOT_FOUND, null);
        }
        List<Text> signText = new ArrayList<>();
        signText.add(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "[Slot Machine]"));
        signText.add(Text.of(TextColors.DARK_BLUE, TextStyles.BOLD, machine.getName()));

        Set<Location<World>> blocks = new HashSet<>();

        Location<World> loc = new Location<World>(optional.get(), machine.getPosition());
        loc.setBlockType(BlockTypes.WALL_SIGN);
        loc.offer(Keys.SIGN_LINES, signText);
        loc.offer(Keys.DIRECTION, direction.getOpposite());

        Location<World> loc1_1 = loc.getRelative(direction);
        loc1_1.setBlockType(BlockTypes.STONEBRICK);

        blocks.add(loc);
        blocks.add(loc1_1);
        return new BlockSlotTransactionResult(Type.SUCCESS, blocks);
    }

    public void removeBase(Set<Location<World>> blocks) {
        for (Location<World> loc : blocks) {
            loc.removeBlock();
        }
    }
}
