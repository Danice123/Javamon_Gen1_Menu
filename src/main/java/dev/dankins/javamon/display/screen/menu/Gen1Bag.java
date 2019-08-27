package dev.dankins.javamon.display.screen.menu;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.TextRender;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.abstraction.Item;
import dev.dankins.javamon.data.abstraction.ItemStack;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.CurrencyContent;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BasicBoxContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.HorzBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Bag implements BagMenu {

	private BagMenuType type;
	private List<? extends Item> itemsInBag;

	private BagMenuAction action;

	private Content window;
	private ListBox itemList;

	private Content textWindow;
	private TextRender textRender;

	private boolean isAmountMenuOpen = false;
	private Content amountWindow;
	private CurrencyContent amountCounter;

	private boolean isSubmenuOpen = false;
	private Content useWindow;
	private ListBox useMenu;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final BagMenuType type, final List<? extends Item> itemsInBag) {
		this.type = type;
		this.itemsInBag = itemsInBag;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, ri.screenWidth, 0).addContent(() -> {
			itemList = new ListBox(assets, 0, 0);
			populateList(font);
			return itemList;
		}).alignRight();

		textWindow = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).setMinHeight(50)
				.addContent(() -> {
					final TextContent c = new TextContent(font, "")
							.setWrappingWidth(ri.screenWidth - 20);
					textRender = (s) -> c.setText(s);
					return c;
				}).setLeftPadding(8).setTopPadding(10).setVisibility(true);

		amountWindow = new BorderBox(assets, ri.screenWidth, 0).addContent(() -> {
			amountCounter = new CurrencyContent(font, "x");
			return amountCounter;
		}).alignRight().setVisibility(true);

		useWindow = new BorderBox(assets, ri.screenWidth, 0).addContent(() -> {
			useMenu = new ListBox(assets, 0, 0);
			useMenu.addContent(new TextContent(font, "Use"))
					.addContent(new TextContent(font, "Toss"));
			return useMenu;
		}).alignRight().setVisibility(true);
	}

	private void populateList(final FontHelper font) {
		itemList.clearContent();
		for (final Item item : itemsInBag) {
			itemList.addContent(() -> {
				final BasicBoxContent itemEntry = new HorzBox(0, 0).setSpacing(12)
						.addContent(new TextContent(font, item.getName()));
				if (item instanceof ItemStack) {
					itemEntry.addContent(new TextContent(font, "x " + ((ItemStack) item).size()));
				}
				return itemEntry;
			});
		}
		itemList.addContent(new TextContent(font, "Cancel"));
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> {
			window.render(rh, 0, 0);
			textWindow.render(rh, 0, rh.ri.screenHeight - textWindow.getHeight());
			useWindow.render(rh, 0,
					rh.ri.screenHeight - textWindow.getHeight() - useWindow.getHeight());
			amountWindow.render(rh, 0,
					rh.ri.screenHeight - textWindow.getHeight() - amountWindow.getHeight());
		});
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isAmountMenuOpen) {
			switch (key) {
			case up:
				amountCounter.increment();
				break;
			case down:
				amountCounter.decrement();
				break;
			case accept:
				switch (type) {
				case Choose:
					action = BagMenuAction.Use;
					ThreadUtils.notifyOnObject(this);
					break;
				case View:
					action = BagMenuAction.Toss;
					ThreadUtils.notifyOnObject(this);
					break;
				}
				break;
			case deny:
				isAmountMenuOpen = false;
				break;
			default:
				break;
			}
			return;
		}
		if (isSubmenuOpen) {
			switch (key) {
			case up:
				useMenu.decrement();
				break;
			case down:
				useMenu.increment();
				break;
			case accept:
				actionOnItem();
				break;
			case deny:
				isSubmenuOpen = false;
				break;
			default:
				break;
			}
			return;
		}

		switch (key) {
		case up:
			itemList.decrement();
			break;
		case down:
			itemList.increment();
			break;
		case accept:
			if (itemList.getIndex() >= itemsInBag.size()) {
				action = BagMenuAction.Exit;
				ThreadUtils.notifyOnObject(this);
			} else {
				switch (type) {
				case Choose:
					final Item item = itemsInBag.get(itemList.getIndex());
					if (item instanceof ItemStack) {
						final ItemStack stack = (ItemStack) item;
						textRender.render("How many?");
						textWindow.setVisibility(false);
						amountCounter.setAmount(1);
						amountCounter.setMaxAmount(stack.size());
						amountWindow.setVisibility(false);
						isAmountMenuOpen = true;
					}
					break;
				case View:
					useWindow.setVisibility(false);
					isSubmenuOpen = true;
					break;
				}
			}
			break;
		case deny:
			action = BagMenuAction.Exit;
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	private void actionOnItem() {
		switch (useMenu.getIndex()) {
		case 0: // Use
			action = BagMenuAction.Use;
			ThreadUtils.notifyOnObject(this);
			break;
		case 1: // Toss
			final Item item = itemsInBag.get(itemList.getIndex());
			if (item instanceof ItemStack) {
				final ItemStack stack = (ItemStack) item;
				textRender.render("How many?");
				textWindow.setVisibility(false);
				amountCounter.setAmount(1);
				amountCounter.setMaxAmount(stack.size());
				amountWindow.setVisibility(false);
				isAmountMenuOpen = true;
			} else {
				action = BagMenuAction.Toss;
				ThreadUtils.notifyOnObject(this);
			}
			break;
		default:
		}
	}

	@Override
	public BagMenuAction getMenuAction() {
		return action;
	}

	@Override
	public int getMenuChoice() {
		return itemList.getIndex();
	}

	@Override
	public int getAmountChoice() {
		return amountCounter.getAmount();
	}
}
