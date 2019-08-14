package dev.dankins.javamon.display.screen.menu;

import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.google.common.collect.Maps;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.Stat;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.Gen1HealthBar;
import dev.dankins.javamon.logic.Key;

public class Gen1PartyStatus implements PartyStatusMenu {

	private MonsterInstance pokemon;
	private Map<Stat, Integer> stats;
	private int expToNextLevel;

	private Texture image;
	private boolean page;
	private int toggle = 0;

	private final Gen1HealthBar healthBar = new Gen1HealthBar();

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final MonsterInstance pokemon) {
		this.pokemon = pokemon;
		stats = Maps.newHashMap();
		stats.put(Stat.ATTACK, pokemon.getAttack());
		stats.put(Stat.DEFENSE, pokemon.getDefense());
		stats.put(Stat.SPECIAL_ATTACK, pokemon.getSpecialAttack());
		stats.put(Stat.SPECIAL_DEFENSE, pokemon.getSpecialDefense());
		stats.put(Stat.SPEED, pokemon.getSpeed());
		stats.put(Stat.HEALTH, pokemon.getHealth());
		expToNextLevel = pokemon.getExpToNextLevel();
	}

	@Override
	public void init(final AssetManager assets) {
		image = assets.get("assets/pokemon/" + pokemon.getBaseMonster().getNumber() + ".png");
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		shape.begin(ShapeType.Filled);
		shape.setColor(0f, 0f, 0f, 0f);

		shape.rect(ri.screenWidth - 5 * ri.getScale(), 2 * ri.getScale(), 2 * ri.getScale(),
				70 * ri.getScale());
		int blength = 110;
		shape.rect(ri.screenWidth - (blength + 5) * ri.getScale(), 2 * ri.getScale(),
				blength * ri.getScale(), 2 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 7) * ri.getScale(), 2 * ri.getScale(),
				2 * ri.getScale(), 1 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 3) * ri.getScale(), 2 * ri.getScale(),
				4 * ri.getScale(), 3 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 1) * ri.getScale(), 2 * ri.getScale(),
				2 * ri.getScale(), 4 * ri.getScale());

		shape.rect(ri.screenWidth - 5 * ri.getScale(), 85 * ri.getScale(), 2 * ri.getScale(),
				70 * ri.getScale());
		blength = 140;
		shape.rect(ri.screenWidth - (blength + 5) * ri.getScale(), 85 * ri.getScale(),
				blength * ri.getScale(), 2 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 7) * ri.getScale(), 85 * ri.getScale(),
				2 * ri.getScale(), 1 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 3) * ri.getScale(), 85 * ri.getScale(),
				4 * ri.getScale(), 3 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 1) * ri.getScale(), 85 * ri.getScale(),
				2 * ri.getScale(), 4 * ri.getScale());

		if (!page) {
			healthBar.render(ri, shape, pokemon.getCurrentHealthPercent(),
					ri.screenWidth - 90 * ri.getScale(), 125 * ri.getScale(), 80 * ri.getScale(),
					6 * ri.getScale());
		}
		shape.end();
		batch.begin();
		ri.font.draw(batch, pokemon.getName(), ri.screenWidth - 135 * ri.getScale(),
				155 * ri.getScale());
		ri.font.draw(batch, "No." + pokemon.getBaseMonster().getFormattedNumber(),
				10 * ri.getScale(), 90 * ri.getScale());

		batch.draw(image, 30 * ri.getScale(), ri.screenHeight - 60 * ri.getScale(),
				image.getWidth() * ri.getScale(), image.getHeight() * ri.getScale(), 0, 0,
				image.getWidth(), image.getHeight(), true, false);

		if (!page) {
			ri.font.draw(batch, "HP:", ri.screenWidth - 115 * ri.getScale(), 132 * ri.getScale());
			ri.font.draw(batch, ":L" + pokemon.getLevel(), ri.screenWidth - 80 * ri.getScale(),
					141 * ri.getScale());

			ri.font.draw(batch, "Status/" + pokemon.getStatus().name,
					ri.screenWidth - 135 * ri.getScale(), 97 * ri.getScale());

			ri.border.drawBox(batch, 0, 0, 110 * ri.getScale(), 80 * ri.getScale());
			ri.font.draw(batch, "Attack", 10 * ri.getScale(), (72 - 0 * 14) * ri.getScale());
			ri.font.draw(batch, "Defense", 10 * ri.getScale(), (72 - 1 * 14) * ri.getScale());
			ri.font.draw(batch, "Speed", 10 * ri.getScale(), (72 - 2 * 14) * ri.getScale());
			ri.font.draw(batch, "SAttack", 10 * ri.getScale(), (72 - 3 * 14) * ri.getScale());
			ri.font.draw(batch, "SDefense", 10 * ri.getScale(), (72 - 4 * 14) * ri.getScale());

			switch (toggle) {
			case 0: // Stats
				ri.font.draw(batch, pokemon.getCurrentHealth() + "/ " + stats.get(Stat.HEALTH),
						ri.screenWidth - 85 * ri.getScale(), 123 * ri.getScale());

				ri.font.draw(batch, Integer.toString(stats.get(Stat.ATTACK)), 80 * ri.getScale(),
						(72 - 0 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(stats.get(Stat.DEFENSE)), 80 * ri.getScale(),
						(72 - 1 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(stats.get(Stat.SPEED)), 80 * ri.getScale(),
						(72 - 2 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(stats.get(Stat.SPECIAL_ATTACK)),
						80 * ri.getScale(), (72 - 3 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(stats.get(Stat.SPECIAL_DEFENSE)),
						80 * ri.getScale(), (72 - 4 * 14) * ri.getScale());
				break;
			case 1: // IV
				ri.font.draw(batch, "IV", 2 * ri.getScale(), 155 * ri.getScale());
				ri.font.draw(batch, pokemon.getCurrentHealth() + "/ " + pokemon.getIV(Stat.HEALTH),
						ri.screenWidth - 85 * ri.getScale(), 123 * ri.getScale());

				ri.font.draw(batch, Integer.toString(pokemon.getIV(Stat.ATTACK)),
						80 * ri.getScale(), (72 - 0 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getIV(Stat.DEFENSE)),
						80 * ri.getScale(), (72 - 1 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getIV(Stat.SPEED)), 80 * ri.getScale(),
						(72 - 2 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getIV(Stat.SPECIAL_ATTACK)),
						80 * ri.getScale(), (72 - 3 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getIV(Stat.SPECIAL_DEFENSE)),
						80 * ri.getScale(), (72 - 4 * 14) * ri.getScale());
				break;
			case 2: // EV
				ri.font.draw(batch, "EV", 2 * ri.getScale(), 155 * ri.getScale());
				ri.font.draw(batch, pokemon.getCurrentHealth() + "/ " + pokemon.getEV(Stat.HEALTH),
						ri.screenWidth - 85 * ri.getScale(), 123 * ri.getScale());

				ri.font.draw(batch, Integer.toString(pokemon.getEV(Stat.ATTACK)),
						80 * ri.getScale(), (72 - 0 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getEV(Stat.DEFENSE)),
						80 * ri.getScale(), (72 - 1 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getEV(Stat.SPEED)), 80 * ri.getScale(),
						(72 - 2 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getEV(Stat.SPECIAL_ATTACK)),
						80 * ri.getScale(), (72 - 3 * 14) * ri.getScale());
				ri.font.draw(batch, Integer.toString(pokemon.getEV(Stat.SPECIAL_DEFENSE)),
						80 * ri.getScale(), (72 - 4 * 14) * ri.getScale());
				break;
			}

			ri.font.draw(batch, "Type1/ " + pokemon.getBaseMonster().getType(0).name,
					112 * ri.getScale(), (73 - 0 * 14) * ri.getScale());
			if (pokemon.getBaseMonster().isDualType()) {
				ri.font.draw(batch, "Type2/ " + pokemon.getBaseMonster().getType(1).name,
						112 * ri.getScale(), (73 - 1 * 14) * ri.getScale());
			}
			ri.font.draw(batch, "ID/ " + pokemon.getId(), 112 * ri.getScale(),
					(73 - 2 * 14) * ri.getScale());
			ri.font.draw(batch, "OT/ " + pokemon.getOT(), 112 * ri.getScale(),
					(73 - 3 * 14) * ri.getScale());
		} else {
			ri.border.drawBox(batch, 0, 0, ri.screenWidth, 80 * ri.getScale());

			ri.font.draw(batch, "EXP/" + pokemon.getExp(), ri.screenWidth - 135 * ri.getScale(),
					135 * ri.getScale());

			ri.font.draw(batch, "Level Up", ri.screenWidth - 135 * ri.getScale(),
					121 * ri.getScale());
			ri.font.draw(batch, expToNextLevel + " to :L" + (pokemon.getLevel() + 1),
					ri.screenWidth - 125 * ri.getScale(), 107 * ri.getScale());

			for (int i = 0; i < 4; i++) {
				if (i >= pokemon.getAttacks().size()) {
					ri.font.draw(batch, "-", 10 * ri.getScale(), (72 - i * 17) * ri.getScale());
					ri.font.draw(batch, "--", ri.screenWidth - 80 * ri.getScale(),
							(72 - i * 17) * ri.getScale());
				} else {
					ri.font.draw(batch, pokemon.getAttacks().get(i).getName(), 10 * ri.getScale(),
							(72 - i * 17) * ri.getScale());
					ri.font.draw(batch,
							"pp " + pokemon.getAttacks().get(i).getCurrentUsage() + "/"
									+ pokemon.getAttacks().get(i).getMaxUsage(),
							ri.screenWidth - 80 * ri.getScale(), (72 - i * 17) * ri.getScale());
				}
			}
		}
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		switch (key) {
		case accept:
			if (!page) {
				page = true;
			} else {
				ThreadUtils.notifyOnObject(this);
			}
			break;
		case deny:
			if (page) {
				page = false;
			} else {
				ThreadUtils.notifyOnObject(this);
			}
			break;
		case select:
			if (toggle >= 2) {
				toggle = 0;
			} else {
				toggle++;
			}
		default:
			break;
		}
	}

}
