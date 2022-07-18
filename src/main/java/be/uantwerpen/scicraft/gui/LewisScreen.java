package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.lewisrecipes.DelegateSettings;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LewisScreen extends HandledScreen<LewisBlockScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("scicraft", "textures/block/lewiscrafting/lewis_block_inventory_craftable.png");
    private static final Identifier TEXTURE2 = new Identifier("scicraft", "textures/block/lewiscrafting/lewis_block_inventory_default.png");

    private Identifier currentTexture;

    private int tickCounter = 0;
    LewisBlockScreenHandler screenHandler;

    public LewisScreen(LewisBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        screenHandler = handler;
        this.currentTexture = TEXTURE2;

        // 3x18 for 3 inventory slots | +4 for extra offset to match the double chest | +5 for the row between the 5x5 grid and the input slots
        backgroundHeight += (18 * 3 + 4) + 5;
    }

    /**
     * draw function is called every tick
     */
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
//        List<ItemStack> a = new ArrayList<>();
//        for (int i = 0; i < 34; i++) {
//            a.add(screenHandler.getInventory().getStack(i));
//        }
//        System.out.println(a);
//        System.out.println("----------");

        int craftingProgress = screenHandler.getPropertyDelegate(1);

        //if the animation status is in the interval [0,22], we continue the animation
        if (craftingProgress >= 0 && craftingProgress < 23) {
            this.setCorrectTexture(craftingProgress);
        }

        //if there is no animation going on, use the default texture
        else {
            int textureID = screenHandler.getPropertyDelegate(0);
            if (textureID == 0) {
                this.currentTexture = TEXTURE2;
            } else if (textureID == 1) {
                this.currentTexture = TEXTURE;
            }
        }

        // we are at the end of the crafting animation, so we reset the animation status back to -1
        if (craftingProgress >= 23) {
            screenHandler.setOutput();
//            screenHandler.setStackInSlot(34, 1, new ItemStack(net.minecraft.item.Items.AIR));
//            screenHandler.setStackInSlot(34, 1, new ItemStack(Items.ANTI_DOWNQUARK_RED));
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, currentTexture);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        /*
         * Render input slot overlays
         */
        int slotItems = this.screenHandler.getPropertyDelegate(2);
        int slotReady = this.screenHandler.getPropertyDelegate(3);


        // if it is allowed to put items in slots:
        if (slotItems > 1) {

            // hashed mappings for the slots
            List<Integer> P_slots = this.getSlotList(slotItems);
            List<Integer> P_ready = this.getSlotList(slotReady);

            // textures to show whether a slot is ready or not
            ItemStack ready = new ItemStack(net.minecraft.item.Items.LIME_STAINED_GLASS_PANE);
            ItemStack notReady = new ItemStack(net.minecraft.item.Items.RED_STAINED_GLASS_PANE);

            //125 = 18-(11-29)+12+4*18+5)  <-- offset for input slots
            int y_val = 125;
            int offset = 0;

            // loop over the slots and place the correct atom on the index
            for (int P_slot : P_slots) {
                ItemStack temp = new ItemStack(DelegateSettings.ATOM_MAPPINGS.inverse().get(P_slot));
                this.itemRenderer.renderInGuiWithOverrides(temp, x + 8 + offset, y_val);
                offset += 18;
            }

            // reset the offset and create a new list where all the indexes of the ready slots are stored
            offset = 0;
            List<Integer> readyIndex = new ArrayList<>();

            // loop over the hashed indexes and retrieve the corresponding index from the map, then render it as 'ready'
            for (int r : P_ready) {
                offset = DelegateSettings.SLOT_MAPPINGS.inverse().get(r) * 18;
                this.itemRenderer.renderInGuiWithOverrides(ready, x + 8 + offset, y_val);
                readyIndex.add(DelegateSettings.SLOT_MAPPINGS.inverse().get(r));
            }

            // for each slot, check if it maybe isn't ready yet
            for (int i = 0; i < P_slots.size(); ++i) {

                // if the slot isn't ready
                if (!readyIndex.contains(i)) {
                    this.itemRenderer.renderInGuiWithOverrides(notReady, x + 8 + i * 18, y_val);
                }
            }
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
//        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
//        playerInventoryTitleX = (backgroundWidth - textRenderer.getWidth(playerInventoryTitle)) / 2;

        // move the title to the correct place
        playerInventoryTitleY += 61;
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();

        // increase the animation counter by 1
        if (tickCounter % 6 == 0) {

            // if the crafting is ongoing (aka it's not -1), keep going
            if (this.screenHandler.getPropertyDelegate(1) != -1) {
                this.screenHandler.setPropertyDelegate(1, this.screenHandler.getPropertyDelegate(1) + 1);
            }
            tickCounter = 1;
        }
        ++this.tickCounter;
    }

    protected void setCorrectTexture(int craftingProgress) {
        this.currentTexture = new Identifier("scicraft", "textures/block/lewiscrafting/crafting_progress/cp" + craftingProgress + ".png");
    }

    protected List<Integer> getSlotList(int N) {
        List<Integer> div = new ArrayList<>();

        // count number of 2s that divide N
        while (N % 2 == 0) {
            N /= 2;
            div.add(2);
        }

        // N must be odd at this point.
        // So we can skip one element
        for (int i = 3; i * i <= N; i = i + 2) {

            while (N % i == 0) {
                // divide the value of N
                N = N / i;
                div.add(i);
            }
        }

        // add the remaining number to the vector
        if (N != 1) div.add(N);
        return div;
    }

    @Override
    public void close() {
        super.close();
        this.screenHandler.setPropertyDelegate(1, -1);
    }
}
