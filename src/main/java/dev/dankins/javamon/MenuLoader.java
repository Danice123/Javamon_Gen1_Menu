package dev.dankins.javamon;

import java.util.List;

import com.google.common.collect.Lists;

import dev.dankins.javamon.display.screen.menu.Gen1Bag;
import dev.dankins.javamon.display.screen.menu.Gen1Battle;
import dev.dankins.javamon.display.screen.menu.Gen1Chatbox;
import dev.dankins.javamon.display.screen.menu.Gen1Choicebox;
import dev.dankins.javamon.display.screen.menu.Gen1GameMenu;
import dev.dankins.javamon.display.screen.menu.Gen1ItemStorage;
import dev.dankins.javamon.display.screen.menu.Gen1PC;
import dev.dankins.javamon.display.screen.menu.Gen1Party;
import dev.dankins.javamon.display.screen.menu.Gen1PartyStatus;
import dev.dankins.javamon.display.screen.menu.Gen1PlayerBattle;
import dev.dankins.javamon.display.screen.menu.Gen1Pokedex;
import dev.dankins.javamon.display.screen.menu.Gen1PokedexPage;
import dev.dankins.javamon.display.screen.menu.Gen1Save;
import dev.dankins.javamon.display.screen.menu.Gen1Shop;
import dev.dankins.javamon.display.screen.menu.Gen1StartMenu;
import dev.dankins.javamon.display.screen.menu.Gen1TextInput;
import dev.dankins.javamon.display.screen.menu.Gen1Trainer;

public class MenuLoader implements LoadMenusFromHere {

	@Override
	public List<Class<?>> load() {
		final List<Class<?>> menus = Lists.newArrayList();
		menus.add(Gen1GameMenu.class);
		menus.add(Gen1TextInput.class);
		menus.add(Gen1StartMenu.class);
		menus.add(Gen1Pokedex.class);
		menus.add(Gen1PokedexPage.class);
		menus.add(Gen1Party.class);
		menus.add(Gen1PartyStatus.class);
		menus.add(Gen1Battle.class);
		menus.add(Gen1PlayerBattle.class);
		menus.add(Gen1Bag.class);
		menus.add(Gen1Trainer.class);
		menus.add(Gen1Save.class);
		menus.add(Gen1ItemStorage.class);
		menus.add(Gen1Chatbox.class);
		menus.add(Gen1Choicebox.class);
		menus.add(Gen1PC.class);
		menus.add(Gen1Shop.class);
		return menus;
	}
}
