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
import java.util.HashMap;
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

// TODO: change around when Gson stuff is in XtraCore
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

    public void saveSlot(SlotMachine machine) {
        this.load();
        try {
            this.node.getAppendedNode().setValue(TypeToken.of(SlotMachine.class), machine);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        this.save();
    }

    public Optional<Map<ConfigurationNode, SlotMachine>> getSlot(String name) {
        this.load();
        for (ConfigurationNode configNode : this.node.getChildrenList()) {
            try {
                SlotMachine machine = configNode.getValue(TypeToken.of(SlotMachine.class));
                if (name.equals(machine.getName())) {
                    Map<ConfigurationNode, SlotMachine> cs = new HashMap<>();
                    cs.put(configNode, machine);
                    return Optional.of(cs);
                }
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public boolean removeSlot(String name) {
        this.load();
        Optional<Map<ConfigurationNode, SlotMachine>> optional = this.getSlot(name);
        if (!optional.isPresent()) {
            return false;
        }
        Map<ConfigurationNode, SlotMachine> map = optional.get();
        map.keySet().iterator().next().setValue(null);
        this.save();
        return true;
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
