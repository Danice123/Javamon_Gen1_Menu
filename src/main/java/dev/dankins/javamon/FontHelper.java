package dev.dankins.javamon;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontHelper {

	public final BitmapFont font;
	public final int scale;

	public FontHelper(final FreeTypeFontGenerator fontGen, final int size, final int scale) {
		this.scale = scale;

		final FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = size * scale;
		param.kerning = true;
		font = fontGen.generateFont(param);
	}
}
