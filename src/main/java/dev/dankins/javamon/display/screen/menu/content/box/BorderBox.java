package dev.dankins.javamon.display.screen.menu.content.box;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.screen.RenderHelper;

public class BorderBox extends BasicBoxContent {

	static public final AssetDescriptor<Texture> RESOURCE = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/border.png", Texture.class);

	private final TextureRegion TL;
	private final TextureRegion TR;
	private final TextureRegion BL;
	private final TextureRegion BR;
	private final TextureRegion L;
	private final TextureRegion R;
	private final TextureRegion T;
	private final TextureRegion B;
	public final TextureRegion FILL;

	private int minWidth = 0;
	private int minHeight = 0;

	public BorderBox(final AssetManager assets, final int x, final int y) {
		super(x, y);
		final Texture tex = assets.get(RESOURCE);
		TL = new TextureRegion(tex, 0, 0, 8, 8);
		TR = new TextureRegion(tex, 0, 8, 8, 8);
		BL = new TextureRegion(tex, 0, 16, 8, 8);
		BR = new TextureRegion(tex, 0, 24, 8, 8);
		T = new TextureRegion(tex, 0, 32, 8, 8);
		B = new TextureRegion(tex, 0, 40, 8, 8);
		L = new TextureRegion(tex, 0, 48, 8, 8);
		R = new TextureRegion(tex, 0, 56, 8, 8);
		FILL = new TextureRegion(tex, 0, 64, 8, 8);

		setTopPadding(14);
		setBottomPadding(14);
		setLeftPadding(11);
		setRightPadding(11);
	}

	@Override
	protected int getContentWidth() {
		final int width = super.getContentWidth();
		if (width < minWidth) {
			return minWidth;
		}
		return width;
	}

	@Override
	protected int getContentHeight() {
		final int height = super.getContentHeight();
		if (height < minHeight) {
			return minHeight;
		}
		return height;
	}

	public BorderBox setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	public BorderBox setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
		return this;
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;
		final int width = getWidth();
		final int height = getHeight();

		drawBox(rh, xOffset, rh.ri.screenHeight - yOffset - height, width, height);

		super.renderContent(rh, x, y);
	}

	private void drawBox(final RenderHelper rh, final int x, final int y, final int width,
			final int height) {
		final int WIDTH = 8;
		final int HEIGHT = 8;

		rh.sprite(TL, x, y + height - HEIGHT, WIDTH, HEIGHT);
		rh.sprite(TR, x + width - WIDTH, y + height - HEIGHT, WIDTH, HEIGHT);
		rh.sprite(BL, x, y, WIDTH, HEIGHT);
		rh.sprite(BR, x + width - WIDTH, y, WIDTH, HEIGHT);

		rh.sprite(L, x, y + HEIGHT, WIDTH, height - HEIGHT * 2);
		rh.sprite(R, x + width - WIDTH, y + HEIGHT, WIDTH, height - HEIGHT * 2);
		rh.sprite(T, x + WIDTH, y + height - HEIGHT, width - WIDTH * 2, HEIGHT);
		rh.sprite(B, x + WIDTH, y, width - WIDTH * 2, HEIGHT);

		rh.sprite(FILL, x + WIDTH, y + HEIGHT, width - WIDTH * 2, height - HEIGHT * 2);
	}

}
