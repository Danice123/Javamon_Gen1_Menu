package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.screen.RenderHelper;

public class ImageContent extends Content {

	private TextureRegion texture;
	private Integer centerX = null;
	private Integer centerY = null;
	private float scale = 1f;

	protected ImageContent() {
		texture = null;
	}

	public ImageContent(final Texture texture) {
		this.texture = new TextureRegion(texture);
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public ImageContent setTexture(final Texture texture) {
		this.texture = new TextureRegion(texture);
		return this;
	}

	@Override
	protected int getContentWidth() {
		return (int) (getTexture().getRegionWidth() * scale);
	}

	@Override
	protected int getContentHeight() {
		return (int) (getTexture().getRegionHeight() * scale);
	}

	public ImageContent setCenterPoint(final int x, final int y) {
		centerX = x;
		centerY = y;
		return this;
	}

	public ImageContent setScale(final float scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		if (centerX != null && centerY != null) {
			rh.sprite(getTexture(), x - centerX, rh.ri.screenHeight - y - centerY, getContentWidth(),
					getContentHeight());
		} else {
			rh.sprite(getTexture(), x - getContentWidth() / 2, rh.ri.screenHeight - y - getContentHeight() / 2,
					getContentWidth(), getContentHeight());
		}

	}

}
