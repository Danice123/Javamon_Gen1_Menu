package dev.dankins.javamon.display.screen.menu.content.box;

import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;

public class VertBox extends BasicBoxContent {

	protected int spacing;

	public VertBox(final int x, final int y) {
		super(x, y);
		spacing = 8;
	}

	@Override
	public int getContentHeight() {
		int height = 0;
		for (final Content content : contents) {
			height += content.getHeight();
		}
		height += (contents.size() - 1) * spacing;
		return height;
	}

	public VertBox setSpacing(final int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		final int xOffset = x + this.x;
		int yOffset = y + this.y;

		for (final Content content : contents) {
			renderInternal(rh, xOffset, yOffset, content);
			yOffset += content.getHeight() + spacing;
		}
	}

}
