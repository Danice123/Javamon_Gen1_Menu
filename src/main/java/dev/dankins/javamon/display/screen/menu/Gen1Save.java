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
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Player;

public class Gen1Save implements SaveMenu {

	private Player player;

	private Content window;
	private Content textBox;
	private Content choiceBox;
	private ListBox menu;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final Player player) {
		this.player = player;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, ri.screenWidth, 0)
				.addContent(new VertBox(0, 0)
						.addContent(new TextContent(font, "Player: " + player.getName()))
						.addContent(new HorzBox(0, 0).addContent(new TextContent(font, "Badges:"))
								.addContent(new TextContent(font, "0")))
						.addContent(new HorzBox(0, 0).addContent(new TextContent(font, "Pokedex:"))
								.addContent(new TextContent(font,
										zeroBuffer(player.getPokeData().amountCaught()))))
						.addContent(new HorzBox(0, 0).addContent(new TextContent(font, "Time:"))
								.addContent(new TextContent(font, "20:20"))))
				.alignRight();

		textBox = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).setMinHeight(50)
				.addContent(new TextContent(font, "Would you like to SAVE the game?")
						.setWrappingWidth(ri.screenWidth - 20))
				.setLeftPadding(8).setTopPadding(10);

		choiceBox = new BorderBox(assets, 0, 0).addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			menu.addContent(new TextContent(font, "Yes")).addContent(new TextContent(font, "No"));
			return menu;
		}).setLeftPadding(6);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> {
			window.render(rh, 0, 0);
			textBox.render(rh, 0, rh.ri.screenHeight - textBox.getHeight());
			choiceBox.render(rh, 0,
					rh.ri.screenHeight - textBox.getHeight() - choiceBox.getHeight());
		});
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
		case deny:
			menu.setIndex(1);
		case accept:
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean shouldSave() {
		return menu.getIndex() == 0;
	}

}
