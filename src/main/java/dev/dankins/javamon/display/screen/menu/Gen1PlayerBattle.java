package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.RightArrow;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.battlesystem.BattleAction;
import dev.dankins.javamon.logic.battlesystem.BattleAction.BattleActionEnum;

public class Gen1PlayerBattle implements PlayerBattleMenu {

	private MonsterInstance pokemon;

	private BattleAction action;

	private boolean isMoveMenuOpen = false;

	private Content menu;
	private RightArrow arrow;
	private int index = 0;

	private Content moveBox;
	private ListBox moveMenu;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final MonsterInstance pokemon) {
		this.pokemon = pokemon;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		menu = new BorderBox(assets, 0, 0).setMinHeight(50).setLeftPadding(20)
				.addContent(new HorzBox(0, 0).setSpacing(12)
						.addContent(new VertBox(0, 0).addContent(new TextContent(font, "Fight"))
								.addContent(new TextContent(font, "Item")))
						.addContent(new VertBox(0, 0).addContent(new TextContent(font, "PKMN"))
								.addContent(new TextContent(font, "Run"))));
		arrow = new RightArrow(assets);

		moveBox = new BorderBox(assets, 0, 0).setMinHeight(50).setMinWidth(150).setTopPadding(8)
				.setBottomPadding(8).addContent(() -> {
					moveMenu = new ListBox(assets, 0, 0);
					moveMenu.setSpacing(1);
					for (int i = 0; i < 4; i++) {
						if (i < pokemon.getAttacks().size()) {
							moveMenu.addContent(
									new TextContent(font, pokemon.getAttacks().get(i).getName()));
						} else {
							moveMenu.addContent(new TextContent(font, "-"));
						}
					}
					return moveMenu;
				});
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> {
			menu.render(rh, rh.ri.screenWidth - menu.getWidth(),
					rh.ri.screenHeight - menu.getHeight());
			arrow.render(rh, rh.ri.screenWidth - menu.getWidth() + 9 + 45 * (index % 2),
					rh.ri.screenHeight - menu.getHeight() + 22 + 15 * (index / 2));

			if (isMoveMenuOpen) {
				moveBox.render(rh, rh.ri.screenWidth - moveBox.getWidth(),
						rh.ri.screenHeight - moveBox.getHeight());
			}
		});
	}

	@Override
	public void tickSelf(final float delta) {

	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isMoveMenuOpen) {
			switch (key) {
			case up:
				moveMenu.decrement();
				break;
			case down:
				moveMenu.increment();
				break;
			case accept:
				action = new BattleAction(BattleActionEnum.Attack, moveMenu.getIndex());
				ThreadUtils.notifyOnObject(this);
				break;
			case deny:
				isMoveMenuOpen = false;
				break;
			default:
			}
			return;
		}

		switch (key) {
		case up:
			if (index > 1) {
				index -= 2;
			}
			break;
		case down:
			if (index < 2) {
				index += 2;
			}
			break;
		case left:
			if (index % 2 == 0) {
				index++;
			}
			break;
		case right:
			if (index % 2 == 1) {
				index--;
			}
			break;
		case accept:
			switch (index) {
			case 0: // Move menu
				isMoveMenuOpen = true;
				break;
			case 1: // Switch menu
				action = new BattleAction(BattleActionEnum.Switch, 0);
				ThreadUtils.notifyOnObject(this);
				break;
			case 2: // Item menu
				action = new BattleAction(BattleActionEnum.Item, 0);
				ThreadUtils.notifyOnObject(this);
				break;
			case 3: // Run
				action = new BattleAction(BattleActionEnum.Run, 0);
				ThreadUtils.notifyOnObject(this);
				break;
			}
			break;
		default:
		}
	}

	@Override
	public BattleAction getAction() {
		return action;
	}

}
