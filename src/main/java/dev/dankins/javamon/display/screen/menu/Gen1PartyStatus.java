package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.Stat;
import dev.dankins.javamon.data.monster.attack.Attack;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1PartyStatus implements PartyStatusMenu {

	private MonsterInstance pokemon;

	private boolean page;

	private Content image;
	private Content number;
	private Content name;
	private Content infoBox;
	private Content expBox;
	private Content infoBox2;
	private Content statsBox;
	private Content moveBox;

	private TextContent statsInd;
	private TextContent attackValue;
	private TextContent defenseValue;
	private TextContent specialAttackValue;
	private TextContent specialDefenseValue;
	private TextContent speedValue;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final MonsterInstance pokemon) {
		this.pokemon = pokemon;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		assets.load("pokemon/" + pokemon.getBaseMonster().getNumber() + ".png", Texture.class);
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		final FontHelper miniFont = MenuLoader.getFont(assets, ri, 6);

		image = new ImageContent(assets
				.get("pokemon/" + pokemon.getBaseMonster().getNumber() + ".png", Texture.class))
						.alignBottom();
		number = new HorzBox(0, 0).addContent(new TextContent(miniFont, "No.")).addContent(
				new TextContent(font, "" + pokemon.getBaseMonster().getFormattedNumber())
						.setTopMargin(-2));
		name = new TextContent(font, pokemon.getName());

		infoBox = new VertBox(0, 0).setSpacing(0)
				.addContent(
						new HorzBox(0, 0).setSpacing(2).addContent(new TextContent(miniFont, ":L"))
								.addContent(new TextContent(font, "" + pokemon.getLevel())
										.setTopMargin(-2))
								.setLeftMargin(80).setTopMargin(5))
				.addContent(new TextContent(miniFont, "HP:").setLeftMargin(30).setTopMargin(4))
				.addContent(new TextContent(font,
						pokemon.getCurrentHealth() + "/ " + pokemon.getHealth()).setLeftMargin(65)
								.setTopMargin(2))
				.addContent(new TextContent(font, "Status/" + pokemon.getStatus().name)
						.setTopMargin(9));

		expBox = new VertBox(0, 0).setSpacing(2).addContent(new TextContent(font, "EXP Points"))
				.addContent(new TextContent(font, "" + pokemon.getExp()).setLeftMargin(40))
				.addContent(new TextContent(font, "Level Up"))
				.addContent(new HorzBox(0, 0).setSpacing(2)
						.addContent(new TextContent(font, "" + pokemon.getExpToNextLevel()))
						.addContent(new TextContent(miniFont, "to").setTopMargin(2))
						.addContent(new TextContent(font, "" + pokemon.getLevel() + 1)
								.setLeftMargin(4)));

		statsInd = new TextContent(font, "Stats");
		statsInd.setVisibility(true);
		statsBox = new BorderBox(assets,
				0, 0)
						.setLeftPadding(
								8)
						.setTopPadding(10).setBottomPadding(10)
						.addContent(new VertBox(0, 0).setSpacing(0)
								.addContent(new TextContent(font, "Attack")).addContent(() -> {
									attackValue = new TextContent(font, "" + pokemon.getAttack());
									attackValue.setLeftMargin(-110).alignRight();
									return attackValue;
								}).addContent(new TextContent(font, "Defense")).addContent(() -> {
									defenseValue = new TextContent(font, "" + pokemon.getDefense());
									defenseValue.setLeftMargin(-110).alignRight();
									return defenseValue;
								}).addContent(new TextContent(font, "Special Attack"))
								.addContent(() -> {
									specialAttackValue = new TextContent(font,
											"" + pokemon.getSpecialAttack());
									specialAttackValue.setLeftMargin(-110).alignRight();
									return specialAttackValue;
								}).addContent(new TextContent(font, "Special Defense"))
								.addContent(() -> {
									specialDefenseValue = new TextContent(font,
											"" + pokemon.getSpecialDefense());
									specialDefenseValue.setLeftMargin(-110).alignRight();
									return specialDefenseValue;
								}).addContent(new TextContent(font, "Speed")).addContent(() -> {
									speedValue = new TextContent(font, "" + pokemon.getSpeed());
									speedValue.setLeftMargin(-110).alignRight();
									return speedValue;
								}));

		infoBox2 = new VertBox(0, 0).setSpacing(2)
				.addContent(
						new VertBox(0, 0).setSpacing(2).addContent(new TextContent(font, "Type1/"))
								.addContent(new TextContent(font,
										pokemon.getBaseMonster().getType(0).name).setLeftMargin(8)))
				.addContent(() -> {
					final String typeName = pokemon.getBaseMonster().isDualType()
							? pokemon.getBaseMonster().getType(1).name : "NULL";
					final Content entry = new VertBox(0, 0).setSpacing(2)
							.addContent(new TextContent(font, "Type2/"))
							.addContent(new TextContent(font, typeName).setLeftMargin(8));
					if (!pokemon.getBaseMonster().isDualType()) {
						entry.setVisibility(true);
					}
					return entry;
				})
				.addContent(new VertBox(0, 0).setSpacing(2)
						.addContent(new TextContent(font, "ID No/"))
						.addContent(new TextContent(font, pokemon.getId()).setLeftMargin(8)))
				.addContent(new VertBox(0, 0).setSpacing(2).addContent(new TextContent(font, "OT/"))
						.addContent(new TextContent(font, pokemon.getOT()).setLeftMargin(8)));

		moveBox = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).setMinHeight(90)
				.setTopPadding(10).setBottomPadding(10).addContent(() -> {
					final VertBox moveList = new VertBox(0, 0);
					moveList.setSpacing(2);
					for (int i = 0; i < 4; i++) {
						if (pokemon.getAttacks().size() <= i) {
							moveList.addContent(new TextContent(font, "-"))
									.addContent(new TextContent(font, "--").setLeftMargin(120));
						} else {
							final Attack attack = pokemon.getAttacks().get(i);
							moveList.addContent(new TextContent(font, attack.getName()))
									.addContent(new HorzBox(0, 0)
											.addContent(new TextContent(font, "PP"))
											.addContent(new TextContent(font,
													"" + attack.getCurrentUsage())
															.setLeftMargin(20))
											.addContent(new TextContent(font, "/"))
											.addContent(new TextContent(font,
													"" + attack.getMaxUsage()))
											.setLeftMargin(120));
						}
					}
					return moveList;
				});
	}

	private int toggle = 0;

	private void toggleStatusBox() {
		if (toggle >= 2) {
			toggle = 0;
		} else {
			toggle++;
		}

		switch (toggle) {
		case 0:
			statsInd.setText("Stats");
			statsInd.setVisibility(true);
			attackValue.setText("" + pokemon.getAttack());
			defenseValue.setText("" + pokemon.getDefense());
			specialAttackValue.setText("" + pokemon.getSpecialAttack());
			specialDefenseValue.setText("" + pokemon.getSpecialDefense());
			speedValue.setText("" + pokemon.getSpeed());
			break;
		case 1:
			statsInd.setText("IVs");
			statsInd.setVisibility(false);
			attackValue.setText("" + pokemon.getIV(Stat.ATTACK));
			defenseValue.setText("" + pokemon.getIV(Stat.DEFENSE));
			specialAttackValue.setText("" + pokemon.getIV(Stat.SPECIAL_ATTACK));
			specialDefenseValue.setText("" + pokemon.getIV(Stat.SPECIAL_DEFENSE));
			speedValue.setText("" + pokemon.getIV(Stat.SPEED));
			break;
		case 2:
			statsInd.setText("EVs");
			statsInd.setVisibility(false);
			attackValue.setText("" + pokemon.getEV(Stat.ATTACK));
			defenseValue.setText("" + pokemon.getEV(Stat.DEFENSE));
			specialAttackValue.setText("" + pokemon.getEV(Stat.SPECIAL_ATTACK));
			specialDefenseValue.setText("" + pokemon.getEV(Stat.SPECIAL_DEFENSE));
			speedValue.setText("" + pokemon.getEV(Stat.SPEED));
			break;
		}
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withShapeRenderer((shape) -> {
			shape.filled((helper) -> {
				helper.arrowCorner(rh.ri.screenWidth - 5, 2, 80, 80);
				helper.arrowCorner(rh.ri.screenWidth - 5, 95, 140, 60);
				if (!page) {
					helper.percentBar(rh.ri.screenWidth - 90, 125, 80, 6,
							pokemon.getCurrentHealthPercent());
				}
			});
		});

		rh.withSpriteBatch((batch) -> {
			image.render(rh, 50, 80);
			number.render(rh, 10, 64);
			name.render(rh, rh.ri.screenWidth - 139, 5);

			if (!page) {
				infoBox.render(rh, rh.ri.screenWidth - 139, 15);
				statsBox.render(rh, 0, rh.ri.screenHeight - statsBox.getHeight());
				statsInd.render(rh, 1, 1);
				infoBox2.render(rh, rh.ri.screenWidth - 105, 78);
			} else {
				expBox.render(rh, rh.ri.screenWidth - 139, 25);
				moveBox.render(rh, 0, rh.ri.screenHeight - moveBox.getHeight());
			}
		});
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
			toggleStatusBox();
			break;
		default:
			break;
		}
	}

}
