package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public class BorderBoxContent extends BasicBoxContent {

	private final int width;
	private final int height;

	public BorderBoxContent(final int x, final int y, final int width, final int height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;

		ri.border.drawBox(batch, xOffset * ri.getScale(),
				ri.screenHeight - yOffset * ri.getScale() - height * ri.getScale(),
				width * ri.getScale(), height * ri.getScale());

		for (final BoxContent content : contents) {
			renderContent(ri, batch, xOffset + 6, yOffset + 14, content);
		}
	}

}
