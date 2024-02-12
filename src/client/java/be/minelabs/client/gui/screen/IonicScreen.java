package be.minelabs.client.gui.screen;

import be.minelabs.Minelabs;
import be.minelabs.client.gui.widget.CounterButtonWidget;
import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.BondManager;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import be.minelabs.screen.IonicBlockScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class IonicScreen extends HandledScreen<IonicBlockScreenHandler> implements ScreenHandlerProvider<IonicBlockScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/ionic_gui.png");

    private ButtonWidget clear_btn;

    private CounterButtonWidget left_minus;
    private CounterButtonWidget left_plus;
    private CounterButtonWidget right_minus;
    private CounterButtonWidget right_plus;

    //just the height and width of the texture
    public IonicScreen(IonicBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 220;
        backgroundWidth = 205;
        playerInventoryTitleX = 22;
        playerInventoryTitleY = 127;
    }

    @Override
    protected void init() {
        super.init();

        registerButtons();
    }

    private void registerButtons() {
        clear_btn = ButtonWidget.builder(Text.of("C"), button -> {
            client.interactionManager.clickButton(handler.syncId, 0);
            button.setFocused(false);
        }).dimensions(x + 178, y + 80, 20, 20).build();

        int start_left = x + 42;
        int start_right = x + 112;
        int y_counter = y + 88;

        left_minus = new CounterButtonWidget(start_left, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 1);
            button.setFocused(false);
        });

        left_plus = new CounterButtonWidget(start_left + 11, y_counter, CounterButtonWidget.Type.PLUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 2);
            button.setFocused(false);
        });

        right_minus = new CounterButtonWidget(start_right, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 3);
            button.setFocused(false);
        });

        right_plus = new CounterButtonWidget(start_right + 11, y_counter, CounterButtonWidget.Type.PLUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 4);
            button.setFocused(false);
        });

        addDrawableChild(clear_btn);
        addDrawableChild(left_minus);
        addDrawableChild(left_plus);
        addDrawableChild(right_minus);
        addDrawableChild(right_plus);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Keep mapping between stack (in graph) and slots (for rendering)
        Map<ItemStack, Slot> stackToSlotMap = new HashMap<>();
        for (int i = 0; i < IonicBlockScreenHandler.GRIDSIZE * 2; i++) {
            stackToSlotMap.put(handler.getInventory().getStack(i), handler.getSlot(i));
        }

        /*
         * Draw Bonds on screen
         */
        LewisCraftingGrid grid = getScreenHandler().getInventory().getLeftGrid();
        MoleculeItemGraph graph = (MoleculeItemGraph) grid.getPartialMolecule().getStructure();
        for (MoleculeItemGraph.Edge edge : graph.getEdges()) {
            Slot slot1 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getFirst()));
            Slot slot2 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getSecond()));
            BondManager.Bond bond = new BondManager.Bond(slot1, slot2, edge.data.bondOrder);
            this.itemRenderer.renderInGuiWithOverrides(matrices, bond.getStack(), bond.getX() + x, bond.getY() + y);
        }

        /*
         * Draw Bonds on screen
         */
        LewisCraftingGrid grid2 = getScreenHandler().getInventory().getRightGrid();
        MoleculeItemGraph graph2 = (MoleculeItemGraph) grid2.getPartialMolecule().getStructure();
        for (MoleculeItemGraph.Edge edge : graph2.getEdges()) {
            Slot slot1 = stackToSlotMap.get(graph2.getItemStackOfVertex(edge.getFirst()));
            Slot slot2 = stackToSlotMap.get(graph2.getItemStackOfVertex(edge.getSecond()));
            BondManager.Bond bond = new BondManager.Bond(slot1, slot2, edge.data.bondOrder);
            this.itemRenderer.renderInGuiWithOverrides(matrices, bond.getStack(), bond.getX() + x, bond.getY() + y);
        }

        /*
         * Render input slot overlays
         */
        DefaultedList<Ingredient> leftIngredients = handler.getLeftIngredients();
        for (int i = 0; i < leftIngredients.size(); i++) {
            ItemStack atom = leftIngredients.get(i).getMatchingStacks()[0];
            if (this.handler.getLeftDensity() == 0 || atom.isEmpty()) {
                break;
            }
            if (handler.getInventory().getStack(2 * IonicBlockScreenHandler.GRIDSIZE + i).getCount() < handler.getLeftDensity()) {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 12 + 18 * i, 86 + y);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 12 + 18 * i, 86 + y);
            }
            this.itemRenderer.renderInGuiWithOverrides(matrices, atom, x + 12 + 18 * i, 86 + y);
        }

        /*
         * Render input slot overlays
         */
        DefaultedList<Ingredient> rightIngredients = handler.getRightIngredients();
        for (int i = 0; i < rightIngredients.size(); i++) {
            ItemStack atom = rightIngredients.get(i).getMatchingStacks()[0];
            if (this.handler.getRightDensity() == 0 || atom.isEmpty()) {
                break;
            }
            if (handler.getInventory().getStack(2 * IonicBlockScreenHandler.GRIDSIZE + i + leftIngredients.size()).getCount() < handler.getRightDensity()) {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
            }
            this.itemRenderer.renderInGuiWithOverrides(matrices, atom, x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
        }
        if (handler.getLeftCharge() != 0 && handler.getRightCharge() != 0) {
            this.textRenderer.draw(matrices, "+" + handler.getLeftCharge(), 66 + x, 6 + y, 0);
            this.textRenderer.draw(matrices, "-" + handler.getRightCharge(), 139 + x, 6 + y, 0);
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
