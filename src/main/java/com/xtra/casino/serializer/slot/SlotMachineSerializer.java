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

package com.xtra.casino.serializer.slot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import com.xtra.casino.api.slot.SlotMachine;
import com.xtra.casino.api.slot.SlotState;
import com.xtra.casino.api.slot.SlotType;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class SlotMachineSerializer implements TypeSerializer<SlotMachine> {

    @Override
    public SlotMachine deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String slotName = value.getNode("name").getString();
        SlotType slotType = SlotType.valueOf(value.getNode("type").getString());
        SlotState slotState = SlotState.valueOf(value.getNode("state").getString());

        String vectorString = value.getNode("location").getString();
        String[] coordinates = vectorString.split(",");
        double x = Double.valueOf(coordinates[0]);
        double y = Double.valueOf(coordinates[1]);
        double z = Double.valueOf(coordinates[2]);

        UUID worldUUID = UUID.fromString(value.getNode("world").getString());
        Optional<World> optional = Sponge.getServer().getWorld(worldUUID);
        if (!optional.isPresent()) {
            // If no world then return as-is.
            return new SlotMachine(slotName, slotType, new Vector3d(x, y, z), slotState, worldUUID);
        }

        Set<Location<World>> blockLocs = new HashSet<>();
        for (ConfigurationNode node : value.getNode("blocks").getChildrenList()) {
            String[] coordinates2 = node.getString().split(",");
            double x2 = Double.valueOf(coordinates2[0]);
            double y2 = Double.valueOf(coordinates2[1]);
            double z2 = Double.valueOf(coordinates2[2]);
            blockLocs.add(new Location<World>(optional.get(), x2, y2, z2));
        }

        return new SlotMachine(slotName, slotType, new Vector3d(x, y, z), slotState, worldUUID).setSlotBlocks(blockLocs);
    }

    @Override
    public void serialize(TypeToken<?> type, SlotMachine obj, ConfigurationNode value) throws ObjectMappingException {
        Vector3d vector = obj.getPosition();
        value.getNode("name").setValue(obj.getName());
        value.getNode("type").setValue(obj.getType().toString());
        value.getNode("location").setValue(vector.getX() + "," + vector.getY() + "," + vector.getZ());
        value.getNode("state").setValue(obj.getState().toString());
        value.getNode("world").setValue(obj.getWorldUUID().toString());
        for (Location<World> blockLoc : obj.getSlotBlocks()) {
            value.getNode("blocks").getAppendedNode().setValue(blockLoc.getX() + "," + blockLoc.getY() + "," + blockLoc.getZ());
        }
    }
}
