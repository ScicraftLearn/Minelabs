package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.entity.LewisBlockEntity;
import be.uantwerpen.scicraft.lewisrecipes.BondManager;
import be.uantwerpen.scicraft.lewisrecipes.DelegateSettings;
import be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid;
import be.uantwerpen.scicraft.lewisrecipes.MoleculeItemGraph;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.*;

public class LewisScreen extends HandledScreen<LewisBlockScreenHandler> implements ScreenHandlerProvider<LewisBlockScreenHandler>{
    private static final Identifier TEXTURE = new Identifier("scicraft", "textures/block/lewiscrafting/lewis_block_inventory_craftable.png");
    private static final Identifier TEXTURE2 = new Identifier("scicraft", "textures/block/lewiscrafting/lewis_block_inventory_default.png");
    private Identifier currentTexture;
    private ButtonWidget buttonWidget;
    private boolean widgetTooltip = false;

    public LewisScreen(LewisBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTexture = TEXTURE2;
        // 3x18 for 3 inventory slots | +4 for extra offset to match the double chest | +5 for the row between the 5x5 grid and the input slots
        backgroundHeight += (18 * 3 + 4) + 5;

        // Add button to clear input/grid
        registerButtonWidget();
    }

    public ButtonWidget getButtonWidget() {
        return buttonWidget;
    }

    /*
     * draw function is called every tick
     */
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.setCorrectTexture();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, currentTexture);

        // executed in HandledScreen<? extends ScreenHandler> by minecraft itself
//        (protected) int x = (width - backgroundWidth) / 2;
//        (protected) int y = (height - backgroundHeight) / 2;

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        buttonWidget.renderButton(matrices, mouseX, mouseY, delta);

        // get crafting progress and handle its
        int cp = handler.getProgress();
        if (cp >= 0)
            drawTexture(matrices, x + 100, y + 51, 176, 0, cp, 20);

        // Keep mapping between stack (in graph) and slots (for rendering)
        Map<ItemStack, Slot> stackToSlotMap = new HashMap<>();
        for (int i = 0; i < 25; i++) {
            stackToSlotMap.put(handler.getInventory().getStack(i), handler.getSlot(i));
        }

        /*
         * Draw Bonds on screen
         */
        LewisCraftingGrid grid = handler.getLewisCraftingGrid();
        MoleculeItemGraph graph = (MoleculeItemGraph) grid.getPartialMolecule().getStructure();
        for (MoleculeItemGraph.Edge edge : graph.getEdges()) {
            Slot slot1 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getFirst()));
            Slot slot2 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getSecond()));
            BondManager.Bond bond = new BondManager.Bond(slot1, slot2, edge.data.bondOrder);
            this.itemRenderer.renderInGuiWithOverrides(bond.getStack(), bond.getX() + x, bond.getY() + y);
        }

        List<ItemStack> ingredients = handler.getIngredients();
        System.out.println(handler.getIngredients());
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack temp = ingredients.get(i);
            if (temp.isEmpty() || this.handler.getDensity() == 0) {
                //handler.clearIngredients();
                break;
            }
            if (this.handler.getInventory().getStack(25 + i).getCount() < handler.getDensity()) {
                this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 8 + 18*i, 133+y-20);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 8 + 18*i, 133+y-20);
            }
            this.itemRenderer.renderInGuiWithOverrides(temp, x + 8 + 18*i, 133+y-20);
        }


        /*
         * Render input slot overlays
         */
     //   int slotReady = this.handler.getPropertyDelegate(DelegateSettings.LCT_SLOT_READY);

        // if it is allowed to put items in the input slots:
//        if (slotItems > 1 && this.getScreenHandler().isInputOpen()) {
//            // hashed mappings for the slots
//            List<Integer> slotItemList = this.getSlotList(slotItems);
//            List<Integer> slotReadyList = this.getSlotList(slotReady);
//            slotItemList.sort(Comparator.comparingInt(o -> o));
////            Scicraft.LOGGER.info("slotList: " + slotItemList);
//
//            // textures to show whether a slot is ready or not
//            ItemStack ready = new ItemStack(net.minecraft.item.Items.LIME_STAINED_GLASS_PANE);
//            ItemStack notReady = new ItemStack(net.minecraft.item.Items.RED_STAINED_GLASS_PANE);
//
//            //125 = 18-(11-29)+12+4*18+5)  <-- offset for input slots
//            int y_val = 113 + y;
//            int offset = 0;
//
//            // loop over the slots and place the correct atom on the index
//            for (int P_slot : slotItemList) {
//                ItemStack temp = new ItemStack(DelegateSettings.ATOM_MAPPINGS.inverse().get(P_slot));
//                this.itemRenderer.renderInGuiWithOverrides(temp, x + 8 + offset, y_val);
//                offset += 18;
//            }
//
//            // create a new list where all the indexes of the ready slots are stored
//            List<Integer> readyIndex = new ArrayList<>();
//
//            // loop over the hashed indexes and retrieve the corresponding index from the map, then render it as 'ready'
//            for (int r : slotReadyList) {
//                offset = DelegateSettings.SLOT_MAPPINGS.inverse().get(r) * 18;
//                this.itemRenderer.renderInGuiWithOverrides(ready, x + 8 + offset, y_val);
//                readyIndex.add(DelegateSettings.SLOT_MAPPINGS.inverse().get(r));
//            }
//
//            // for each slot, check if it maybe isn't ready yet
//            for (int i = 0; i < slotItemList.size(); ++i) {
//                // if the slot isn't ready
//                if (!readyIndex.contains(i)) {
//                    this.itemRenderer.renderInGuiWithOverrides(notReady, x + 8 + i * 18, y_val);
//                }
//            }
//        }

    }

    @Override
    protected void init() {
        super.init();
        // Center the title
//        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
//        playerInventoryTitleX = (backgroundWidth - textRenderer.getWidth(playerInventoryTitle)) / 2;

        // move the title to the correct place
        playerInventoryTitleY += 61;

        registerButtonWidget();
    }


    @SuppressWarnings("ConstantConditions")
    private void registerButtonWidget() {
        buttonWidget = new ButtonWidget(x + 133, y + 17, 18, 18, Text.of("C"),
                button -> {
                    if (!widgetTooltip) return;
                    if (handler.isInputEmpty()) {
                        for (int i = 0; i < 25; i++) {
                            client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.PICKUP, client.player);
                        }
                    } else {
                        for (int i = 0; i < 9; i++) {
                            client.interactionManager.clickSlot(handler.syncId, i + 25, 0, SlotActionType.QUICK_MOVE, client.player);
                        }
                    }
                },
                (button, matrixStack, mx, my) -> {
                    // On Button Hover:
                    renderTooltip(matrixStack, Text.of(handler.isInputEmpty() ? "Clear 5x5 Grid" : "Clear Bottom Input Slots"), mx, my);
                }
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        widgetTooltip = false;
        if (this.handler.getCursorStack().isEmpty()) {
            if (this.focusedSlot != null && this.focusedSlot.hasStack())
                this.renderTooltip(matrices, this.focusedSlot.getStack(), mouseX, mouseY);
            if ((mouseX >= x + 133 && mouseX < x + 133 + 18)
                    && (mouseY >= y + 17 && mouseY < y + 17 + 18)) {
                buttonWidget.renderTooltip(matrices, mouseX, mouseY);
                widgetTooltip = true;
            }
        }
    }

    protected void setCorrectTexture() {
        int textureID = 1;
        if (textureID == 0) {
            this.currentTexture = TEXTURE2;
        } else if (textureID == 1) {
            this.currentTexture = TEXTURE;
        }
    }
}
