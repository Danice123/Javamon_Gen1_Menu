package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.BorderBoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxContent;
import dev.dankins.javamon.display.screen.menu.helper.ListBox;
import dev.dankins.javamon.logic.Key;

public class Gen1StartMenu implements StartMenu {

	private boolean hasPokemon;
	private boolean hasPokedex;
	private StartMenuOptions startMenuOption;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final boolean hasPokemon, final boolean hasPokedex) {
		this.hasPokedex = hasPokedex;
		this.hasPokemon = hasPokemon;
	}

	@Override
	public StartMenuOptions getMenuChoice() {
		return startMenuOption;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	private BoxContent window;
	private ListBox menu;

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		if (menu == null) {
			menu = new ListBox(0, 0);
			if (hasPokedex) {
				menu.addLine("PokeDex");
			}
			if (hasPokemon) {
				menu.addLine("Pokemon");
			}
			menu.addLine("Bag").addLine("Trainer").addLine("Save").addLine("Options")
					.addLine("Exit");
			window = new BorderBoxContent(-90, 0, 90, menu.getHeight()).addContent(menu);
		}

		batch.begin();
		window.render(ri, batch, ri.screenWidth / ri.getScale(), 0);
		batch.end();
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
				startMenuOption = StartMenuOptions.Pokemon;
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
