package dev.dankins.javamon.display.screen.menu;

import java.util.List;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.Timer;
import dev.dankins.javamon.battle.display.event.Event;
import dev.dankins.javamon.battle.display.event.TargetedEvent;
import dev.dankins.javamon.battle.display.event.attack.AttackEvent;
import dev.dankins.javamon.battle.display.event.attack.TypeEffectivenessEvent;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.data.monster.instance.Trainer;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.battle.animation.MonsterRelease;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Battle implements BattleMenu {

	static public final AssetDescriptor<Texture> POKEBALL = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/pokeball.png", Texture.class);
	static public final AssetDescriptor<Texture> DEAD_POKEBALL = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/dead_pokeball.png", Texture.class);
	static public final AssetDescriptor<Texture> EMPTY_POKEBALL = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/empty_pokeball.png", Texture.class);

	private AssetManager assets;

	private Trainer player;
	private HorzBox playerInfo;
	private VertBox playerMonsterInfo;
	private MonsterDisplayChanger playerChanger;
	private ImageContent playerImage;
	private float playerCurrentHealth;

	private Trainer enemy;
	private HorzBox enemyInfo;
	private VertBox enemyMonsterInfo;
	private MonsterDisplayChanger enemyChanger;
	private ImageContent enemyImage;
	private float enemyCurrentHealth;

	private BorderBox textBox;
	private TextContent textContent;
	private boolean verifyText = false;
	private Object textLock = new Object();

	private final Object initLock = new Object();
	private final List<Timer> animations = Lists.newArrayList();

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final Trainer player, final Trainer enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		this.assets = assets;
		assets.load(POKEBALL);
		assets.load(DEAD_POKEBALL);
		assets.load(EMPTY_POKEBALL);
		assets.load(player.getImage());
		assets.load(enemy.getImage());
		player.getParty_().forEach(
				monster -> assets.load("pokemon/back/" + monster.getBaseMonster().getNumber() + ".png", Texture.class));
		enemy.getParty_().forEach(
				monster -> assets.load("pokemon/" + monster.getBaseMonster().getNumber() + ".png", Texture.class));
		assets.finishLoading();

		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		final FontHelper miniFont = MenuLoader.getFont(assets, ri, 6);

		playerImage = new ImageContent(assets.get(player.getImage()));
		playerImage.setVisibility(true);
		playerInfo = new HorzBox(0, 0).setSpacing(2);
		for (int i = 0; i < 6; i++) {
			if (i < Iterables.size(player.getParty_())) {
				playerInfo.addContent(new ImageContent(assets.get(POKEBALL)));
			} else {
				playerInfo.addContent(new ImageContent(assets.get(EMPTY_POKEBALL)));
			}
		}
		playerInfo.setVisibility(true);
		playerMonsterInfo = new VertBox(0, 0).setSpacing(2);
		playerMonsterInfo.setVisibility(true);
		playerChanger = (monster) -> {
			playerMonsterInfo.clearContent().addContent(new TextContent(font, monster.getName()))
					.addContent(new HorzBox(0, 0).setSpacing(2).addContent(new TextContent(miniFont, ":L"))
							.addContent(new TextContent(font, "" + monster.getLevel()).setTopMargin(-2))
							.setLeftMargin(50))
					.addContent(new TextContent(miniFont, "HP:").setTopMargin(1))
					.addContent(new TextContent(font, monster.getCurrentHealth() + "/" + monster.getHealth())
							.setLeftMargin(30).setTopMargin(1));
		};

		enemyImage = new ImageContent(assets.get(enemy.getImage()));
		enemyImage.setVisibility(true);
		enemyInfo = new HorzBox(0, 0).setSpacing(2);
		for (int i = 0; i < 6; i++) {
			if (i < Iterables.size(enemy.getParty_())) {
				enemyInfo.addContent(new ImageContent(assets.get(POKEBALL)));
			} else {
				enemyInfo.addContent(new ImageContent(assets.get(EMPTY_POKEBALL)));
			}
		}
		enemyInfo.setVisibility(true);
		enemyMonsterInfo = new VertBox(0, 0).setSpacing(2);
		enemyMonsterInfo.setVisibility(true);
		enemyChanger = (monster) -> {
			enemyMonsterInfo.clearContent().addContent(new TextContent(font, monster.getName()))
					.addContent(new HorzBox(0, 0).setSpacing(2).addContent(new TextContent(miniFont, ":L"))
							.addContent(new TextContent(font, "" + monster.getLevel()).setTopMargin(-2))
							.setLeftMargin(35))
					.addContent(new TextContent(miniFont, "HP:").setLeftMargin(10).setTopMargin(2));
		};

		textBox = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth);
		textBox.setMinHeight(50).addContent(() -> {
			textContent = new TextContent(font, "").setWrappingWidth(ri.screenWidth - 20).setIsProgressive();
			return textContent;
		}).setLeftPadding(8).setTopPadding(10);

		ThreadUtils.notifyOnObject(initLock);
	}

	@Override
	public void sendEvent(Event event) {
		switch (event.getType()) {
		case StartBattle:
			startBattle();
			break;
		case Attack: {
			final AttackEvent e = (AttackEvent) event;
			if (player.getKey() == e.key) {
				setMessageBoxContentsVerify(player.getCurrentMonster_().getName() + " used " + e.attack.getName());
			} else {
				setMessageBoxContentsVerify(
						"Enemy " + enemy.getCurrentMonster_().getName() + " used " + e.attack.getName());
			}
			break;
		}
		case UpdateHealth:
			updateHealth(player.getCurrentMonster_(), enemy.getCurrentMonster_());
			break;
		case TypeEffectiveness: {
			final TypeEffectivenessEvent e = (TypeEffectivenessEvent) event;
			if (e.effectM() != null) {
				setMessageBoxContentsVerify(e.effectM());
			}
			break;
		}
		case CriticalHit:
			setMessageBoxContentsVerify("It was a critical hit!");
			break;
		case FaintMonster: {
			final TargetedEvent e = (TargetedEvent) event;
			if (player.getKey() == e.getTarget()) {
				setMessageBoxContentsVerify(player.getCurrentMonster_().getName() + " fainted!");
			} else {
				setMessageBoxContentsVerify("Enemy " + enemy.getCurrentMonster_().getName() + " fainted!");
			}
			break;
		}
		case TrainerLoss:
			setMessageBoxContentsVerify(player.getName() + " defeated " + enemy.getName() + "!");
			setMessageBoxContentsVerify(enemy.getTrainerLossQuip());
			break;
		case EndBattle:
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			System.out.println("Unhandled event " + event.getType());
		}
	}

	public void startBattle() {
		ThreadUtils.waitOnObject(initLock);
		playerImage.setLeftMargin(PLAYER_OFF_BEGINNING);
		playerImage.setVisibility(false);
		enemyImage.setLeftMargin(ENEMY_OFF_BEGINNING);
		enemyImage.setVisibility(false);

		synchronized (animations) {
			animations.add(beginning);
		}
		ThreadUtils.waitOnObject(beginning);
		synchronized (animations) {
			animations.remove(beginning);
		}
		playerInfo.setVisibility(false);
		if (enemy.isTrainer()) {
			enemyInfo.setVisibility(false);
		}

		setMessageBoxContentsVerify(enemy.getName() + " wants to fight!");
		moveEnemyFromWindow();
		setMessageBoxContents(enemy.getName() + " sent out " + enemy.getCurrentMonster_().getName() + "!");
		enemyMonster(enemy.getCurrentMonster_());
		movePlayerFromWindow();
		setMessageBoxContents("Go! " + player.getCurrentMonster_().getName() + "!");
		playerMonster(player.getCurrentMonster_());
		setMessageBoxContents("");
	}

	public void moveEnemyFromWindow() {
		enemyInfo.setVisibility(true);
		synchronized (animations) {
			animations.add(enemyMoveOut);
		}
		ThreadUtils.waitOnObject(enemyMoveOut);
		synchronized (animations) {
			animations.remove(enemyMoveOut);
		}
	}

	public void movePlayerFromWindow() {
		playerInfo.setVisibility(true);
		synchronized (animations) {
			animations.add(playerMoveOut);
		}
		ThreadUtils.waitOnObject(playerMoveOut);
		synchronized (animations) {
			animations.remove(playerMoveOut);
		}
	}

	public void enemyMonster(final MonsterInstance monster) {
		enemyChanger.change(monster);
		enemyCurrentHealth = monster.getCurrentHealthPercent();
		enemyImage.setTexture(assets.get("pokemon/" + monster.getBaseMonster().getNumber() + ".png", Texture.class))
				.setScale(0).setLeftMargin(0);
		enemyMonsterInfo.setVisibility(false);
		Timer playerMonsterRelease = new MonsterRelease(enemyImage, 1f, 0);
		synchronized (animations) {
			animations.add(playerMonsterRelease);
		}
		ThreadUtils.waitOnObject(playerMonsterRelease);
		synchronized (animations) {
			animations.remove(playerMonsterRelease);
		}
	}

	public void playerMonster(final MonsterInstance monster) {
		playerChanger.change(monster);
		playerCurrentHealth = monster.getCurrentHealthPercent();
		playerImage
				.setTexture(assets.get("pokemon/back/" + monster.getBaseMonster().getNumber() + ".png", Texture.class))
				.setScale(0).setLeftMargin(0).setTopMargin(4);
		playerMonsterInfo.setVisibility(false);
		Timer playerMonsterRelease = new MonsterRelease(playerImage, 2f, 4);
		synchronized (animations) {
			animations.add(playerMonsterRelease);
		}
		ThreadUtils.waitOnObject(playerMonsterRelease);
		synchronized (animations) {
			animations.remove(playerMonsterRelease);
		}
	}

	public void setMessageBoxContents(final String message) {
		textContent.setText(message);
		if (message != "") {
			ThreadUtils.waitOnObject(textLock);
		}
	}

	public void setMessageBoxContentsVerify(final String message) {
		textContent.setText(message);
		verifyText = true;
		if (message != "") {
			ThreadUtils.waitOnObject(textLock);
		}
	}

	public void updateHealth(final MonsterInstance player, final MonsterInstance enemy) {
		if (player.getCurrentHealthPercent() != playerCurrentHealth) {
			final HealthTimer healthTimer = new HealthTimer(playerCurrentHealth, player.getCurrentHealthPercent()) {
				@Override
				protected void modifyValue(final float current) {
					playerCurrentHealth = current;
				}
			};
			synchronized (animations) {
				animations.add(healthTimer);
			}
			ThreadUtils.waitOnObject(healthTimer);
			synchronized (animations) {
				animations.remove(healthTimer);
			}
			playerChanger.change(player);
		}
		if (enemy.getCurrentHealthPercent() != enemyCurrentHealth) {
			final HealthTimer healthTimer = new HealthTimer(enemyCurrentHealth, enemy.getCurrentHealthPercent()) {
				@Override
				protected void modifyValue(final float current) {
					enemyCurrentHealth = current;
				}
			};
			synchronized (animations) {
				animations.add(healthTimer);
			}
			ThreadUtils.waitOnObject(healthTimer);
			synchronized (animations) {
				animations.remove(healthTimer);
			}
		}
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withShapeRenderer((shape) -> {
			shape.filled((helper) -> {
				if (!playerInfo.isHidden()) {
					helper.arrowCorner(rh.ri.screenWidth - 15, 55, 80, 10);
				}

				if (!playerMonsterInfo.isHidden()) {
					helper.arrowCorner(rh.ri.screenWidth - 15, 55, 110, 30);
					helper.percentBar(rh.ri.screenWidth - 100, 70, 80, 6, playerCurrentHealth);
				}

				if (!enemyInfo.isHidden()) {
					helper.arrowCornerRight(15, rh.ri.screenHeight - 32, 85, 10);
				}

				if (!enemyMonsterInfo.isHidden()) {
					helper.arrowCornerRight(15, rh.ri.screenHeight - 32, 110, 17);
					helper.percentBar(41, rh.ri.screenHeight - 24, 80, 6, enemyCurrentHealth);
				}
			});
		});

		rh.withSpriteBatch((batch) -> {
			enemyInfo.render(rh, 35, 23);
			enemyMonsterInfo.render(rh, 10, 0);
			playerInfo.render(rh, 165, 98);
			playerMonsterInfo.render(rh, 122, 67);

			enemyImage.render(rh, 185, 30);
			playerImage.render(rh, 50, 82);

			textBox.render(rh, 0, rh.ri.screenHeight - textBox.getHeight());
		});
	}

	@Override
	public void tickSelf(final float delta) {
		textContent.tickSelf(delta);
		if (textContent.isFinished() && !verifyText) {
			ThreadUtils.notifyOnObject(textLock);
		}

		synchronized (animations) {
			for (final Timer animation : animations) {
				animation.tick(delta);
			}
		}
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (key == Key.accept || key == Key.deny) {
			if (!textContent.isFinished()) {
				textContent.finishText();
				if (!verifyText) {
					ThreadUtils.notifyOnObject(textLock);
				}
			} else if (verifyText) {
				ThreadUtils.notifyOnObject(textLock);
				verifyText = false;
			}
		}
	}

	private interface MonsterDisplayChanger {
		void change(MonsterInstance monster);
	}

	static private final int PLAYER_OFF_BEGINNING = 250;
	static private final int ENEMY_OFF_BEGINNING = -PLAYER_OFF_BEGINNING;

	private final Timer beginning = new Timer(0) {

		static private final float TIME = 1.0f;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			int current = (int) (timeSinceRung * PLAYER_OFF_BEGINNING / TIME);
			if (timeSinceRung >= TIME) {
				current = PLAYER_OFF_BEGINNING;
				done();
			}
			playerImage.setLeftMargin(PLAYER_OFF_BEGINNING - current);
			enemyImage.setLeftMargin(ENEMY_OFF_BEGINNING + current);
		}
	};

	private final Timer enemyMoveOut = new Timer(0) {

		static private final int ENEMY_OFF = 100;
		static private final float TIME = 0.5f;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			int current = (int) (timeSinceRung * ENEMY_OFF / TIME);
			if (timeSinceRung >= TIME) {
				current = ENEMY_OFF;
				done();
			}
			enemyImage.setLeftMargin(current);
		}
	};

	private final Timer playerMoveOut = new Timer(0) {

		static private final int PLAYER_OFF = 100;
		static private final float TIME = 0.5f;

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			int current = (int) (timeSinceRung * PLAYER_OFF / TIME);
			if (timeSinceRung >= TIME) {
				current = PLAYER_OFF;
				done();
			}
			playerImage.setLeftMargin(-current);
		}
	};

	private abstract class HealthTimer extends Timer {

		static private final float TIME = 0.5f;

		private final float start;
		private final float target;

		public HealthTimer(final float start, final float target) {
			super(0);
			this.start = start;
			this.target = target;
		}

		@Override
		public void ring(final float delta, final float timeSinceRung) {
			float current = start + timeSinceRung * (target - start) / TIME;
			if (timeSinceRung >= TIME) {
				current = target;
				done();
			}
			modifyValue(current);
		}

		protected abstract void modifyValue(float current);

	}

}
