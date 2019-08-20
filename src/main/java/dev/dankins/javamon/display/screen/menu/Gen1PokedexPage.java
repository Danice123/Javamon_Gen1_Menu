package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.Monster;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BasicBoxContent;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1PokedexPage implements PokedexPageMenu {

	private Monster pokemon;
	private boolean isCaught;
	
	private Content image;
	private Content number;
	private Content stats;
	private Content description;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final Monster pokemon, final boolean caught) {
		this.pokemon = pokemon;
		isCaught = caught;
	}

	

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		assets.load("pokemon/" + pokemon.getNumber() + ".png", Texture.class);
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		
		image = new ImageContent(assets.get("pokemon/" + pokemon.getNumber() + ".png", Texture.class)).alignBottom();
		number = new TextContent(font, "No." + pokemon.getFormattedNumber());
		stats = new VertBox(100, 15)
				.addContent(new TextContent(font, pokemon.getName()))
				.addContent(new TextContent(font, pokemon.getSpecies()))
				.addContent(() -> {
					BasicBoxContent box = new HorzBox(0, 0).setSpacing(48).addContent(new TextContent(font, "HT"));
					if (isCaught) {
						box.addContent(new TextContent(font, Integer.toString(pokemon.getHeight()) + "m"));
					} else {
						box.addContent(new TextContent(font, "???m"));
					}
					return box;
				})
				.addContent(() -> {
					BasicBoxContent box = new HorzBox(0, 0).setSpacing(48).addContent(new TextContent(font, "WT"));
					
					if (isCaught) {
						box.addContent(new TextContent(font, Integer.toString(pokemon.getWeight()) + "lb"));
					} else {
						box.addContent(new TextContent(font, "???lb"));
					}
					return box;
				});
		description = new TextContent(font, pokemon.getDescription().replace('\n', ' ')).setWrappingWidth(220);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		// Fill background
		
		rh.withShapeRenderer((shape) -> {
			shape.filled((helper) -> {
				Color black = new Color(0f, 0f, 0f, 0f);
				Color grey = new Color(.5f, .5f, .5f, 0f);
				Color white = new Color(1f, 1f, 1f, 0f);
				
				helper.rect(grey, 0, 0, rh.ri.screenWidth, rh.ri.screenHeight);
				helper.rect(black, 5, 5, rh.ri.screenWidth - 10, rh.ri.screenHeight - 10);
				helper.rect(white, 6, 6, rh.ri.screenWidth - 12, rh.ri.screenHeight - 12);
				
				helper.rect(black, 6, 69, rh.ri.screenWidth - 12, 2);
				
				final int xoff = 11;
				final int yoff = 69;
				for (int i = 0; i < 13; i++) {
					if (i == 5 || i == 6) {
						continue;
					}
					helper.rect(black, xoff + i * 17, yoff - 2, 6, 6);
					helper.rect(grey, xoff + i * 17 + 1, yoff - 1, 4, 4);
					helper.rect(white, xoff + i * 17 + 1, yoff, 4, 2);
				}
			});
		});
		
		rh.withSpriteBatch((batch) -> {
			image.render(rh, 50, 90);
			number.render(rh, 25, 75);
			stats.render(rh, 0, 0);
			if (isCaught) {
				description.render(rh, 10, rh.ri.screenHeight - 60);
			}
		});
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (key == Key.deny || key == Key.accept) {
			ThreadUtils.notifyOnObject(this);
		}
	}

}
