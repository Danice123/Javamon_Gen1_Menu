package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public class HorzBox extends BasicBoxContent {

	private int spacing;

	public HorzBox(final int x, final int y) {
		super(x, y);
		spacing = 18;
	}

	public HorzBox setSpacing(final int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		int xOffset = x + this.x;
		final int yOffset = y + this.y;

		for (final BoxContent content : contents) {
			renderContent(ri, batch, xOffset, yOffset, content);
			xOffset += spacing;
		}
	}
}
