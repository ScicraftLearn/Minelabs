package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import be.uantwerpen.scicraft.util.NuclidesTable;
import be.uantwerpen.scicraft.util.NucleusState;

import java.util.ArrayList;

//import java.util.ArrayList;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> protonInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> neutronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> electronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private final NuclidesTable nuclidesTable = new NuclidesTable();

    public BohrBlockEntity( BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    public int getProtonCount(){
        return countList(this.getProtonInventory());
    }
    public int getNeutronCount(){
        return countList(this.getNeutronInventory());
    }
    public int getElectronCount(){
        return countList(this.getElectronInventory());
    }

    private int countList(DefaultedList<ItemStack> list) {
        return list.get(0).getCount() + list.get(1).getCount() + list.get(2).getCount();
    }

    public NuclidesTable getNuclidesTable() {
        return nuclidesTable;
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
        NucleusState nuclideStateInfo = nuclidesTable.getNuclide(nrOfProtons, nrOfNeutrons);
        String protonString = "#Protons: " + nrOfProtons;
        String electronString = "#Electrons: " + nrOfElectrons;
        String neutronString = "#Neutrons: " + nrOfNeutrons;

        if ( nuclideStateInfo != null ) {
            String atomName = nuclideStateInfo.getAtomName();
            String symbol = nuclideStateInfo.getSymbol();
            String mainDecayMode = nuclideStateInfo.getMainDecayMode();

            String ionicCharge = nuclidesTable.calculateIonicCharge(nrOfProtons, nrOfElectrons);
            String ion = "ion: " + ionicCharge;

            String atomInfo = mainDecayMode + "    " + atomName + "    " + symbol + "    " + ion;
            MinecraftClient.getInstance().textRenderer.draw(matrixStack, atomInfo, 10, 10, 111455);
        }

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


    public void empty(){
        for ( int index = 0; index < 3; index++ ){
            protonInventory.get(index).setCount(0);
            neutronInventory.get(index).setCount(0);
            electronInventory.get(index).setCount(0);
        }
    }

    /**
     * Removes a particle (proton, neutron or electron depending on passed param) from the bohrblock.
     *
     * @param particleName : String name of the particle to be removed.
     */
    public void removeParticle(String particleName) {
        switch (particleName) {
            case "proton":
                for (int index = 0; index < 3; index++) {
                    if (protonInventory.get(index).getCount() > 0) {
                        protonInventory.get(index).setCount(protonInventory.get(index).getCount() - 1);
                        break;
                    }
                }
                break;
            case "neutron":
                for (int index = 0; index < 3; index++) {
                    if (neutronInventory.get(index).getCount() > 0) {
                        neutronInventory.get(index).setCount(neutronInventory.get(index).getCount() - 1);
                        break;
                    }
                }
                break;
            case "electron":
                for (int index = 0; index < 3; index++) {
                    if (electronInventory.get(index).getCount() > 0) {
                        electronInventory.get(index).setCount(electronInventory.get(index).getCount() - 1);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.protonInventory);
        Inventories.writeNbt(nbt, this.neutronInventory);
        Inventories.writeNbt(nbt, this.electronInventory);
    }

}
