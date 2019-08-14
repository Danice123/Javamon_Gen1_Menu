package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.BorderBoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxTextContent;
import dev.dankins.javamon.display.screen.menu.helper.Gen1TitleMenu;
import dev.dankins.javamon.display.screen.menu.helper.ListBox;
import dev.dankins.javamon.display.screen.menu.helper.Timer;
import dev.dankins.javamon.display.screen.menu.helper.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1GameMenu implements GameMenu {

	private boolean hasSave;

	private TitleStage stage = TitleStage.Start;
	private GameMenuAction action;

	private final Gen1TitleMenu titleMenu = new Gen1TitleMenu();

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final boolean hasSave) {
		this.hasSave = hasSave;
	}

	@Override
	public void init(final AssetManager assets) {
		titleMenu.init(assets);
	}

	private BoxContent startScreen;

	private BoxContent window;
	private ListBox menu;

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {

		switch (stage) {
		case Start:
			if (startScreen == null) {
				startScreen = new VertBox(20, 50)
						.addContent(new BoxTextContent("'95'96'98 Nintendo"))
						.addContent(new BoxTextContent("'95'96'98 Creatures inc."))
						.addContent(new BoxTextContent("'95'96'98 Game Freak inc."));
			}
			batch.begin();
			startScreen.render(ri, batch, 0, 0);
			batch.end();
			break;
		case TitleScreen:
			titleMenu.renderScreen(ri, delta, batch);
			break;
		case Menu:
			if (menu == null) {
				if (hasSave) {
					menu = new ListBox(0, 0).addLine("Continue").addLine("New Game")
							.addLine("Option");
				} else {
					menu = new ListBox(0, 0).addLine("New Game").addLine("Option");
				}
				window = new BorderBoxContent(0, 0, 100, menu.getHeight()).addContent(menu);
			}
			batch.begin();
			window.render(ri, batch, 0, 0);
			batch.end();
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
