package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.RenderInfo;

public class ImageBox implements BoxContent {

	private final TextureRegion texture;
	private int horzOffset;
	private int vertOffset;
	private boolean bottomAlign = false;

	public ImageBox(final Texture texture) {
		this.texture = new TextureRegion(texture);
	}

	protected TextureRegion getTexture() {
		return texture;
	}

	public ImageBox setHorzIndent(final int horzOffset) {
		this.horzOffset = horzOffset;
		return this;
	}

	public ImageBox setVertIndent(final int vertOffset) {
		this.vertOffset = vertOffset;
		return this;
	}

	public ImageBox setAlignBottom() {
		bottomAlign = true;
		return this;
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		float alignY;
		if (bottomAlign) {
			alignY = ri.screenHeight - (y + vertOffset) * ri.getScale();
		} else {
			alignY = ri.screenHeight - (y + vertOffset) * ri.getScale()
					- getTexture().getRegionHeight() * ri.getScale();
		}

		batch.draw(getTexture(), (x + horzOffset) * ri.getScale(), alignY,
				getTexture().getRegionWidth() * ri.getScale(),
				getTexture().getRegionHeight() * ri.getScale());
	}

}
