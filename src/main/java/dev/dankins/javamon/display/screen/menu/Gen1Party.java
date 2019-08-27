package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.monster.instance.MonsterInstance;
import dev.dankins.javamon.data.monster.instance.Party;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.ImageContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BasicBoxContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.display.screen.menu.content.box.VertBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Party implements PartyMenu {

	static public final AssetDescriptor<Texture> RESOURCE = new AssetDescriptor<>(
			"jar:file:menu.jar!/resources/pokeball.png", Texture.class);

	private PartyMenuType type;
	private Party party;

	private boolean submenuOpen = false;
	private PartyMenuAction action;

	private ListBox pokemonList;
	private VertBox pokemonStats;
	private BorderBox textBox;
	private TextContent textContent;
	private BasicBoxContent submenuWindow;
	private ListBox submenu;

	@Override
	public boolean renderBehind() {
		return false;
	}

	@Override
	public void setupMenu(final PartyMenuType menuType, final Party party) {
		type = menuType;
		this.party = party;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		assets.load(RESOURCE);
		assets.finishLoading();
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);
		final FontHelper hpFont = MenuLoader.getFont(assets, ri, 6);

		pokemonList = new ListBox(assets, 0, 8).setOldIndex(null);
		pokemonList.setSpacing(3);
		pokemonStats = new VertBox(160, 2).setSpacing(4);
		for (final MonsterInstance monster : party) {
			pokemonList.addContent(new HorzBox(0, 0)
					.addContent(
							new ImageContent(assets.get(RESOURCE)).setTopMargin(4).setLeftMargin(2))
					.addContent(new VertBox(0, -8).setSpacing(3)
							.addContent(new TextContent(font, monster.getName()))
							.addContent(new TextContent(hpFont, "HP:").setLeftMargin(9))));

			pokemonStats.addContent(new VertBox(0, 0).setSpacing(2)
					.addContent(new HorzBox(0, 0)
							.setSpacing(2).addContent(new TextContent(hpFont, ":L"))
							.addContent(new TextContent(font, "" + monster.getLevel())
									.setTopMargin(-2)))
					.addContent(new TextContent(font,
							monster.getCurrentHealth() + "/ " + monster.getHealth())));
		}

		textBox = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth);
		textBox.setMinHeight(50).addContent(() -> {
			textContent = new TextContent(font, "Choose a Pokemon.")
					.setWrappingWidth(ri.screenWidth - 20);
			return textContent;
		}).setLeftPadding(8).setTopPadding(10);

		submenuWindow = new BorderBox(assets, 0, 0).addContent(() -> {
			submenu = new ListBox(assets, 0, 0);
			switch (type) {
			case View:
				submenu.addContent(new TextContent(font, "Status"))
						.addContent(new TextContent(font, "Switch"));
				break;
			case Switch:
				submenu.addContent(new TextContent(font, "Switch"))
						.addContent(new TextContent(font, "Status"));
				break;
			case UseItem:
				submenu.addContent(new TextContent(font, "Use"))
						.addContent(new TextContent(font, "Status"));
				break;
			}
			submenu.addContent(new TextContent(font, "Cancel"));
			return submenu;
		}).setLeftPadding(6);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withShapeRenderer((shape) -> {
			shape.filled((helper) -> {
				for (int i = 0; i < party.size(); i++) {
					helper.percentBar(55, rh.ri.screenHeight - (16 + 18 * i), 80, 6,
							party.get(i).getCurrentHealthPercent());
				}
			});
		});

		rh.withSpriteBatch((batch) -> {
			pokemonList.render(rh, 0, 0);
			pokemonStats.render(rh, 0, 0);
			textBox.render(rh, 0, rh.ri.screenHeight - textBox.getHeight());

			if (submenuOpen) {
				submenuWindow.render(rh, rh.ri.screenWidth - submenuWindow.getWidth(),
						rh.ri.screenHeight - submenuWindow.getHeight());
			}
		});
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (submenuOpen) {
			switch (key) {
			case up:
				submenu.decrement();
				break;
			case down:
				submenu.increment();
				break;
			case accept:
				switch (submenu.getIndex()) {
				case 0: // Status OR Switch
					switch (type) {
					case View:
						action = PartyMenuAction.View;
						submenuOpen = false;
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
						pokemonList.setOldIndex(pokemonList.getIndex());
						pokemonList.increment();
						submenuOpen = false;
						break;
					case Switch:
					case UseItem:
						action = PartyMenuAction.View;
						ThreadUtils.notifyOnObject(this);
						break;
					}
					break;
				case 2:
					submenuOpen = false;
					break;
				}
				break;
			case deny:
				submenuOpen = false;
				break;
			default:
				break;
			}
			return;
		}
		switch (key) {
		case up:
			pokemonList.decrement();
			break;
		case down:
			pokemonList.increment();
			break;
		case accept:
			if (pokemonList.getOldIndex() != null) {
				action = PartyMenuAction.Switch;
				ThreadUtils.notifyOnObject(this);
				pokemonList.swapEntries();
			} else {
				submenuOpen = true;
			}
			break;
		case deny:
			if (pokemonList.getOldIndex() != null) {
				pokemonList.setOldIndex(null);
			} else {
				action = PartyMenuAction.Exit;
				ThreadUtils.notifyOnObject(this);
			}
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
		return pokemonList.getIndex();
	}

	@Override
	public int getSwitchChoice() {
		return pokemonList.getOldIndex();
	}

	@Override
	public void resetAfterSwitch() {
		pokemonList.setOldIndex(null);
	}

}
