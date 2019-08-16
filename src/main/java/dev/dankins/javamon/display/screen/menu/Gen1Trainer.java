package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

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
	private Texture playerImage;
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
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		playerImage = player.getImage(assets);

		playerWindow = new BorderBox(assets,
				0, 0)
						.setMinWidth(
								ri.screenWidth)
						.addContent(
								new HorzBox(
										45, 0)
												.addContent(
														new VertBox(0, 0)
																.addContent(new TextContent(font,
																		"NAME/ " + player
																				.getName()))
																.addContent(new TextContent(font,
																		"MONEY/ $"
																				+ player.getMoney()))
																.addContent(new TextContent(font,
																		"TIME/ 10:48"))
																.setTopMargin(10))
												.addContent(new ImageContent(playerImage)));
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> playerWindow.render(rh, 0, 0));

		// batch.begin();
		//
		// final int playerBoxTop = ri.screenHeight - 20 * ri.getScale();
		// final int playerBoxSide = 20 * ri.getScale();
		//
		// ri.border.drawBox(batch, 0, ri.screenHeight - 80 * ri.getScale(),
		// ri.screenWidth,
		// 80 * ri.getScale());
		//
		// ri.font.draw(batch, "NAME/ " + player.getName(), playerBoxSide,
		// playerBoxTop);
		// ri.font.draw(batch, "MONEY/ $" + player.getMoney(), playerBoxSide,
		// playerBoxTop - 18 * 1 * ri.getScale());
		// ri.font.draw(batch, "TIME/ 10:48", playerBoxSide, playerBoxTop - 18 *
		// 2 * ri.getScale());
		//
		// batch.draw(playerImage, playerBoxSide + 140 * ri.getScale(),
		// playerBoxTop - 50 * ri.getScale(), playerImage.getWidth() *
		// ri.getScale(),
		// playerImage.getHeight() * ri.getScale(), 0, 0,
		// playerImage.getWidth(),
		// playerImage.getHeight(), false, false);
		//
		// batch.end();
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
