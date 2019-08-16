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
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.logic.Key;

public class Gen1PC implements PCMenu {

	private boolean knowsStorageGuy = false;
	private String playerName = "";

	private PCMenuOptions action;

	private Content window;
	private ListBox menu;

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
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, 0, 0).addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			if (knowsStorageGuy) {
				menu.addContent(new TextContent(font, "Bill's PC"));
			} else {
				menu.addContent(new TextContent(font, "Someone's PC"));
			}
			menu.addContent(new TextContent(font, playerName + "'s PC"))
					.addContent(new TextContent(font, "Prof. Oak's PC"))
					.addContent(new TextContent(font, "Log Off"));
			return menu;
		}).setLeftPadding(6);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> window.render(rh, 0, 0));
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (key) {
		case up:
			menu.decrement();
			break;
		case down:
			menu.increment();
			break;
		case accept:
			switch (menu.getIndex()) {
			case 0:
				action = PCMenuOptions.Pokemon;
				ThreadUtils.notifyOnObject(this);
				break;
			case 1:
				action = PCMenuOptions.Item;
				ThreadUtils.notifyOnObject(this);
				break;
			case 2:
				break;
			case 3:
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
