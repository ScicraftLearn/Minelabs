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
import net.minecraft.client.gui.tooltip.Tooltip;
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

import java.util.Arrays;
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
        activeBTN();
    }

    @SuppressWarnings("ConstantConditions")
    private void registerButtons() {
        clear_btn = ButtonWidget.builder(Text.of("C"), button -> {
            client.interactionManager.clickButton(handler.syncId, 0);
            button.setFocused(false);
        }).dimensions(x + 177, y + 80, 18, 18).build();

        int start_left = x + 21;
        int start_right = x + 96;
        int y_counter = y + 88;

        int offsest = 24;

        left_minus = new CounterButtonWidget(start_left, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 1);
            button.setFocused(false);
        });

        left_plus = new CounterButtonWidget(start_left + offsest, y_counter, CounterButtonWidget.Type.PLUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 2);
            button.setFocused(false);
        });

        right_minus = new CounterButtonWidget(start_right, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 3);
            button.setFocused(false);
        });

        right_plus = new CounterButtonWidget(start_right + offsest, y_counter, CounterButtonWidget.Type.PLUS, button -> {
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
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderRecipeCheck(matrices, this.x, this.y);
        renderProgressArrow(matrices, this.x, this.y);

        // Keep mapping between stack (in graph) and slots (for rendering)
        Map<ItemStack, Slot> stackToSlotMap = new HashMap<>();
        for (int i = 0; i < IonicBlockScreenHandler.GRIDSIZE * 2; i++) {
            stackToSlotMap.put(handler.getInventory().getStack(i), handler.getSlot(i));
        }

        /*
         * Draw Bonds on screen
         */
        renderBonds(matrices, getScreenHandler().getInventory().getLeftGrid(), stackToSlotMap);
        renderBonds(matrices, getScreenHandler().getInventory().getRightGrid(), stackToSlotMap);

        /*
         * Render input slot overlays
         */
        DefaultedList<Ingredient> leftIngredients = handler.getLeftIngredients();
        for (int i = 0; i < leftIngredients.size(); i++) {
            ItemStack atom = leftIngredients.get(i).getMatchingStacks()[0];
            if (this.handler.getLeftDensity() == 0 || atom.isEmpty()) {
                break;
            }
            if (handler.getInventory().getLeftGrid().getStack(i).getCount() < handler.getLeftDensity()) {
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
            if (handler.getInventory().getRightGrid().getStack(i).getCount() < handler.getRightDensity()) {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
            }
            this.itemRenderer.renderInGuiWithOverrides(matrices, atom, x + 12 + 18 * i + 18 * leftIngredients.size(), 86 + y);
        }

        int charge = handler.getLeftCharge();
        this.textRenderer.draw(matrices, charge >= 0 ? "+" + charge : String.valueOf(charge), 70 + x, 20 + y, 0);

        charge = handler.getRightCharge();
        this.textRenderer.draw(matrices, charge >= 0 ? "+" + charge : String.valueOf(charge), 147 + x, 20 + y, 0);

        this.textRenderer.draw(matrices, Text.of(String.valueOf(handler.getLeftAmount())), x + 36, y + 91, 0x404040);
        this.textRenderer.draw(matrices, Text.of(String.valueOf(handler.getRightAmount())), x + 111, y + 91, 0x404040);
    }

    private void renderBonds(MatrixStack matrices, LewisCraftingGrid grid, Map<ItemStack, Slot> stackToSlotMap) {
        if (grid.getPartialMolecule().getStructure() instanceof MoleculeItemGraph graph) {
            BondManager manager = new BondManager();
            for (MoleculeItemGraph.Edge edge : graph.getEdges()) {
                Slot slot1 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getFirst()));
                Slot slot2 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getSecond()));
                BondManager.Bond bond = manager.getOrCreateBond(slot1, slot2, edge.data.bondOrder);
                this.itemRenderer.renderInGuiWithOverrides(matrices, bond.getStack(), bond.getX() + x, bond.getY() + y);
            }
        }
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 146, y + 45, 206, 0, handler.getScaledProgress(), 20);
        }
    }

    private void renderRecipeCheck(MatrixStack matrices, int x, int y) {
        int x_coord = x + 177, y_coord = y + 17;
        switch (handler.getStatus()) {
            case 1 -> drawTexture(matrices, x_coord, y_coord, 206, 52, 16, 16); // NOT IMPLEMENTED
            case 2 -> drawTexture(matrices, x_coord, y_coord, 206, 69, 16, 16); // VALID
            case 3 -> drawTexture(matrices, x_coord, y_coord, 206, 35, 16, 16); // TO MANY MOL
            default -> drawTexture(matrices, x_coord, y_coord, 206, 18, 16, 16); // INVALID
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);

        if (this.handler.getCursorStack().isEmpty()) {
            if (mouseX >= x + 177 && mouseX < x + 177 + 16 && mouseY >= y + 17 && mouseY < y + 17 + 16) {
                switch (handler.getStatus()) {
                    case 1 -> renderTooltip(matrices, Arrays.asList(
                            Text.translatable("text.minelabs.valid"),
                            Text.translatable("text.minelabs.not_implemented")), mouseX, mouseY);
                    case 2 -> renderTooltip(matrices,
                            Text.translatable("text.minelabs.valid"), mouseX, mouseY);
                    case 3 -> renderTooltip(matrices,
                            Text.translatable("text.minelabs.multiple_molecules"), mouseX, mouseY);
                    default -> renderTooltip(matrices,
                            Text.translatable("text.minelabs.invalid"), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean ret = super.mouseClicked(mouseX, mouseY, button);
        // button stays focussed after click which we don't want. This prevents it.
        if (ret)
            setFocused(null);
        return ret;
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
    }

    @Override
    protected void handledScreenTick() {
        activeBTN();

        clear_btn.setTooltip(Tooltip.of(handler.isInputEmpty() ?
                Text.translatableWithFallback("text.minelabs.clear_grid", "Clear Grid") :
                Text.translatableWithFallback("text.minelabs.clear_slots", "Clear Slots")));
    }

    private void activeBTN() {
        left_minus.active = handler.getLeftAmount() > 1;
        left_plus.active = handler.getLeftAmount() < 9;

        right_minus.active = handler.getRightAmount() > 1;
        right_plus.active = handler.getRightAmount() < 9;
    }
}
