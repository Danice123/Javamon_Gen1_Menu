package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.instance.Party;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.Gen1HealthBar;
import dev.dankins.javamon.logic.Key;

public class Gen1Party implements PartyMenu {

	private static final String text = "Choose a Pokemon";

	private PartyMenuType type;
	private Party party;
	private int[] health;

	private PartyMenuAction action;
	private int pokemonIndex = 0;
	private int pokemonToSwitchIndex = -99;

	private boolean isSubmenuOpen = false;
	private int submenuIndex = 0;

	private final Gen1HealthBar healthBar = new Gen1HealthBar();

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final PartyMenuType menuType, final Party party) {
		type = menuType;
		this.party = party;
		health = new int[party.size()];
		for (int i = 0; i < party.size(); i++) {
			health[i] = party.get(i).getHealth();
		}
		pokemonToSwitchIndex = -99;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		shape.begin(ShapeType.Filled);
		for (int i = 0; i < party.size(); i++) {
			healthBar.render(ri, shape, party.get(i).getCurrentHealthPercent(), 55 * ri.getScale(),
					ri.screenHeight - (19 + 20 * i) * ri.getScale(), 80 * ri.getScale(),
					6 * ri.getScale());
		}
		shape.end();
		batch.begin();
		for (int i = 0; i < party.size(); i++) {
			ri.font.draw(batch, party.get(i).getName(), 30 * ri.getScale(),
					ri.screenHeight - (2 + 20 * i) * ri.getScale());
			ri.font.draw(batch, ":L" + party.get(i).getLevel(), 120 * ri.getScale(),
					ri.screenHeight - (2 + 20 * i) * ri.getScale());
			ri.font.draw(batch, party.get(i).getCurrentHealth() + "/ " + health[i],
					170 * ri.getScale(), ri.screenHeight - (12 + 20 * i) * ri.getScale());
			ri.font.draw(batch, "HP:", 30 * ri.getScale(),
					ri.screenHeight - (12 + 20 * i) * ri.getScale());
		}

		TextureRegion a = ri.arrow.rightArrow;
		if (isSubmenuOpen) {
			a = ri.arrow.rightArrowAlt;
		}
		batch.draw(a, 2 * ri.getScale(), ri.screenHeight - (10 + 20 * pokemonIndex) * ri.getScale(),
				a.getRegionWidth() * ri.getScale(), a.getRegionHeight() * ri.getScale());

		if (pokemonToSwitchIndex != -99) {
			batch.draw(ri.arrow.rightArrowAlt, 2 * ri.getScale(),
					ri.screenHeight - (10 + 20 * pokemonToSwitchIndex) * ri.getScale(),
					ri.arrow.rightArrowAlt.getRegionWidth() * ri.getScale(),
					ri.arrow.rightArrowAlt.getRegionHeight() * ri.getScale());
		}

		// chatbox
		ri.border.drawBox(batch, 0, 0, ri.screenWidth, 40 * ri.getScale());
		ri.font.draw(batch, text, ri.border.WIDTH + 2, 40 * ri.getScale() - ri.border.HEIGHT,
				ri.screenWidth - 2 * (ri.border.WIDTH + 2), Align.left, true);

		if (isSubmenuOpen) {
			renderSubmenu(ri, batch);
		}
		batch.end();
	}

	private void renderSubmenu(final RenderInfo ri, final SpriteBatch batch) {
		ri.border.drawBox(batch, ri.screenWidth - 75 * ri.getScale(), 0, 75 * ri.getScale(),
				50 * ri.getScale());
		switch (type) {
		case View:
			ri.font.draw(batch, "Status", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 0)) * ri.getScale());
			ri.font.draw(batch, "Switch", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 1)) * ri.getScale());
			break;
		case Switch:
			ri.font.draw(batch, "Switch", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 0)) * ri.getScale());
			ri.font.draw(batch, "Status", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 1)) * ri.getScale());
			break;
		case UseItem:
			ri.font.draw(batch, "Use", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 0)) * ri.getScale());
			ri.font.draw(batch, "Status", ri.screenWidth - 60 * ri.getScale(),
					(15 + 14 * (2 - 1)) * ri.getScale());
			break;
		}
		ri.font.draw(batch, "Cancel", ri.screenWidth - 60 * ri.getScale(),
				(15 + 14 * (2 - 2)) * ri.getScale());

		batch.draw(ri.arrow.rightArrow, ri.screenWidth - 70 * ri.getScale(),
				(7 + 14 * (2 - submenuIndex)) * ri.getScale(),
				ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
				ri.arrow.rightArrow.getRegionHeight() * ri.getScale());
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isSubmenuOpen) {
			handleKeySubmenu(key);
			return;
		}
		switch (key) {
		case up:
			if (pokemonIndex > 0) {
				pokemonIndex--;
			} else {
				pokemonIndex = party.size() - 1;
			}

			if (pokemonIndex == pokemonToSwitchIndex) {
				pokemonIndex--;
			}
			if (pokemonIndex == -1) {
				pokemonIndex = party.size() - 1;
			}
			if (pokemonIndex == pokemonToSwitchIndex) {
				pokemonIndex--;
			}
			break;
		case down:
			if (pokemonIndex < party.size() - 1) {
				pokemonIndex++;
			} else {
				pokemonIndex = 0;
			}

			if (pokemonIndex == pokemonToSwitchIndex) {
				pokemonIndex++;
			}
			if (pokemonIndex == party.size()) {
				pokemonIndex = 0;
			}
			if (pokemonIndex == pokemonToSwitchIndex) {
				pokemonIndex++;
			}
			break;
		case accept:
			if (pokemonToSwitchIndex != -99) {
				action = PartyMenuAction.Switch;
				ThreadUtils.notifyOnObject(this);
			} else {
				isSubmenuOpen = true;
			}
			break;
		case deny:
			if (pokemonToSwitchIndex != -99) {
				pokemonToSwitchIndex = -99;
			} else {
				action = PartyMenuAction.Exit;
				ThreadUtils.notifyOnObject(this);
			}
			break;
		default:
			break;
		}
	}

	private void handleKeySubmenu(final Key key) {
		switch (key) {
		case up:
			if (submenuIndex > 0) {
				submenuIndex--;
			}
			break;
		case down:
			if (submenuIndex < 2) {
				submenuIndex++;
			}
			break;
		case accept:
			switch (submenuIndex) {
			case 0: // Status OR Switch
				switch (type) {
				case View:
					action = PartyMenuAction.View;
					ThreadUtils.notifyOnObject(this);
					break;
				case Switch:
				case UseItem:
					action = PartyMenuAction.Switch;
					ThreadUtils.notifyOnObject(this);
					break;
				}
				break;
			case 1: // Switch or Status
				switch (type) {
				case View:
					pokemonToSwitchIndex = pokemonIndex;
					pokemonIndex++;
					if (pokemonIndex == party.size()) {
						pokemonIndex = 0;
					}
					isSubmenuOpen = false;
					break;
				case Switch:
				case UseItem:
					action = PartyMenuAction.View;
					ThreadUtils.notifyOnObject(this);
					break;
				}
				break;
			case 2:
				isSubmenuOpen = false;
				break;
			}
			break;
		case deny:
			isSubmenuOpen = false;
			break;
		default:
			break;
		}
	}

	@Override
	public PartyMenuAction getMenuAction() {
		return action;
	}

	@Override
	public int getPokemonChoice() {
		return pokemonIndex;
	}

	@Override
	public int getSwitchChoice() {
		return pokemonToSwitchIndex;
	}

}
