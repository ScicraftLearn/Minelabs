package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import be.uantwerpen.scicraft.util.NuclidesTable;
import be.uantwerpen.scicraft.util.NucleusState;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiling.jfr.sample.NetworkIoStatistics;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

//import java.util.ArrayList;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {
    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int GREEN_COLOR = ColorHelper.Argb.getArgb(255, 0, 255, 0);
    public static final int RED_COLOR = ColorHelper.Argb.getArgb(255, 255, 0, 0);


    public long time = 0;
    public static final int maxTimerAmount = 30;
    public static final IntProperty TIMER = IntProperty.of("timer",0, maxTimerAmount);
    public static final IntProperty STATUS = IntProperty.of("status",0, 2);


    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public BohrBlockEntity( BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    public int getProtonCount(){
        int count = 0;
        for (int slot=0; slot <3; slot++)
            count += getItems().get(slot).getCount();
        return count;
    }
    public int getNeutronCount(){
        int count = 0;
        for (int slot=3; slot <6; slot++)
            count += getItems().get(slot).getCount();
        return count;
    }
    public int getElectronCount(){
        int count = 0;
        for (int slot=6; slot <9; slot++)
            count += getItems().get(slot).getCount();
        return count;
    }

    private int countList(DefaultedList<ItemStack> list) {
        return list.get(0).getCount() + list.get(1).getCount() + list.get(2).getCount();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
    public void renderText(){
        int nrOfProtons = getProtonCount();
        int nrOfNeutrons = getNeutronCount();
        int nrOfElectrons = getElectronCount();

        MatrixStack matrixStack = new MatrixStack();
        NucleusState nuclideStateInfo = NuclidesTable.getNuclide(nrOfProtons, nrOfNeutrons);
        String protonString = "#Protons: " + nrOfProtons;
        String electronString = "#Electrons: " + nrOfElectrons;
        String neutronString = "#Neutrons: " + nrOfNeutrons;
        String atomName = "None";
        String symbol = "None";
        String mainDecayMode = "Unstable";
        String ionicCharge = NuclidesTable.calculateIonicCharge(nrOfProtons, nrOfElectrons);
        String ion = "ion: " + ionicCharge;
        int color = RED_COLOR;
        if ( nuclideStateInfo != null ) {
            atomName = nuclideStateInfo.getAtomName();
            symbol = nuclideStateInfo.getSymbol();
            mainDecayMode = nuclideStateInfo.getMainDecayMode();

            if (nuclideStateInfo.isStable()){
                color = GREEN_COLOR;
            }
        }
        String atomInfo = mainDecayMode + "    " + atomName + "    " + symbol + "    " + ion + "    Timer: " + this.getCachedState().get(TIMER);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, atomInfo, 10, 10, color);

        MinecraftClient.getInstance().textRenderer.draw(matrixStack, protonString, 10, 20, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, neutronString, 10, 30, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, electronString, 10, 40, 111455);
    }

    @Override
    public void markDirty() {
        super.markDirty();

    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }



    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.items);
    }


    public ActionResult insertParticle(Item item) {
        int slot, times_looped = 0;

        if (item == Items.PROTON) {
            slot = 0;
        }
        else if (item == Items.NEUTRON) {
            slot = 3;
        }
        else if (item == Items.ELECTRON) {
            slot = 6;
        }
        else {
            return ActionResult.FAIL;
        }

        while (items.get(slot).getCount() == 64 && items.get(slot).getItem() == item) {
            slot += 1;
            times_looped +=1;
            if (times_looped == 3) return ActionResult.FAIL;
        }
        assert items.get(slot).getCount() < 64;
//            if the inventory is empty initialize the inventory with 0 items
        if (items.get(slot).isEmpty()) {
//                the item that the player was holding
            items.set(slot, new ItemStack(item));
//                set the counter for the item on 0
            items.get(slot).setCount(0);
        }
//            if the stack isn't full
        if (items.get(slot).getCount() < 64) {
//                add 1 to the inventory
            items.get(slot).setCount(items.get(slot).getCount() + 1);
        }
        return ActionResult.SUCCESS;
    }


    public void scatterParticles(){
        ItemScatterer.spawn(Objects.requireNonNull(getWorld()), getPos().up(1), items);
    }

    /**
     * Removes a particle (proton, neutron or electron depending on passed param) from the bohrblock.
     *
     * @param item : item particle to be removed.
     */
    public ActionResult removeParticle(Item item) {
            if (item == Items.ANTI_PROTON) {
                for (int index = 0; index < 3; index++) {
                    if (items.get(index).getCount() > 0) {
                        items.get(index).setCount(items.get(index).getCount() - 1);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        else if (item == Items.ANTI_NEUTRON) {
                for (int index = 3; index < 6; index++) {
                    if (items.get(index).getCount() > 0) {
                        items.get(index).setCount(items.get(index).getCount() - 1);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        else if (item == Items.POSITRON) {
                for (int index = 6; index < 9; index++) {
                    if (items.get(index).getCount() > 0) {
                        items.get(index).setCount(items.get(index).getCount() - 1);
                        return ActionResult.SUCCESS;
                    }
                }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    public static void writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        nbt.put("Items", nbtList);
    }

}
