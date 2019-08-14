package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.display.RenderInfo;

public class Gen1HealthBar {

	public void render(final RenderInfo ri, final ShapeRenderer shape,
			final float currentHealthPercent, final int x, final int y, final int width,
			final int height) {
		shape.setColor(0f, 0f, 0f, 0f);
		shape.rect(x, y + ri.getScale(), width, height - 2 * ri.getScale());
		shape.rect(x + ri.getScale(), y, width - 2 * ri.getScale(), height);
		shape.setColor(1f, 1f, 1f, 0f);
		shape.rect(x + ri.getScale(), y + ri.getScale(), width - 2 * ri.getScale(),
				height - 2 * ri.getScale());
		shape.setColor(0f, 1f, 0f, 0f);
		if (currentHealthPercent < .5f) {
			shape.setColor(1f, 1f, 0f, 0f);
		}
		if (currentHealthPercent < .1f) {
			shape.setColor(1f, 0f, 0f, 0f);
		}
		shape.rect(x + ri.getScale(), y + ri.getScale(),
				currentHealthPercent * width - 2 * ri.getScale(), height - 2 * ri.getScale());
	}

}
