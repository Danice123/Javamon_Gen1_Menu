package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.Timer;
import dev.dankins.javamon.display.screen.RenderHelper;

public class TextContent extends Content {

	private final FontHelper font;
	private String text;
	private final Color textColor;

	private final BitmapFontCache cache;
	private final GlyphLayout layout;

	private Integer wrappingWidth = null;
	private boolean progressive = false;
	private int charactersToShow = 1;

	public TextContent(final FontHelper font, final String text) {
		this(font, text, Color.BLACK);
	}

	public TextContent(final FontHelper font, final String text, final Color textColor) {
		this.font = font;
		this.text = text;
		this.textColor = textColor;

		cache = new BitmapFontCache(font.font);
		layout = new GlyphLayout(font.font, text, textColor, 0, Align.left, false);
		cache.setText(layout, 0, 0);
	}

	@Override
	protected int getContentWidth() {
		return (int) (layout.width / font.scale);
	}

	@Override
	protected int getContentHeight() {
		return (int) (layout.height / font.scale);
	}

	public String getText() {
		return text;
	}

	public synchronized void setText(final String text) {
		this.text = text;
		charactersToShow = 1;
		if (wrappingWidth != null) {
			layout.setText(font.font, text, textColor, wrappingWidth * font.scale, Align.left, true);
			cache.setText(layout, 0, 0);
		} else {
			layout.setText(font.font, text, textColor, 0, Align.left, false);
			cache.setText(layout, 0, 0);
		}
	}

	public TextContent setWrappingWidth(final int wrappingWidth) {
		this.wrappingWidth = wrappingWidth;
		layout.setText(font.font, text, textColor, wrappingWidth * font.scale, Align.left, true);
		cache.setText(layout, 0, 0);
		return this;
	}

	public TextContent setIsProgressive() {
		progressive = true;
		return this;
	}

	public boolean isFinished() {
		if (progressive) {
			return charactersToShow >= text.length();
		} else {
			return true;
		}
	}

	public synchronized void finishText() {
		charactersToShow = text.length();
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		cache.setPosition(x * font.scale, (rh.ri.screenHeight - y) * font.scale);

		if (progressive && charactersToShow < text.length()) {
			rh.text(cache, 0, charactersToShow);
		} else {
			rh.text(cache);
		}
	}

	private final Timer charTimer = new Timer(0.05f) {
		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (charactersToShow < text.length()) {
				charactersToShow++;
			}
			reset();
		}
	};

	public synchronized void tickSelf(final float delta) {
		charTimer.tick(delta);
	}

	@Override
	public void update() {
	}

}
