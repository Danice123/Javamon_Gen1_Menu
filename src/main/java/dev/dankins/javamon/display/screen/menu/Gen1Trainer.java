package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Player;

public class Gen1Trainer implements TrainerMenu {

	private Player player;

	private Content playerWindow;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final Player player) {
		this.player = player;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		assets.load(player.getImage());
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		playerWindow = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth)
				.addContent(new HorzBox(0, 0)
						.addContent(new VertBox(0, 0).addContent(new TextContent(font, "NAME/ " + player.getName()))
								.addContent(new TextContent(font, "MONEY/ $" + player.getMoney()))
								.addContent(new TextContent(font, "TIME/ 10:48")))
						.addContent(
								new ImageContent(assets.get(player.getImage())).setLeftMargin(20).setTopMargin(20)));
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> playerWindow.render(rh, 0, 0));
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (key) {
		case accept:
		case deny:
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

}
