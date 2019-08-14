package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public class VertBox extends BasicBoxContent {

	protected int spacing;

	public VertBox(final int x, final int y) {
		super(x, y);
		spacing = 18;
	}

	public VertBox setSpacing(final int spacing) {
		this.spacing = spacing;
		return this;
	}

	public int getHeight() {
		return (contents.size() + 1) * spacing;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		final int xOffset = x + this.x;
		int yOffset = y + this.y;

		for (final BoxContent content : contents) {
			renderContent(ri, batch, xOffset, yOffset, content);
			yOffset += spacing;
		}
	}

}
