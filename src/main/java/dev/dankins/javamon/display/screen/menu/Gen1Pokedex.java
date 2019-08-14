package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.CollectionLibrary;
import dev.dankins.javamon.data.monster.MonsterList;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.BasicBoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxTextContent;
import dev.dankins.javamon.display.screen.menu.helper.HorzBox;
import dev.dankins.javamon.display.screen.menu.helper.ImageBox;
import dev.dankins.javamon.display.screen.menu.helper.ListBox;
import dev.dankins.javamon.display.screen.menu.helper.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Pokedex implements PokedexMenu {

	public static final String pokeball = "assets/gui/pokedex/pokeball.png";
	private Texture pokeballTex;

	private MonsterList pokemonDB;
	private CollectionLibrary pokeData;

	private boolean isSubmenuOpen = false;
	private PokedexMenuAction action;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final MonsterList pokemonDB, final CollectionLibrary pokeData) {
		this.pokemonDB = pokemonDB;
		this.pokeData = pokeData;
	}

	private ListBox pokemonList;
	private ListBox submenu;

	private BoxContent dataPanel;
	private BoxContent title;

	@Override
	public void init(final AssetManager assets) {
		// TODO: pokeballTex = assets.get(pokeball);
		pokeballTex = new Texture(pokeball);

		title = new BasicBoxContent(13, 6).addContent(new BoxTextContent("Contents"));

		pokemonList = new ListBox(1, 27).setListSize(7);

		for (int i = 1; i < pokemonDB.getTotalMonsters(); i++) {

			final BoxTextContent number = new BoxTextContent(getPokemonNumber(i)).setVertIndent(-8);
			final BasicBoxContent numberBox = new BasicBoxContent(0, 0).addContent(number);
			if (pokeData.isCaught(i)) {
				numberBox.addContent(new ImageBox(pokeballTex).setHorzIndent(16));
			}

			final BoxTextContent name = new BoxTextContent(getPokemonName(i));
			final BasicBoxContent horzBox = new HorzBox(0, 0).setSpacing(24).addContent(numberBox)
					.addContent(name);
			pokemonList.addContent(horzBox);
		}

		dataPanel = new VertBox(-36, 23).setSpacing(20)
				.addContent(new VertBox(0, 0).setSpacing(8).addContent(new BoxTextContent("Seen"))
						.addContent(new BoxTextContent(Integer.toString(pokeData.amountSeen()))
								.setHorzIndent(16)))
				.addContent(
						new VertBox(0, 0).setSpacing(8).addContent(new BoxTextContent("Own"))
								.addContent(new BoxTextContent(
										Integer.toString(pokeData.amountCaught()))
												.setHorzIndent(16)));

		submenu = new ListBox(-43, 91).setArrowIndent(10).addLine("Data").addLine("Cry")
				.addLine("Area").addLine("Quit");
		submenu.toggleArrowHidden();
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		shape.begin(ShapeType.Filled);
		shape.setColor(.2f, .2f, .2f, 0f);
		shape.rect(0, 0, ri.screenWidth, ri.screenHeight);
		shape.setColor(1f, 1f, 1f, 0f);
		shape.rect(1 * ri.getScale(), 1 * ri.getScale(), ri.screenWidth - 2 * ri.getScale(),
				ri.screenHeight - 2 * ri.getScale());
		shape.setColor(0f, 0f, 0f, 0f);
		shape.rect(ri.screenWidth - 45 * ri.getScale(), 0, 2 * ri.getScale(), ri.screenWidth);

		final int xoff = ri.screenWidth - 45 * ri.getScale();
		final int yoff = 11;
		for (int i = 0; i < 8; i++) {
			shape.setColor(0f, 0f, 0f, 0f);
			shape.rect(xoff - 2 * ri.getScale(), (yoff + i * 17) * ri.getScale(), 6 * ri.getScale(),
					6 * ri.getScale());
			shape.setColor(.5f, .5f, .5f, 0f);
			shape.rect(xoff - 1 * ri.getScale(), (yoff + i * 17 + 1) * ri.getScale(),
					4 * ri.getScale(), 4 * ri.getScale());
			shape.setColor(1f, 1f, 1f, 0f);
			shape.rect(xoff, (yoff + i * 17 + 1) * ri.getScale(), 2 * ri.getScale(),
					4 * ri.getScale());
		}

		shape.setColor(0f, 0f, 0f, 0f);
		shape.rect(xoff + 4 * ri.getScale(), 83 * ri.getScale(), ri.screenWidth, 1 * ri.getScale());
		shape.rect(xoff + 4 * ri.getScale(), 80 * ri.getScale(), ri.screenWidth, 2 * ri.getScale());
		shape.end();

		batch.begin();
		pokemonList.render(ri, batch, 0, 0);
		submenu.render(ri, batch, ri.screenWidth / ri.getScale(), 0);
		dataPanel.render(ri, batch, ri.screenWidth / ri.getScale(), 0);
		title.render(ri, batch, 0, 0);
		batch.end();
	}

	private String getPokemonName(final int i) {
		if (pokeData.isCaught(i) || pokeData.isSeen(i)) {
			return pokemonDB.getMonster(i).getName();
		}
		return "-------------------";
	}

	private String getPokemonNumber(final int i) {
		if (pokemonDB.getMonster(i) == null) {
			return "???";
		}
		return pokemonDB.getMonster(i).getFormattedNumber();
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
