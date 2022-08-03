package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import be.uantwerpen.scicraft.NuclidesTable;

import java.util.ArrayList;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> protonInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> neutronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> electronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private final NuclidesTable nuclidesTable = new NuclidesTable();

    public BohrBlockEntity( BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    private int getProtonCount(){
        return countList(this.getProtonInventory());
    }
    private int getNeutronCount(){
        return countList(this.getNeutronInventory());
    }
    private int getElectronCount(){
        return countList(this.getElectronInventory());
    }

    private int countList(DefaultedList<ItemStack> list) {
        return list.get(0).getCount() + list.get(1).getCount() + list.get(2).getCount();
    }
    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }
    public void renderText(){

        int nrOfProtons = getProtonCount();
        int nrOfNeutrons = getNeutronCount();
        int nrOfElectrons = getElectronCount();
        MatrixStack matrixStack = new MatrixStack();

        ArrayList<String> nuclideInfo = nuclidesTable.getNuclide(nrOfProtons, nrOfNeutrons);
        String atomName = nuclideInfo.get(0);
        String symbol = nuclideInfo.get(1);
        String mainDecayMode = nuclideInfo.get(2);

//        symbol = "<sub>" + nrOfProtons + "</sub>" + symbol;


        String protonString = "#Protons: " + nrOfProtons;
        String electronString = "#Electrons: " + nrOfElectrons;
        String neutronString = "#Neutrons: " + nrOfNeutrons;

        MinecraftClient.getInstance().textRenderer.draw(matrixStack, atomName, 10, 10, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, symbol, 20, 10, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, mainDecayMode, 30, 10, 111455);

        MinecraftClient.getInstance().textRenderer.draw(matrixStack, protonString, 10, 20, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, neutronString, 10, 30, 111455);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, electronString, 10, 40, 111455);
    }
    public DefaultedList<ItemStack> getProtonInventory() {
        return protonInventory;
    }

    public DefaultedList<ItemStack> getNeutronInventory() {
        return neutronInventory;
    }

    public DefaultedList<ItemStack> getElectronInventory() {
        return electronInventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        //super.readNbt(nbt);
        Inventories.readNbt(nbt, this.protonInventory);
        Inventories.readNbt(nbt, this.neutronInventory);
        Inventories.readNbt(nbt, this.electronInventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.protonInventory);
        Inventories.writeNbt(nbt, this.neutronInventory);
        Inventories.writeNbt(nbt, this.electronInventory);
    }

}
