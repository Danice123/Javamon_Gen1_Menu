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
import dev.dankins.javamon.display.screen.menu.helper.TextRender;
import dev.dankins.javamon.logic.Key;

public class Gen1ItemStorage implements ItemStorageMenu {

	private ItemStorageMenuOptions action;

	private Content window;
	private ListBox menu;
	private Content textWindow;
	private TextRender textRender;

	public Gen1ItemStorage() {
	}

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, 0, 0).addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			menu.addContent(new TextContent(font, "Withdraw Item"))
					.addContent(new TextContent(font, "Deposit Item"))
					.addContent(new TextContent(font, "Toss Item"))
					.addContent(new TextContent(font, "Log Off"));
			return menu;
		}).setLeftPadding(6);

		textWindow = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).setMinHeight(50)
				.addContent(() -> {
					final TextContent c = new TextContent(font, "What do you want to do?")
							.setWrappingWidth(ri.screenWidth - 20);
					textRender = (s) -> c.setText(s);
					return c;
				}).setLeftPadding(8).setTopPadding(10);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> {
			window.render(rh, 0, 0);
			textWindow.render(rh, 0, rh.ri.screenHeight - textWindow.getHeight());
		});
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
			case 0: // Withdraw
				textRender.render("What do you want to withdraw?");
				action = ItemStorageMenuOptions.Take;
				ThreadUtils.notifyOnObject(this);
				break;
			case 1: // Deposit
				textRender.render("What do you want to deposit?");
				action = ItemStorageMenuOptions.Store;
				ThreadUtils.notifyOnObject(this);
				break;
			case 2: // Toss
				textRender.render("What do you want to toss away?");
				action = ItemStorageMenuOptions.Toss;
				ThreadUtils.notifyOnObject(this);
				break;
			case 3: // Log off
				action = ItemStorageMenuOptions.Exit;
				ThreadUtils.notifyOnObject(this);
				break;
			}
			break;
		case deny:
			action = ItemStorageMenuOptions.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	@Override
	public ItemStorageMenuOptions getMenuChoice() {
		return action;
	}
}
