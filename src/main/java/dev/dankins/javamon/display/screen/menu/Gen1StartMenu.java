package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Player;

public class Gen1StartMenu implements StartMenu {

	private boolean hasPokemon;
	private boolean hasPokedex;
	private StartMenuOptions startMenuOption;

	private BorderBox window;
	private ListBox menu;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final Player player) {
		hasPokedex = player.getStrings().containsKey("Has_Pokedex");
		hasPokemon = player.getParty().size() > 0;
	}

	@Override
	public StartMenuOptions getMenuChoice() {
		return startMenuOption;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, ri.screenWidth, 0);
		window.addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			if (hasPokedex) {
				menu.addContent(new TextContent(font, "PokeDex"));
			}
			if (hasPokemon) {
				menu.addContent(new TextContent(font, "Pokemon"));
			}
			menu.addContent(new TextContent(font, "Bag"))
					.addContent(new TextContent(font, "Trainer"))
					.addContent(new TextContent(font, "Save"))
					.addContent(new TextContent(font, "Options"))
					.addContent(new TextContent(font, "Exit"));
			return menu;
		}).setLeftMargin(6).alignRight();
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
			int index = menu.getIndex();
			if (!hasPokedex) {
				index++;
			}
			if (!hasPokemon) {
				index++;
			}

			switch (index) {
			case 0:
				startMenuOption = StartMenuOptions.Pokedex;
				ThreadUtils.notifyOnObject(this);
				break;
			case 1:
				if (hasPokedex && !hasPokemon) {
					startMenuOption = StartMenuOptions.Pokedex;
				} else {
					startMenuOption = StartMenuOptions.Pokemon;
				}
				ThreadUtils.notifyOnObject(this);
				break;
			case 2:
				startMenuOption = StartMenuOptions.Bag;
				ThreadUtils.notifyOnObject(this);
				break;
			case 3:
				startMenuOption = StartMenuOptions.Trainer;
				ThreadUtils.notifyOnObject(this);
				break;
			case 4:
				startMenuOption = StartMenuOptions.Save;
				ThreadUtils.notifyOnObject(this);
				break;
			case 5:
				startMenuOption = StartMenuOptions.Options;
				ThreadUtils.notifyOnObject(this);
				break;
			case 6:
				startMenuOption = StartMenuOptions.Exit;
				ThreadUtils.notifyOnObject(this);
				break;
			}
			break;
		case deny:
			startMenuOption = StartMenuOptions.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}
}
