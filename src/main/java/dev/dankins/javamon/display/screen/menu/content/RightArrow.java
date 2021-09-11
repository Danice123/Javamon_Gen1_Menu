package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.screen.RenderHelper;

public class RightArrow extends Content {

	static public final AssetDescriptor<Texture> RESOURCE = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/arrow.png", Texture.class);

	private final TextureRegion rightArrow;
	private final TextureRegion rightArrowAlt;

	private boolean selected = false;

	public RightArrow(final AssetManager assets) {
		final Texture arrow = assets.get(RESOURCE);
		rightArrow = new TextureRegion(arrow, 0, 0, 8, 8);
		rightArrowAlt = new TextureRegion(arrow, 8, 0, 8, 8);
	}

	@Override
	protected int getContentWidth() {
		return 8;
	}

	@Override
	protected int getContentHeight() {
		return 8;
	}

	public void setSelected(final boolean isSelected) {
		selected = isSelected;
	}

	public void toggleSelected() {
		selected = !selected;
	}

	@Override
	protected void renderContent(final RenderHelper rh, final int x, final int y) {
		if (selected) {
			rh.sprite(rightArrowAlt, x, rh.ri.screenHeight - y, rightArrowAlt.getRegionWidth(),
					rightArrowAlt.getRegionHeight());
		} else {
			rh.sprite(rightArrow, x, rh.ri.screenHeight - y, rightArrow.getRegionWidth(), rightArrow.getRegionHeight());
		}
	}

	@Override
	public void update() {
	}

}
