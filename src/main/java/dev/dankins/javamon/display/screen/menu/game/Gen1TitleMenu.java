package dev.dankins.javamon.display.screen.menu.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.Timer;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.animation.Animation;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.AnimationContent;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;

public class Gen1TitleMenu implements Gen1GameMenuPart {

	static public final AssetDescriptor<Texture> TITLE_IMAGE = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/Title.png", Texture.class);

	static private final int TITLE_TOP_START = -60;
	static private final int VERSION_TOP_START = 330;
	static private final int POKEMON_CENTERED = 110;

	private final Gen1TitlePokemonSwitcher titlePokemon = new Gen1TitlePokemonSwitcher(4);
	private Animation titlePlayer;

	private Content title;
	private Content version;
	private AnimationContent trainer;
	private AnimationContent pokemon;
	private TextContent bottomText;

	@Override
	public void init(final AssetManager assets, final RenderInfo ri, final String gameName) {
		assets.load(TITLE_IMAGE);
		assets.load("animation/titlePlayer", Animation.class);
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		titlePokemon.init(assets, ri);
		titlePlayer = assets.get("animation/titlePlayer");

		title = new ImageContent(assets.get(TITLE_IMAGE)).setCenterPoint(68, 22)
				.setTopMargin(TITLE_TOP_START);
		version = new TextContent(font, gameName).setLeftMargin(VERSION_TOP_START);
		pokemon = new AnimationContent(titlePokemon);
		pokemon.setLeftMargin(POKEMON_CENTERED);
		trainer = new AnimationContent(titlePlayer);
		bottomText = new TextContent(font, "'95 '96 '98 Game Freak inc.");
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		final int screenMiddle = rh.ri.screenWidth / 2;

		rh.withSpriteBatch((batch) -> {
			title.render(rh, screenMiddle, 40);
			version.render(rh, screenMiddle - version.getWidth() / 2, 70);
			pokemon.render(rh, 0, 125);
			trainer.render(rh, screenMiddle + 20, 120);
			bottomText.render(rh, screenMiddle - bottomText.getWidth() / 2,
					rh.ri.screenHeight - bottomText.getHeight() - 2);
		});
	}

	@Override
	public void tickSelf(final float delta) {
		titleStartTimer.tick(delta);
	}

	private final Timer titleStartTimer = new Timer(0) {

		static private final float TITLE_TIME = 0.2f;
		static private final float VERSION_TIME = 0.2f;

		private boolean titleDown = false;
		private boolean versionOver = false;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (!titleDown) {
				int current = (int) (timeSinceRung * -TITLE_TOP_START / TITLE_TIME);
				if (timeSinceRung >= TITLE_TIME) {
					current = -TITLE_TOP_START;
					titleDown = true;
					reset();
				}
				title.setTopMargin(TITLE_TOP_START + current);
			} else if (!versionOver) {
				int current = (int) (timeSinceRung * VERSION_TOP_START / VERSION_TIME);
				if (timeSinceRung >= VERSION_TIME) {
					current = VERSION_TOP_START;
					versionOver = true;
					reset();
				}
				version.setLeftMargin(VERSION_TOP_START - current);
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

		static private final int POKEMON_OFF = 140;
		static private final float POKEMON_TIME = 0.4f;

		private boolean toggle = true;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			if (toggle) {
				int current = (int) (timeSinceRung * POKEMON_OFF / POKEMON_TIME);
				if (timeSinceRung >= POKEMON_TIME) {
					current = POKEMON_OFF;
					toggle = false;
					titlePokemon.next();
					reset();
				}
				pokemon.setLeftMargin(POKEMON_CENTERED - current);
			} else {
				int current = (int) (timeSinceRung * POKEMON_OFF / POKEMON_TIME);
				if (timeSinceRung >= POKEMON_TIME) {
					current = POKEMON_OFF;
					toggle = true;
					reset();
				}
				pokemon.setLeftMargin(POKEMON_CENTERED + POKEMON_OFF - current);
			}
		}
	};
}
