package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.logic.Key;

public class Gen1Chatbox implements Chatbox {

	private String[] text;
	private int index = 0;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final String text) {
		index = 0;
		if (text.contains("/n")) {
			this.text = text.split("/n");
		} else {
			this.text = new String[] { text };
		}
	}

	public boolean isFinished() {
		return !(index < text.length - 1);
	}

	@Override
	public void init(final AssetManager assets) {
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		batch.begin();
		renderChatbox(batch, ri);
		batch.end();
	}

	public void renderChatbox(final SpriteBatch batch, final RenderInfo ri) {
		ri.border.drawBox(batch, 0, 0, ri.screenWidth, 50 * ri.getScale());
		if (text != null) {
			ri.font.draw(batch, text[index], ri.border.WIDTH + 2,
					50 * ri.getScale() - ri.border.HEIGHT,
					ri.screenWidth - 2 * (ri.border.WIDTH + 2), Align.left, true);
		}
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (key == Key.accept || key == Key.deny) {
			if (isFinished()) {
				ThreadUtils.notifyOnObject(this);
			} else {
				index++;
			}
		}
	}

}
