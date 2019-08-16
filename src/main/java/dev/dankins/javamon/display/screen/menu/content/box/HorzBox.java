package dev.dankins.javamon.display.screen.menu.content.box;

import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;

public class HorzBox extends BasicBoxContent {

	private int spacing;

	public HorzBox(final int x, final int y) {
		super(x, y);
		spacing = 4;
	}

	@Override
	public int getContentWidth() {
		int width = 0;
		for (final Content content : contents) {
			width += content.getWidth();
		}
		width += (contents.size() - 1) * spacing;
		return width;
	}

	public HorzBox setSpacing(final int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		int xOffset = x + this.x;
		final int yOffset = y + this.y;

		for (final Content content : contents) {
			renderInternal(rh, xOffset, yOffset, content);
			xOffset += content.getWidth() + spacing;
		}
	}
}
