package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.Timer;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.display.screen.menu.game.Gen1TitleMenu;
import dev.dankins.javamon.logic.Key;

public class Gen1GameMenu implements GameMenu {

	private TitleStage stage = TitleStage.Start;
	private GameMenuAction action;

	// Logo
	private Content startScreen;

	// Title Screen
	private final Gen1TitleMenu titleMenu = new Gen1TitleMenu();

	// Menu
	private BorderBox menuScreen;
	private ListBox menu;

	@Override
	public boolean renderBehind() {
		return false;
	}

	private boolean hasSave;
	private String gameName;

	@Override
	public void setupMenu(final String gameName, final boolean hasSave) {
		this.gameName = gameName;
		this.hasSave = hasSave;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		titleMenu.init(assets, ri, gameName);
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		startScreen = new VertBox(20, 50).setSpacing(12)
				.addContent(new TextContent(font, "'95'96'98 Nintendo"))
				.addContent(new TextContent(font, "'95'96'98 Creatures inc."))
				.addContent(new TextContent(font, "'95'96'98 Game Freak inc."));

		menuScreen = new BorderBox(assets, 0, 0);
		menuScreen.addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			if (hasSave) {
				menu.addContent(new TextContent(font, "Continue"));
			}
			menu.addContent(new TextContent(font, "New Game"))
					.addContent(new TextContent(font, "Option"));
			return menu;
		}).setLeftPadding(6);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		switch (stage) {
		case Start:
			rh.withSpriteBatch((batch) -> startScreen.render(rh, 0, 0));
			break;
		case TitleScreen:
			titleMenu.renderScreen(rh, delta);
			break;
		case Menu:
			rh.withSpriteBatch((batch) -> menuScreen.render(rh, 0, 0));
			break;
		}
	}

	private final Timer startTimer = new Timer(2) {

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			stage = TitleStage.TitleScreen;
		}
	};

	@Override
	public void tickSelf(final float delta) {
		switch (stage) {
		case Start:
			startTimer.tick(delta);
			break;
		case TitleScreen:
			titleMenu.tickSelf(delta);
			break;
		case Menu:
			break;
		}

	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (stage) {
		case Start:
			break;
		case TitleScreen:
			stage = TitleStage.Menu;
			break;
		case Menu:
			handleMenuMenuKey(key);
			break;
		}
	}

	private void handleMenuMenuKey(final Key key) {
		switch (key) {
		case accept:
			switch (menu.getIndex()) {
			case 0:
				if (hasSave) {
					action = GameMenuAction.LoadGame;
					ThreadUtils.notifyOnObject(this);
				} else {
					action = GameMenuAction.NewGame;
					ThreadUtils.notifyOnObject(this);
				}
				break;
			case 1:
				if (hasSave) {
					action = GameMenuAction.NewGame;
					ThreadUtils.notifyOnObject(this);
				}
				break;
			case 2:
				break;
			}
			break;
		case deny:
			stage = TitleStage.TitleScreen;
			break;
		case up:
			menu.decrement();
			break;
		case down:
			menu.increment();
			break;
		default:
		}
	}

	@Override
	public GameMenuAction getMenuAction() {
		return action;
	}

	private enum TitleStage {
		Start, TitleScreen, Menu
	}

}
