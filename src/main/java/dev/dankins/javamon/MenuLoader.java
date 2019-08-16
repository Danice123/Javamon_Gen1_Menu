package dev.dankins.javamon;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dev.dankins.javamon.display.RenderInfo;
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
import dev.dankins.javamon.display.screen.menu.content.RightArrow;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;

public class MenuLoader implements LoadMenusFromHere {

	static public final AssetDescriptor<FreeTypeFontGenerator> FONT_RESOURCE = new AssetDescriptor<FreeTypeFontGenerator>(
			"jar:file:menu.jar!/resources/Gen1-Font.ttf", FreeTypeFontGenerator.class);

	static private final Map<Integer, FontHelper> fontCache = Maps.newHashMap();

	static public FontHelper getFont(final AssetManager assets, final RenderInfo ri,
			final int size) {
		if (!fontCache.containsKey(size)) {
			fontCache.put(size, new FontHelper(assets.get(FONT_RESOURCE), size, ri.getScale()));
		}
		return fontCache.get(size);
	}

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

	@Override
	public List<AssetDescriptor<?>> loadResources() {
		final List<AssetDescriptor<?>> resources = Lists.newArrayList();
		resources.add(RightArrow.RESOURCE);
		resources.add(BorderBox.RESOURCE);
		resources.add(FONT_RESOURCE);
		return resources;
	}
}//
