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

package com.xtra.casino.api.slot;

import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class SlotMachine {

    private String name;
    private SlotType type;
    private Vector3d vector;
    private SlotState state;
    private UUID worldUUID;
    private double cost;
    private Set<Location<World>> slotBlocks;
    private Set<Location<World>> slots;

    public SlotMachine() {
    }

    public SlotMachine(String name, SlotType type, Vector3d vector, SlotState state, UUID worldUUID, double cost) {
        this.name = name;
        this.type = type;
        this.vector = vector;
        this.state = state;
        this.worldUUID = worldUUID;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public SlotType getType() {
        return this.type;
    }

    public Vector3d getPosition() {
        return this.vector;
    }

    public SlotState getState() {
        return this.state;
    }

    public SlotMachine setState(SlotState state) {
        this.state = state;
        return this;
    }

    public UUID getWorldUUID() {
        return this.worldUUID;
    }

    public double getCost() {
        return this.cost;
    }

    public Set<Location<World>> getSlotBlocks() {
        return this.slotBlocks;
    }

    public SlotMachine setSlotBlocks(Set<Location<World>> slotBlocks) {
        this.slotBlocks = slotBlocks;
        return this;
    }

    public Set<Location<World>> getSlots() {
        return this.slots;
    }

    public SlotMachine setSlots(Set<Location<World>> slots) {
        this.slots = slots;
        return this;
    }
}
