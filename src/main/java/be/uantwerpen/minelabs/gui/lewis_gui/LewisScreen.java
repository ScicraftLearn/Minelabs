package be.uantwerpen.minelabs.gui.lewis_gui;


import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.lewis.LewisCraftingGrid;
import be.uantwerpen.minelabs.crafting.molecules.BondManager;
import be.uantwerpen.minelabs.crafting.molecules.MoleculeItemGraph;
import be.uantwerpen.minelabs.crafting.molecules.ValenceElectrons;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static be.uantwerpen.minelabs.gui.lewis_gui.LewisBlockScreenHandler.GRIDSIZE;


public class LewisScreen extends HandledScreen<LewisBlockScreenHandler> implements ScreenHandlerProvider<LewisBlockScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/lewis_block/lewis_block_inventory_craftable.png");
    private static final Identifier TEXTURE2 = new Identifier(Minelabs.MOD_ID, "textures/gui/lewis_block/lewis_block_inventory.png");
    private ButtonWidget buttonWidget;
    private boolean widgetTooltip = false;

    public LewisScreen(LewisBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.handler.hasRecipe()) {
            RenderSystem.setShaderTexture(0, TEXTURE);
        } else {
            RenderSystem.setShaderTexture(0, TEXTURE2);
        }

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(matrices, this.x, this.y);
        renderRecipeCheck(matrices, this.x, this.y);
        buttonWidget.renderButton(matrices, mouseX, mouseY, delta);

        // Keep mapping between stack (in graph) and slots (for rendering)
        Map<ItemStack, Slot> stackToSlotMap = new HashMap<>();
        for (int i = 0; i < GRIDSIZE; i++) {
            stackToSlotMap.put(handler.getLewisCraftingGrid().getStack(i), handler.getSlot(i));
        }

        /*
         * Draw Bonds on screen
         */
        LewisCraftingGrid grid = handler.getLewisCraftingGrid();
        BondManager manager = new BondManager();
        if (grid.getPartialMolecule().getStructure() instanceof MoleculeItemGraph graph){
            for (MoleculeItemGraph.Edge edge : graph.getEdges()) {
                Slot slot1 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getFirst()));
                Slot slot2 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getSecond()));
                BondManager.Bond bond = manager.getOrCreateBond(slot1, slot2, edge.data.bondOrder);
                this.itemRenderer.renderInGuiWithOverrides(bond.getStack(), bond.getX() + x, bond.getY() + y);
            }
            for (MoleculeItemGraph.Vertex vertex : graph.getVertices()) {
                Slot slot = stackToSlotMap.get(graph.getItemStackOfVertex(vertex));
                int total_bonds = 0;
                Map<String, Integer> bonds = manager.findEmptyBonds(slot);
                for (String key : bonds.keySet()) {
                    total_bonds += bonds.get(key);
                }

                ValenceElectrons valentieE = new ValenceElectrons(bonds,
                        vertex.data.getInitialValenceElectrons() - vertex.getEdgesData().stream().map(bond -> bond.bondOrder).mapToInt(Integer::intValue).sum()
                        , vertex.data.getMaxPossibleBonds() == total_bonds); //no clue: copied it from getOpenConnections in the MoleculeGraph class
                for (String i : Arrays.asList("n", "e", "s", "w")) { //render item 4x: N-E-S-W
                    if (valentieE.getDirectionalValence(i) != 0) {
                        this.itemRenderer.renderInGuiWithOverrides(valentieE.getStack(i), slot.x + x, slot.y + y);
                    }
                }
            }
        }

        /*
         * Render input slot overlays
         */
        DefaultedList<Ingredient> ingredients = handler.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack atom = ingredients.get(i).getMatchingStacks()[0];
            if(!this.handler.hasRecipe() || atom.isEmpty()) {
                break;
            }
            if (handler.getIoInventory().getStack(i).getCount() < handler.getDensity()) {
                this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 8 + 18*i, 133+y-20);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 8 + 18*i, 133+y-20);
            }
            this.itemRenderer.renderInGuiWithOverrides(atom, x + 8 + 18*i, 133+y-20);
        }
    }


    @Override
    protected void init() {
        super.init();

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
                        for (int i = 0; i < GRIDSIZE; i++) {
                            client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.PICKUP, client.player);
                        }
                    } else {
                        for (int i = 0; i < 9; i++) {
                            client.interactionManager.clickSlot(handler.syncId, i + GRIDSIZE, 0, SlotActionType.QUICK_MOVE, client.player);
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
            if (mouseX >= x + 105 && mouseX < x + 105 + 16 && mouseY >= y + 17 && mouseY < y + 17 + 16) {
                switch (handler.getStatus()) {
                    case 1 -> renderTooltip(matrices, Arrays.asList(
                            Text.translatable("text.minelabs.valid"),
                            Text.translatable("text.minelabs.not_implemented")), mouseX, mouseY);
                    case 2 -> renderTooltip(matrices,
                            Text.translatable("text.minelabs.valid"), mouseX, mouseY);
                    case 3 ->  renderTooltip(matrices,
                            Text.translatable("text.minelabs.multiple_molecules"), mouseX, mouseY);
                    default -> renderTooltip(matrices,
                            Text.translatable("text.minelabs.invalid"), mouseX, mouseY);
                }

            }
        }
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 102, y + 52, 176, 0, handler.getScaledProgress(), 20);
        }
    }

    private void renderRecipeCheck(MatrixStack matrices, int x, int y) {
        switch (handler.getStatus()) {
            case 1 -> drawTexture(matrices, x + 105, y + 17, 176, 38, 16, 16); // NOT IMPLEMENTED
            case 2 -> drawTexture(matrices, x + 105, y + 17, 176, 55, 16, 16); // VALID
            case 3 -> drawTexture(matrices, x + 105, y + 17, 176, 38, 16, 16);
            default -> drawTexture(matrices, x + 105, y + 17, 176, 21, 16, 16); // INVALID
        }
    }
}
