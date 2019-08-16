package dev.dankins.javamon.display.screen.menu.content.box;

import java.util.List;

import com.google.common.collect.Lists;

import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ContentBuilder;

public class BasicBoxContent extends Content {

	protected final int x;
	protected final int y;
	protected final List<Content> contents;

	protected int topPadding = 0;
	protected int bottomPadding = 0;
	protected int leftPadding = 0;
	protected int rightPadding = 0;

	public BasicBoxContent(final int x, final int y) {
		this.x = x;
		this.y = y;
		contents = Lists.newArrayList();
	}

	@Override
	protected int getContentWidth() {
		int width = 0;
		for (final Content content : contents) {
			final int contentWidth = content.getWidth();
			if (width < contentWidth) {
				width = contentWidth;
			}
		}
		return width + leftPadding + rightPadding;
	}

	@Override
	protected int getContentHeight() {
		int height = 0;
		for (final Content content : contents) {
			final int contentHeight = content.getHeight();
			if (height < contentHeight) {
				height = contentHeight;
			}
		}
		return height + topPadding + bottomPadding;
	}

	public BasicBoxContent setTopPadding(final int topPadding) {
		this.topPadding = topPadding;
		return this;
	}

	public BasicBoxContent setBottomPadding(final int bottomPadding) {
		this.bottomPadding = bottomPadding;
		return this;
	}

	public BasicBoxContent setLeftPadding(final int leftPadding) {
		this.leftPadding = leftPadding;
		return this;
	}

	public BasicBoxContent setRightPadding(final int rightPadding) {
		this.rightPadding = rightPadding;
		return this;
	}

	public BasicBoxContent addContent(final Content content) {
		contents.add(content);
		return this;
	}

	public final BasicBoxContent addContent(final ContentBuilder builder) {
		return addContent(builder.build());
	}

	public BasicBoxContent clearContent() {
		contents.clear();
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;

		for (final Content content : contents) {
			renderInternal(rh, xOffset + leftPadding, yOffset + topPadding, content);
		}
	}

	protected void renderInternal(final RenderHelper rh, final int x, final int y,
			final Content content) {
		content.render(rh, x, y);
	}

}
