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

package com.xtra.casino.serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.Sponge;

import com.google.common.reflect.TypeToken;
import com.xtra.casino.api.slot.SlotMachine;
import com.xtra.casino.serializer.slot.SlotMachineSerializer;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

/**
 * Handles saving slots to a json file.
 */
public class GsonHandler {

    private GsonConfigurationLoader loader;
    private ConfigurationNode node;
    private File slotsFile;
    private File directory;
    private TypeSerializerCollection serializers;
    private ConfigurationOptions options;

    public GsonHandler() {
        this.directory = new File(Sponge.getGame().getSavesDirectory() + "/xtracasino");
        this.slotsFile = new File(directory + "/slots.json");
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
        if (!this.slotsFile.exists()) {
            try {
                this.slotsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.loader = GsonConfigurationLoader.builder().setFile(slotsFile).build();
        serializers = TypeSerializers.getDefaultSerializers().newChild();
        serializers.registerType(TypeToken.of(SlotMachine.class), new SlotMachineSerializer());
        options = ConfigurationOptions.defaults().setSerializers(serializers);
        this.load();
    }

    /**
     * Saves a slot to a new node.
     * 
     * @param machine The machine to save
     */
    public void saveSlot(SlotMachine machine) {
        this.load();
        try {
            this.node.getAppendedNode().setValue(TypeToken.of(SlotMachine.class), machine);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        this.save();
    }

    /**
     * Gets a slot machine based off of the specified name of the machine.
     * 
     * @param name The name of the slot machine to get
     * @return A mapping of the node for the slot machine and the slot machine
     *         itself
     */
    public Optional<Map<ConfigurationNode, SlotMachine>> getSlot(String name) {
        this.load();
        for (ConfigurationNode configNode : this.node.getChildrenList()) {
            try {
                SlotMachine machine = configNode.getValue(TypeToken.of(SlotMachine.class));
                if (name.toLowerCase().equals(machine.getName().toLowerCase())) {
                    return Optional.of(this.addToMap(configNode, machine));
                }
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Removes a specified slot machine.
     * 
     * @param name The name of the slot machine to remove
     * @return False if a slot with the same name as the specified slot machine
     *         already exists
     */
    public boolean removeSlot(String name) {
        Optional<Map<ConfigurationNode, SlotMachine>> optional = this.getSlot(name);
        if (!optional.isPresent()) {
            return false;
        }
        Map<ConfigurationNode, SlotMachine> map = optional.get();
        map.keySet().iterator().next().setValue(null);
        this.save();
        return true;
    }

    /**
     * Overwrites the slot machine at the specified node with the new slot
     * machine.
     * 
     * @param node The node to overwrite
     * @param machine The new slot machine to write
     */
    public void overwrite(ConfigurationNode node, SlotMachine machine) {
        try {
            node.setValue(TypeToken.of(SlotMachine.class), machine);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        this.save();
    }

    /**
     * Gets all of the slot machines registered.
     * 
     * @return All of the slot machines
     */
    public List<SlotMachine> getAllSlots() {
        this.load();
        List<SlotMachine> slots = new ArrayList<>();
        for (ConfigurationNode configNode : this.node.getChildrenList()) {
            try {
                slots.add(configNode.getValue(TypeToken.of(SlotMachine.class)));
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }
        return slots;
    }

    /**
     * Checks if a slot name is already in use.
     * 
     * @param name The name of the slot
     * @return If the name is already in use
     */
    public boolean isSlotNameAlreadyInUse(String name) {
        if (this.getSlot(name).isPresent()) {
            return true;
        }
        return false;
    }

    private Map<ConfigurationNode, SlotMachine> addToMap(ConfigurationNode node, SlotMachine machine) {
        Map<ConfigurationNode, SlotMachine> cs = new HashMap<>();
        cs.put(node, machine);
        return cs;
    }

    private void load() {
        try {
            this.node = this.loader.load(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GsonConfigurationLoader loader() {
        return this.loader;
    }

    public ConfigurationNode node() {
        return this.node;
    }
}
