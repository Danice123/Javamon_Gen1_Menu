package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.screen.RenderHelper;

public class ImageContent extends Content {

	private final TextureRegion texture;
	private Integer centerX = null;
	private Integer centerY = null;

	protected ImageContent() {
		texture = null;
	}

	public ImageContent(final Texture texture) {
		this.texture = new TextureRegion(texture);
	}

	protected TextureRegion getTexture() {
		return texture;
	}

	@Override
	protected int getContentWidth() {
		return getTexture().getRegionWidth();
	}

	@Override
	protected int getContentHeight() {
		return getTexture().getRegionHeight();
	}

	public ImageContent setCenterPoint(final int x, final int y) {
		centerX = x;
		centerY = y;
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		if (centerX != null && centerY != null) {
			rh.sprite(getTexture(), x - centerX, rh.ri.screenHeight - y - centerY,
					getTexture().getRegionWidth(), getTexture().getRegionHeight());
		} else {
			rh.sprite(getTexture(), x - getTexture().getRegionWidth() / 2,
					rh.ri.screenHeight - y - getTexture().getRegionHeight() / 2,
					getTexture().getRegionWidth(), getTexture().getRegionHeight());
		}

	}

}
