package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.BoxKeyboard;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1TextInput implements TextInput {

	private String title;
	private boolean canCancel;

	private Content titleBox;
	private BoxKeyboard keyboard;

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
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		titleBox = new VertBox(0, 10).addContent(new TextContent(font, title)).addContent(
				new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).addContent(() -> {
					keyboard = new BoxKeyboard(assets, ri, 0, 0);
					return keyboard;
				}).setLeftPadding(6));

	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> titleBox.render(rh, 0, 0));
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
