package dev.efekos.cla.rei.widget;

import dev.efekos.cla.Main;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.util.Identifier;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;

import java.util.List;

public class KnifeWidget extends Widget {

    private final Point point;
    private int maxCuts = 0;

    public KnifeWidget(Point point) {
        this.point = point;
    }

    public static final Identifier KNIFE_TEXTURE = Identifier.of(Main.MOD_ID, "knife");
    public static final Identifier KNIFE_FULL = Identifier.of(Main.MOD_ID, "knife_full");

    private final NumberAnimator<Integer> cutAnimator = ValueAnimator.ofInt()
            .withConvention(() -> maxCuts, 5000)
            .asInt();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        cutAnimator.update(delta);
        context.drawGuiTexture(KNIFE_TEXTURE, point.x, point.y, 32, 11);
        if (cutAnimator.value() > 0)
            context.drawGuiTexture(KNIFE_FULL, 33, 12, 0, 0, point.x, point.y, 0, (int) ((cutAnimator.value() / (float) maxCuts) * 33), 12);
    }

    public KnifeWidget maxCuts(int i) {
        this.maxCuts = i;
        return this;
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }

}
