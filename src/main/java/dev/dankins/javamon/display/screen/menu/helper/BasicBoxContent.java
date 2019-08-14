package dev.dankins.javamon.display.screen.menu.helper;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.common.collect.Lists;

import dev.dankins.javamon.display.RenderInfo;

public class BasicBoxContent implements BoxContent {

	protected final int x;
	protected final int y;
	protected final List<BoxContent> contents;

	public BasicBoxContent(final int x, final int y) {
		this.x = x;
		this.y = y;
		contents = Lists.newArrayList();
	}

	public BasicBoxContent addContent(final BoxContent content) {
		contents.add(content);
		return this;
	}

	public BasicBoxContent addTextContent(final String text) {
		contents.add(new BoxTextContent(text));
		return this;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;

		for (final BoxContent content : contents) {
			renderContent(ri, batch, xOffset, yOffset, content);
		}
	}

	protected void renderContent(final RenderInfo ri, final SpriteBatch batch, final int x,
			final int y, final BoxContent content) {
		content.render(ri, batch, x, y);
	}

}
