package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Player;

public class Gen1Save implements SaveMenu {

	private Player player;

	private int index = 0;
	private boolean isSave = false;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final Player player) {
		this.player = player;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		final int width = 140 * ri.getScale();
		final int height = 90 * ri.getScale();
		final int side = ri.screenWidth - width;
		final int top = ri.screenHeight;
		batch.begin();
		ri.border.drawBox(batch, side, top - height, width, height);

		if (player != null) {
			ri.font.draw(batch, "Player: " + player.getName(), side + 9 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
			ri.font.draw(batch, "Badges:", side + 9 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 1);
			ri.font.draw(batch, "0", side + width - 14 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 1);
			ri.font.draw(batch, "Pokedex:", side + 9 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 2);
			ri.font.draw(batch, zeroBuffer(player.getPokeData().amountCaught()),
					side + width - 30 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 2);
			ri.font.draw(batch, "Time:", side + 9 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 3);
			ri.font.draw(batch, "20:20", side + width - 46 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 3);
		}

		renderBox(ri, batch);
		renderText(ri, batch);

		batch.end();
	}

	private String zeroBuffer(final int amount) {
		if (amount < 10) {
			return "00" + Integer.toString(amount);
		}
		if (amount < 100) {
			return "0" + Integer.toString(amount);
		}
		return Integer.toString(amount);
	}

	private void renderBox(final RenderInfo ri, final SpriteBatch batch) {
		final int width = 60 * ri.getScale();
		final int height = 50 * ri.getScale();
		final int side = 0;
		final int top = ri.screenHeight - 60 * ri.getScale();

		ri.border.drawBox(batch, side, top - height, width, height);

		ri.font.draw(batch, "Yes", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
		ri.font.draw(batch, "No", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 1);

		batch.draw(ri.arrow.rightArrow, side + 6 * ri.getScale(),
				top - 20 * ri.getScale() - 18 * ri.getScale() * index,
				ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
				ri.arrow.rightArrow.getRegionHeight() * ri.getScale());
	}

	private void renderText(final RenderInfo ri, final SpriteBatch batch) {
		final int width = ri.screenWidth;
		final int height = 50 * ri.getScale();
		final int side = 0;
		final int top = 50 * ri.getScale();

		ri.border.drawBox(batch, side, top - height, width, height);

		ri.font.draw(batch, "Would you like to SAVE", side + 9 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
		ri.font.draw(batch, "the game?", side + 9 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 1);
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (key) {
		case up:
			if (index > 0) {
				index--;
			}
			break;
		case down:
			if (index < 1) {
				index++;
			}
			break;
		case accept:
			if (index == 0) {
				isSave = true;
			}
		case deny:
			ThreadUtils.notifyOnObject(this);
		default:
			break;
		}
	}

	@Override
	public boolean shouldSave() {
		return isSave;
	}

}
