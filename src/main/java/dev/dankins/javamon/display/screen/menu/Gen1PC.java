package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.logic.Key;

public class Gen1PC implements PCMenu {

	private boolean knowsStorageGuy = false;
	private String playerName = "";

	private PCMenuOptions action;
	private int index = 0;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final boolean knowsStorageGuy, final String playerName) {
		this.knowsStorageGuy = knowsStorageGuy;
		this.playerName = playerName;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		final int width = 120 * ri.getScale();
		final int height = 70 * ri.getScale();
		final int side = 0;
		final int top = ri.screenHeight;
		batch.begin();
		ri.border.drawBox(batch, side, top - height, width, height);

		if (knowsStorageGuy) {
			ri.font.draw(batch, "Bill's PC", side + 18 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
		} else {
			ri.font.draw(batch, "Someone's PC", side + 18 * ri.getScale(),
					top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
		}
		ri.font.draw(batch, playerName + "'s PC", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 1);
		ri.font.draw(batch, "Log Off", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 2);

		batch.draw(ri.arrow.rightArrow, side + 6 * ri.getScale(),
				top - 20 * ri.getScale() - 18 * ri.getScale() * index,
				ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
				ri.arrow.rightArrow.getRegionHeight() * ri.getScale());

		batch.end();
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
			if (index < 2) {
				index++;
			}
			break;
		case accept:
			switch (index) {
			case 0:
				action = PCMenuOptions.Pokemon;
				ThreadUtils.notifyOnObject(this);
				break;
			case 1:
				action = PCMenuOptions.Item;
				ThreadUtils.notifyOnObject(this);
				break;
			case 2:
				action = PCMenuOptions.Exit;
				ThreadUtils.notifyOnObject(this);
				break;
			}
			break;
		case deny:
			action = PCMenuOptions.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	@Override
	public PCMenuOptions getMenuChoice() {
		return action;
	}

}
