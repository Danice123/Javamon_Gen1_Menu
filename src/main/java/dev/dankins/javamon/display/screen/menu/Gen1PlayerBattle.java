package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.battlesystem.BattleAction;
import dev.dankins.javamon.logic.battlesystem.BattleAction.BattleActionEnum;

public class Gen1PlayerBattle implements PlayerBattleMenu {

	private MonsterInstance pokemon;

	private int index = 0;
	private int mindex = 0;
	private boolean isMoveMenuOpen = false;
	private BattleAction action;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final MonsterInstance pokemon) {
		this.pokemon = pokemon;
	}

	@Override
	public void init(final AssetManager assets) {

	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		if (pokemon == null) {
			return;
		}
		batch.begin();

		if (isMoveMenuOpen) {
			ri.border.drawBox(batch, 50 * ri.getScale(), 0, ri.screenWidth - 50 * ri.getScale(),
					50 * ri.getScale());
			for (int i = 0; i < pokemon.getAttacks().size(); i++) {
				ri.font.draw(batch, pokemon.getAttacks().get(i).getName(),
						(50 + 20) * ri.getScale(), (15 + 9 * (3 - i)) * ri.getScale());
			}
			batch.draw(ri.arrow.rightArrow, (50 + 9) * ri.getScale(),
					(6 + 9 * (3 - mindex)) * ri.getScale(),
					ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
					ri.arrow.rightArrow.getRegionHeight() * ri.getScale());
		} else {
			ri.border.drawBox(batch, 110 * ri.getScale(), 0, ri.screenWidth - 110 * ri.getScale(),
					50 * ri.getScale());
			ri.font.draw(batch, "Fight", (110 + 20) * ri.getScale(), 40 * ri.getScale());
			ri.font.draw(batch, "PKMN", (110 + 80) * ri.getScale(), 40 * ri.getScale());
			ri.font.draw(batch, "Item", (110 + 20) * ri.getScale(), 20 * ri.getScale());
			ri.font.draw(batch, "Run", (110 + 80) * ri.getScale(), 20 * ri.getScale());
			batch.draw(ri.arrow.rightArrow, (110 + 9 + 60 * (index % 2)) * ri.getScale(),
					(31 - 20 * (index / 2)) * ri.getScale(),
					ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
					ri.arrow.rightArrow.getRegionHeight() * ri.getScale());
		}
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {

	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isMoveMenuOpen) {
			handleKeyMove(key);
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

	private void handleKeyMove(final Key key) {
		switch (key) {
		case up:
			if (mindex > 0) {
				mindex--;
			}
			break;
		case down:
			if (mindex < 3) {
				mindex++;
			}
			break;
		case accept:
			action = new BattleAction(BattleActionEnum.Attack, mindex);
			ThreadUtils.notifyOnObject(this);
			break;
		case deny:
			isMoveMenuOpen = false;
			break;
		default:
		}
	}

	@Override
	public BattleAction getAction() {
		return action;
	}

}
