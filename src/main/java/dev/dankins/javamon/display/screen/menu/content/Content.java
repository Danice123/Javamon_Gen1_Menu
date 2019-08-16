package dev.dankins.javamon.display.screen.menu.content;

import dev.dankins.javamon.display.screen.RenderHelper;

public abstract class Content {

	private int topMargin = 0;
	private int bottomMargin = 0;
	private int leftMargin = 0;
	private int rightMargin = 0;

	private boolean hidden = false;
	private boolean alignRight = false;
	private boolean alignBottom = false;

	public final int getWidth() {
		return leftMargin + getContentWidth() + rightMargin;
	}

	protected abstract int getContentWidth();

	public final int getHeight() {
		return topMargin + getContentHeight() + bottomMargin;
	}

	protected abstract int getContentHeight();

	public Content setTopMargin(final int topMargin) {
		this.topMargin = topMargin;
		return this;
	}

	public Content setBottomMargin(final int bottomMargin) {
		this.bottomMargin = bottomMargin;
		return this;
	}

	public Content setLeftMargin(final int leftMargin) {
		this.leftMargin = leftMargin;
		return this;
	}

	public Content setRightMargin(final int rightMargin) {
		this.rightMargin = rightMargin;
		return this;
	}

	public Content alignRight() {
		alignRight = true;
		return this;
	}

	public Content alignBottom() {
		alignBottom = true;
		return this;
	}

	public final void render(final RenderHelper rh, final int x, final int y) {
		if (!hidden) {
			final int renderx = alignRight ? x - getWidth() + rightMargin : x + leftMargin;
			final int rendery = alignBottom ? y - getHeight() + bottomMargin : y + topMargin;
			renderContent(rh, renderx, rendery);
		}
	}

	protected abstract void renderContent(final RenderHelper rh, int x, int y);

	public Content setVisibility(final boolean isHidden) {
		hidden = isHidden;
		return this;
	}

	public Content toggleVisibilty() {
		hidden = !hidden;
		return this;
	}
}
