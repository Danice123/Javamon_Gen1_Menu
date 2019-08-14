package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.BorderBoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxKeyboard;
import dev.dankins.javamon.display.screen.menu.helper.BoxTextContent;
import dev.dankins.javamon.display.screen.menu.helper.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1TextInput implements TextInput {

	private String title;
	private boolean canCancel;

	private boolean cancelled;
	private String input;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final String title, final boolean canCancel) {
		this.title = title;
		this.canCancel = canCancel;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	private BoxContent titleBox;
	private BoxKeyboard keyboard;

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		if (keyboard == null) {
			keyboard = new BoxKeyboard(0, 20);
			final BorderBoxContent border = new BorderBoxContent(0, 0,
					ri.screenWidth / ri.getScale(), keyboard.getHeight());
			titleBox = new VertBox(0, 10).addContent(new BoxTextContent(title)).addContent(border);
		}
		batch.begin();
		titleBox.render(ri, batch, 0, 0);
		keyboard.render(ri, batch, 0, 0);
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {

	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (key) {
		case up:
			keyboard.up();
			break;
		case down:
			keyboard.down();
			break;
		case left:
			keyboard.left();
			break;
		case right:
			keyboard.right();
			break;
		case accept:
			if (keyboard.accept()) {
				input = keyboard.getInput();
				ThreadUtils.notifyOnObject(this);
			}
			break;
		case deny:
			if (keyboard.deny() && canCancel) {
				cancelled = true;
				ThreadUtils.notifyOnObject(this);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public String getInput() {
		return input;
	}

	@Override
	public boolean cancelled() {
		return cancelled;
	}

}
