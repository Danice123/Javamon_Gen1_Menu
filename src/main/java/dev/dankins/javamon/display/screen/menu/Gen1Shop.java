package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.abstraction.Inventory;
import dev.dankins.javamon.data.abstraction.Item;
import dev.dankins.javamon.data.abstraction.ItemStack;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.CurrencyContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.logic.Key;
import dev.dankins.javamon.logic.abstraction.Player;

public class Gen1Shop implements ShopMenu {

	private final Gen1Chatbox chatBox = new Gen1Chatbox();
	private Player player;
	private Inventory shop;

	private boolean buyMenuOpen;
	private boolean sellMenuOpen;
	private boolean amountMenuOpen;
	private boolean waitingForInput;

	private ShopMenuOptions action;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final Player player, final Inventory shop) {
		this.player = player;
		this.shop = shop;
		chatBox.setupMenu("Hi there! May I help you?");
	}

	private Content window;
	private ListBox menu;

	private Content buyWindow;
	private ListBox buyMenu;

	private Content sellWindow;
	private ListBox sellMenu;

	private Content money;

	private Content amountMenu;
	private CurrencyContent amountBox;
	private CurrencyContent costBox;

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, 0, 0).addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			menu.addContent(new TextContent(font, "Buy")).addContent(new TextContent(font, "Sell"))
					.addContent(new TextContent(font, "Exit"));
			return menu;
		});

		buyMenu = new ListBox(assets, 0, 0);
		for (final Item item : shop.getItems()) {
			buyMenu.addContent(
					new HorzBox(0, 0).setSpacing(48).addContent(new TextContent(item.getName()))
							.addContent(new TextContent("$" + item.getCost()).setVertIndent(6)));
		}
		buyMenu.addContent(new TextContent("Cancel"));
		buyWindow = new BorderBox(20, 10, 140, buyMenu.getHeight()).addContent(buyMenu);

		amountBox = new CurrencyContent("x");
		costBox = new CurrencyContent("$");
		amountMenu = new BorderBox(40, 80, 120, 36).addContent(
				new HorzBox(0, 0).setSpacing(60).addContent(amountBox).addContent(costBox));
	}

	@Override
	public void updateMenu() {
		sellWindow = null;
		money = null;
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		if (money == null) {
			money = new BorderBox(-80, 0, 80, 36)
					.addContent(new TextContent("$" + player.getMoney()).setHorzIndent(10));
		}
		if (sellWindow == null) {
			sellMenu = new ListBox(0, 0);
			for (final Item item : player.getInventory().getItems()) {
				if (item instanceof ItemStack) {
					sellMenu.addContent(new HorzBox(0, 0).setSpacing(48)
							.addContent(new TextContent(item.getName()))
							.addContent(new TextContent("x" + ((ItemStack) item).size())
									.setVertIndent(8)));
				} else {
					sellMenu.addContent(new TextContent(item.getName()));
				}
			}
			sellMenu.addContent(new TextContent("Cancel"));
			sellWindow = new BorderBox(20, 10, 140, sellMenu.getHeight()).addContent(sellMenu);
		}

		batch.begin();
		window.render(ri, batch, 0, 0);
		if (money != null) {
			money.render(ri, batch, ri.screenWidth / ri.getScale(), 0);
		}
		chatBox.renderChatbox(batch, ri);

		if (buyMenuOpen) {
			buyWindow.render(ri, batch, 0, 0);
		}
		if (sellMenuOpen && sellMenu != null) {
			sellWindow.render(ri, batch, 0, 0);
		}
		if (amountMenuOpen) {
			amountMenu.render(ri, batch, 0, 0);
		}
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {

	}

	@Override
	public void handleMenuKey(final Key key) {
		if (amountMenuOpen) {
			handleAmountKey(key);
			return;
		}

		if (buyMenuOpen) {
			handleBuyKey(key);
			return;
		}

		if (sellMenuOpen) {
			handleSellKey(key);
			return;
		}

		switch (key) {
		case up:
			menu.decrement();
			break;
		case down:
			menu.increment();
			break;
		case accept:
			switch (menu.getIndex()) {
			case 0: // Buy
				buyMenuOpen = true;
				chatBox.setupMenu("Take your time.");
				break;
			case 1: // Sell
				sellMenuOpen = true;
				chatBox.setupMenu("What would you like to sell?");
				break;
			case 2: // Exit
				action = ShopMenuOptions.Exit;
				ThreadUtils.notifyOnObject(this);
				break;
			}
			break;
		case deny:
			action = ShopMenuOptions.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	private void handleBuyKey(final Key key) {
		if (waitingForInput) {
			if (key.equals(Key.accept) || key.equals(Key.deny)) {
				waitingForInput = false;
				chatBox.setupMenu("Take your time.");
			}
			return;
		}

		switch (key) {
		case up:
			buyMenu.decrement();
			break;
		case down:
			buyMenu.increment();
			break;
		case accept:
			if (buyMenu.getIndex() >= shop.getItems().size()) {
				buyMenuOpen = false;
				chatBox.setupMenu("Is there anything else I can do?");
			} else {
				final int cost = shop.getItems().get(buyMenu.getIndex()).getCost();
				if (cost > player.getMoney()) {
					chatBox.setupMenu("You can't afford that.");
					waitingForInput = true;
					break;
				}

				amountBox.setAmount(1);
				costBox.setAmount(cost);
				amountBox.setMaxAmount(player.getMoney() / cost);
				amountMenuOpen = true;
			}
			break;
		case deny:
			buyMenuOpen = false;
			chatBox.setupMenu("Is there anything else I can do?");
			break;
		default:
			break;
		}
	}

	private void handleSellKey(final Key key) {
		if (waitingForInput) {
			if (key.equals(Key.accept) || key.equals(Key.deny)) {
				waitingForInput = false;
				chatBox.setupMenu("What would you like to sell?");
			}
			return;
		}

		switch (key) {
		case up:
			sellMenu.decrement();
			break;
		case down:
			sellMenu.increment();
			break;
		case accept:
			if (sellMenu.getIndex() >= player.getInventory().getItems().size()) {
				sellMenuOpen = false;
				chatBox.setupMenu("Is there anything else I can do?");
			} else {
				final Item item = player.getInventory().getItems().get(sellMenu.getIndex());
				if (item.isTossable()) {
					final int maxAmount = item instanceof ItemStack ? ((ItemStack) item).size() : 1;
					amountBox.setAmount(1);
					costBox.setAmount(item.getCost() / 2);
					amountBox.setMaxAmount(maxAmount);
					amountMenuOpen = true;
				} else {
					chatBox.setupMenu("I can't put a price on that.");
					waitingForInput = true;
				}
			}
			break;
		case deny:
			sellMenuOpen = false;
			chatBox.setupMenu("Is there anything else I can do?");
			break;
		default:
			break;
		}
	}

	private void handleAmountKey(final Key key) {

		switch (key) {
		case up:
			amountBox.increment();
			break;
		case down:
			amountBox.decrement();
			break;
		case accept:
			if (buyMenuOpen) {
				action = ShopMenuOptions.Buy;
			} else if (sellMenuOpen) {
				action = ShopMenuOptions.Sell;
			}
			amountMenuOpen = false;
			ThreadUtils.notifyOnObject(this);
			break;
		case deny:
			amountMenuOpen = false;
			break;
		default:
			break;
		}
		if (buyMenuOpen) {
			costBox.setAmount(
					shop.getItems().get(buyMenu.getIndex()).getCost() * amountBox.getAmount());
		}
		if (sellMenuOpen) {
			costBox.setAmount(player.getInventory().getItems().get(sellMenu.getIndex()).getCost()
					/ 2 * amountBox.getAmount());
		}
	}

	@Override
	public ShopMenuOptions getMenuChoice() {
		return action;
	}

	@Override
	public int getMenuIndex() {
		if (buyMenuOpen) {
			return buyMenu.getIndex();
		} else if (sellMenuOpen) {
			return sellMenu.getIndex();
		}
		return -1;
	}

	@Override
	public int getMenuAmount() {
		return amountBox.getAmount();
	}

}
