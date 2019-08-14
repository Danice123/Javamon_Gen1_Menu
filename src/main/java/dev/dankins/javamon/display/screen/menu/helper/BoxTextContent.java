package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public class BoxTextContent implements BoxContent {

	private String text;
	private int horzOffset;
	private int vertOffset;

	public BoxTextContent(final String text) {
		this.text = text;
		horzOffset = 0;
		vertOffset = 0;
	}

	public BoxTextContent setHorzIndent(final int horzOffset) {
		this.horzOffset = horzOffset;
		return this;
	}

	public BoxTextContent setVertIndent(final int vertOffset) {
		this.vertOffset = vertOffset;
		return this;
	}

	String getText() {
		return text;
	}

	void setText(final String text) {
		this.text = text;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		ri.font.setColor(0f, 0f, 0f, 1f);
		ri.font.draw(batch, text, (x + horzOffset) * ri.getScale(),
				ri.screenHeight - (y + vertOffset) * ri.getScale());
	}

}
