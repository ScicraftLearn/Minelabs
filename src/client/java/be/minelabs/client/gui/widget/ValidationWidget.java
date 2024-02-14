package be.minelabs.client.gui.widget;

import be.minelabs.Minelabs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ValidationWidget extends ButtonWidget {

    private static final Identifier TEX = new Identifier(Minelabs.MOD_ID, "textures/gui/validation.png");

    private int status = 0;

    public ValidationWidget(int x, int y) {
        super(x, y, 16, 16, Text.of(""), button -> {
        }, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawTexture(matrices, TEX, this.getX(), this.getY(), 0, getOffset(), 0, width, height, 16, 16);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private int getOffset() {
        return status * 17;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
