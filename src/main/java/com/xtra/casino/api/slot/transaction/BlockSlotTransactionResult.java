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

package com.xtra.casino.api.slot.transaction;

import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BlockSlotTransactionResult {

    private Type type;
    private Set<Location<World>> blocks;
    private Set<Location<World>> slots;

    public BlockSlotTransactionResult(Type type, @Nullable Set<Location<World>> blocks, @Nullable Set<Location<World>> slots) {
        this.type = type;
        this.blocks = blocks;
        this.slots = slots;
    }

    public Type getType() {
        return this.type;
    }

    public Set<Location<World>> getBlocks() {
        return this.blocks;
    }

    public Set<Location<World>> getSlots() {
        return this.slots;
    }

    public enum Type {
        SUCCESS, FAILURE_WORLD_NOT_FOUND;
    }
}
