package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.CollectionLibrary;
import dev.dankins.javamon.data.monster.MonsterList;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BasicBoxContent;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Pokedex implements PokedexMenu {

	static public final AssetDescriptor<Texture> RESOURCE = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/pokeball.png", Texture.class);

	private MonsterList pokemonDB;
	private CollectionLibrary pokeData;

	private boolean isSubmenuOpen = false;
	private PokedexMenuAction action;

	private Content title;
	private ListBox pokemonList;
	private Content dataPanel;
	private ListBox submenu;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final MonsterList pokemonDB, final CollectionLibrary pokeData) {
		this.pokemonDB = pokemonDB;
		this.pokeData = pokeData;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		assets.load(RESOURCE);
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		title = new BasicBoxContent(13, 6).addContent(new TextContent(font, "Contents"));
		pokemonList = new ListBox(assets, 1, 23).setListSize(7);

		for (int i = 1; i < pokemonDB.getTotalMonsters(); i++) {
			final int pokemonIterator = i;

			pokemonList.setSpacing(2)
					.addContent(new HorzBox(0, 0)
							.addContent(new TextContent(font, getPokemonNumber(pokemonIterator)))
							.addContent(() -> {
								final HorzBox entry = new HorzBox(0, 0).setSpacing(1);
								entry.setTopMargin(8);
								if (pokeData.isCaught(pokemonIterator)) {
									entry.addContent(new ImageContent(assets.get(RESOURCE))
											.setTopMargin(4).setLeftMargin(-8).setBottomMargin(-4));
								}
								entry.addContent(
										new TextContent(font, getPokemonName(pokemonIterator)));
								return entry;
							}));
		}

		dataPanel = new VertBox(ri.screenWidth - 36, 23)
				.addContent(
						new VertBox(0, 0).setSpacing(1).addContent(new TextContent(font, "Seen"))
								.addContent(new TextContent(font,
										Integer.toString(pokeData.amountSeen())).setLeftMargin(18)))
				.addContent(new VertBox(0, 0).setSpacing(1).addContent(new TextContent(font, "Own"))
						.addContent(new TextContent(font, Integer.toString(pokeData.amountCaught()))
								.setLeftMargin(18)));

		submenu = new ListBox(assets, ri.screenWidth - 43, 91);
		submenu.setArrowIndent(10).toggleArrowHidden().addContent(new TextContent(font, "Data"))
				.addContent(new TextContent(font, "Cry")).addContent(new TextContent(font, "Area"))
				.addContent(new TextContent(font, "Quit"));
	}

	private String getPokemonNumber(final int i) {
		if (pokemonDB.getMonster(i) == null) {
			return "???";
		}
		return pokemonDB.getMonster(i).getFormattedNumber();
	}

	private String getPokemonName(final int i) {
		if (pokeData.isCaught(i) || pokeData.isSeen(i)) {
			return pokemonDB.getMonster(i).getName();
		}
		return "-------------------";
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withShapeRenderer((shape) -> {
			shape.filled((helper) -> {
				final Color backgroundColor = new Color(.2f, .2f, .2f, 0f);
				final Color black = new Color(0f, 0f, 0f, 0f);
				final Color grey = new Color(.5f, .5f, .5f, 0f);
				final Color white = new Color(1f, 1f, 1f, 0f);

				helper.rect(backgroundColor, 0, 0, rh.ri.screenWidth, rh.ri.screenHeight);
				helper.rect(white, 1, 1, rh.ri.screenWidth - 2, rh.ri.screenHeight - 2);
				helper.rect(black, rh.ri.screenWidth - 45, 0, 2, rh.ri.screenWidth);

				final int xoff = rh.ri.screenWidth - 45;
				final int yoff = 11;
				for (int i = 0; i < 8; i++) {
					helper.rect(black, xoff - 2, yoff + i * 17, 6, 6);
					helper.rect(grey, xoff - 1, yoff + i * 17 + 1, 4, 4);
					helper.rect(white, xoff, yoff + i * 17 + 1, 2, 4);
				}
				helper.rect(black, xoff + 4, 83, rh.ri.screenWidth, 1);
				helper.rect(black, xoff + 4, 80, rh.ri.screenWidth, 2);
			});
		});

		rh.withSpriteBatch((batch) -> {
			title.render(rh, 0, 0);
			pokemonList.render(rh, 0, 0);
			dataPanel.render(rh, 0, 0);
			submenu.render(rh, 0, 0);
		});
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isSubmenuOpen) {
			handleSubmenuKey(key);
			return;
		}
		switch (key) {
		case up:
			pokemonList.decrement();
			break;
		case down:
			pokemonList.increment();
			break;
		case accept:
			if (pokeData.isSeen(pokemonList.getIndex() + 1)) {
				isSubmenuOpen = true;
				pokemonList.toggleArrow();
				submenu.toggleArrowHidden();
			}
			break;
		case deny:
			action = PokedexMenuAction.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	private void handleSubmenuKey(final Key key) {
		switch (key) {
		case up:
			submenu.decrement();
			break;
		case down:
			submenu.increment();
			break;
		case accept:
			switch (submenu.getIndex()) {
			case 0: // View
				action = PokedexMenuAction.View;
				ThreadUtils.notifyOnObject(this);
				break;
			case 1: // Cry
				action = PokedexMenuAction.Cry;
				ThreadUtils.notifyOnObject(this);
				break;
			case 2: // Area
				action = PokedexMenuAction.Area;
				ThreadUtils.notifyOnObject(this);
				break;
			case 3: // Cancel
				action = PokedexMenuAction.Exit;
				ThreadUtils.notifyOnObject(this);
				break;
			}
			isSubmenuOpen = false;
			pokemonList.toggleArrow();
			submenu.toggleArrowHidden();
			break;
		case deny:
			isSubmenuOpen = false;
			pokemonList.toggleArrow();
			submenu.toggleArrowHidden();
			break;
		default:
			break;
		}
	}

	@Override
	public PokedexMenuAction getMenuAction() {
		return action;
	}

	@Override
	public int getPokemonChoice() {
		return pokemonList.getIndex() + 1;
	}
}
