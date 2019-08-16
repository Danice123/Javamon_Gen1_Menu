package dev.dankins.javamon.display.screen.menu.game;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.animation.Animation;

public class Gen1TitlePokemonSwitcher implements Animation {

	private final int startingPokemon;

	private PokemonLoader loader;
	private TextureRegion currentPokemonTex;

	public Gen1TitlePokemonSwitcher(final int startingPokemon) {
		this.startingPokemon = startingPokemon;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		loader = (pokemon) -> {
			assets.load("pokemon/" + pokemon + ".png", Texture.class);
			assets.finishLoading();
			final Texture texture = assets.get("pokemon/" + pokemon + ".png", Texture.class);
			return new TextureRegion(texture);
		};
		currentPokemonTex = loader.load(startingPokemon);
	}

	@Override
	public TextureRegion getCurrentFrame() {
		return currentPokemonTex;
	}

	@Override
	public void next() {
		// TODO: better random
		currentPokemonTex = loader.load(ThreadLocalRandom.current().nextInt(1, 133));
	}

	@Override
	public void restart() {
		currentPokemonTex = loader.load(startingPokemon);
	}

	private interface PokemonLoader {
		TextureRegion load(int pokemon);
	}
}
