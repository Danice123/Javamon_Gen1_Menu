package dev.dankins.javamon.display.screen.menu;

import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.Maps;

import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.Gen1HealthBar;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Trainer;
import dev.dankins.javamon.logic.battlesystem.Battlesystem;

public class Gen1Battle implements BattleMenu {

	private Battlesystem system;
	private Trainer player;
	private Trainer enemy;

	private Map<Integer, Texture> playerPokemonTex;
	private Map<Integer, Texture> enemyPokemonTex;
	private String message = "";

	private final Gen1HealthBar healthBar = new Gen1HealthBar();

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final Battlesystem system, final Trainer player, final Trainer enemy) {
		this.system = system;
		this.player = player;
		this.enemy = enemy;
	}

	@Override
	public void init(final AssetManager assets) {
		// TODO: Load with assetManager
		playerPokemonTex = Maps.newHashMap();
		player.getParty().stream().map(monster -> monster.getBaseMonster().getNumber())
				.forEach(number -> {
					playerPokemonTex.put(number,
							new Texture("assets/pokemon/back/" + number + ".png"));
				});

		enemyPokemonTex = Maps.newHashMap();
		enemy.getParty().stream().map(monster -> monster.getBaseMonster().getNumber())
				.forEach(number -> {
					enemyPokemonTex.put(number, new Texture("assets/pokemon/" + number + ".png"));
				});
	}

	@Override
	public void setMessageBoxContents(final String message) {
		this.message = message;
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		shape.begin(ShapeType.Filled);
		batch.begin();

		renderPlayerHealthBar(ri, batch, shape, system.getPlayerMonster());
		renderEnemyHealthBar(ri, batch, shape, system.getEnemyMonster());

		final Texture playerTex = playerPokemonTex
				.get(system.getPlayerMonster().getBaseMonster().getNumber());
		batch.draw(playerTex, 20 * ri.getScale(), 35 * ri.getScale(),
				playerTex.getWidth() * 3 * ri.getScale(),
				playerTex.getHeight() * 3 * ri.getScale());

		final Texture enemyTex = enemyPokemonTex
				.get(system.getEnemyMonster().getBaseMonster().getNumber());
		batch.draw(enemyTex, 160 * ri.getScale(), 100 * ri.getScale(),
				enemyTex.getWidth() * ri.getScale(), enemyTex.getHeight() * ri.getScale());

		ri.border.drawBox(batch, 0, 0, ri.screenWidth, 50 * ri.getScale());
		ri.font.draw(batch, message, ri.border.WIDTH + 2, 50 * ri.getScale() - ri.border.HEIGHT,
				ri.screenWidth - 2 * (ri.border.WIDTH + 2) - 100 * ri.getScale(), Align.left, true);
		batch.end();
		shape.end();
	}

	private void renderPlayerHealthBar(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final MonsterInstance poke) {
		shape.setColor(0f, 0f, 0f, 0f);
		shape.rect(ri.screenWidth - 15 * ri.getScale(), 55 * ri.getScale(), 2 * ri.getScale(),
				30 * ri.getScale());
		final int blength = 115;
		shape.rect(ri.screenWidth - (blength + 5) * ri.getScale(), 55 * ri.getScale(),
				(blength - 10) * ri.getScale(), 2 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 7) * ri.getScale(), 55 * ri.getScale(),
				2 * ri.getScale(), 1 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 3) * ri.getScale(), 55 * ri.getScale(),
				4 * ri.getScale(), 3 * ri.getScale());
		shape.rect(ri.screenWidth - (blength + 1) * ri.getScale(), 55 * ri.getScale(),
				2 * ri.getScale(), 4 * ri.getScale());

		healthBar.render(ri, shape, poke.getCurrentHealthPercent(),
				ri.screenWidth - 100 * ri.getScale(), 70 * ri.getScale(), 80 * ri.getScale(),
				6 * ri.getScale());

		ri.font.draw(batch, "HP:", ri.screenWidth - 125 * ri.getScale(), 77 * ri.getScale());
		ri.font.draw(batch, poke.getCurrentHealth() + "/ " + poke.getHealth(),
				ri.screenWidth - 95 * ri.getScale(), 68 * ri.getScale());
		ri.font.draw(batch, ":L" + poke.getLevel(), ri.screenWidth - 90 * ri.getScale(),
				86 * ri.getScale());
		ri.font.draw(batch, poke.getName(), ri.screenWidth - 114 * ri.getScale(),
				96 * ri.getScale());
	}

	private void renderEnemyHealthBar(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final MonsterInstance poke) {
		shape.setColor(0f, 0f, 0f, 0f);
		shape.rect(12 * ri.getScale(), ri.screenHeight - 30 * ri.getScale(), 2 * ri.getScale(),
				12 * ri.getScale());
		final int blength = 115;
		shape.rect(12 * ri.getScale(), ri.screenHeight - 30 * ri.getScale(),
				blength * ri.getScale(), 2 * ri.getScale());
		healthBar.render(ri, shape, poke.getCurrentHealthPercent(), 41 * ri.getScale(),
				ri.screenHeight - 25 * ri.getScale(), 80 * ri.getScale(), 6 * ri.getScale());

		ri.font.draw(batch, "HP:", 16 * ri.getScale(), ri.screenHeight - 18 * ri.getScale());
		ri.font.draw(batch, ":L" + poke.getLevel(), 46 * ri.getScale(),
				ri.screenHeight - 9 * ri.getScale());
		ri.font.draw(batch, poke.getName(), 10 * ri.getScale(), ri.screenHeight - ri.getScale());
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {

	}
}
