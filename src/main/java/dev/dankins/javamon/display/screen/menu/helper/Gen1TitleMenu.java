package dev.dankins.javamon.display.screen.menu.helper;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.animation.Animation;

public class Gen1TitleMenu implements Gen1GameMenuPart {

	private Texture titleImage;
	private Animation titlePlayer;
	private PokemonSwitcher titlePokemon;

	@Override
	public void init(final AssetManager assets) {
		assets.load("assets/Title.png", Texture.class);
		assets.load("assets/animation/titlePlayer", Animation.class);
		assets.finishLoading();
		titleImage = assets.get("assets/Title.png");
		titlePlayer = assets.get("assets/animation/titlePlayer");
		titlePokemon = new PokemonSwitcher(4, assets);
	}

	private BoxContent titleScreen;
	private ImageBox title;
	private BoxTextContent version;
	private AnimationBox pokemon;

	@Override
	public void renderScreen(final RenderInfo ri, final float delta, final SpriteBatch batch) {
		if (titleScreen == null) {
			title = new ImageBox(titleImage).setVertIndent(-60);
			version = new BoxTextContent("Red Version").setVertIndent(titleImage.getHeight() - 10)
					.setHorzIndent(190);
			pokemon = new AnimationBox(titlePokemon);
			pokemon.setVertIndent(90).setHorzIndent(40).setAlignBottom();

			titleScreen = new VertBox(50, 10).addContent(title).addContent(version)
					.addContent(pokemon)
					.addContent(new AnimationBox(titlePlayer).setHorzIndent(75).setVertIndent(20))
					.addContent(new BoxTextContent("'95 '96 '98 Game Freak inc.").setVertIndent(65)
							.setHorzIndent(-35));
		}

		batch.begin();
		titleScreen.render(ri, batch, 0, 0);
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {
		if (titleScreen != null) {
			titleStartTimer.tick(delta);
		}
	}

	private final Timer titleStartTimer = new Timer(0) {

		private boolean titleDown = false;
		private final int titlePixelPerSecond = 100;

		private boolean versionOver = false;
		private final int versionPixelPerSecond = 200;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (!titleDown) {
				int current = (int) (timeSinceRung * titlePixelPerSecond);
				if (current > 60) {
					current = 60;
					titleDown = true;
					reset();
				}
				title.setVertIndent(-60 + current);
			} else if (!versionOver) {
				int current = (int) (timeSinceRung * versionPixelPerSecond);
				if (current > 165) {
					current = 165;
					versionOver = true;
					reset();
				}
				version.setHorzIndent(190 - current);
			} else {
				ballTossTimer.tick(delta);
				pokemonTimer.tick(delta);
			}
		}

	};

	private final Timer ballTossTimer = new Timer(3) {

		private boolean toggle = false;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (!toggle) {
				titlePlayer.next();
				toggle = true;
			}
			if (timeSinceRung > 1) {
				titlePlayer.restart();
				toggle = false;
				reset();
			}
		}
	};

	private final Timer pokemonTimer = new Timer(2) {

		private boolean toggle = true;
		private final int pokemonPixelPerSecond = 200;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (toggle) {
				int current = (int) (timeSinceRung * pokemonPixelPerSecond);
				if (current > 150) {
					current = 150;
					toggle = false;
					titlePokemon.next();
					reset();
				}
				pokemon.setHorzIndent(40 - current);
			} else {
				int current = (int) (timeSinceRung * pokemonPixelPerSecond);
				if (current > 150) {
					current = 150;
					toggle = true;
					reset();
				}
				pokemon.setHorzIndent(40 + 150 - current);
			}
		}
	};

	private class PokemonSwitcher implements Animation {

		private final AssetManager assets;
		private TextureRegion currentPokemon;

		public PokemonSwitcher(final int pokemonToStart, final AssetManager assets) {
			this.assets = assets;
			loadPokemon(pokemonToStart);
		}

		private void loadPokemon(final int pokemon) {
			assets.load("assets/pokemon/" + pokemon + ".png", Texture.class);
			assets.finishLoading();
			final Texture t = assets.get("assets/pokemon/" + pokemon + ".png");
			currentPokemon = new TextureRegion(t);
		}

		@Override
		public TextureRegion getCurrentFrame() {
			return currentPokemon;
		}

		@Override
		public void next() {
			loadPokemon(ThreadLocalRandom.current().nextInt(1, 133));
		}

		@Override
		public void restart() {

		}
	}
}
