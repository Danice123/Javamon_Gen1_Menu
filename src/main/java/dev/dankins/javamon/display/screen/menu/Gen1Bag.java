package dev.dankins.javamon.display.screen.menu;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.data.abstraction.Item;
import dev.dankins.javamon.data.abstraction.ItemStack;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.logic.Key;

public class Gen1Bag implements BagMenu {

	private BagMenuType type;
	private List<? extends Item> itemsInBag;

	private final Gen1Chatbox chatbox = new Gen1Chatbox();
	private BagMenuAction action;
	private int index = 0;
	private int arrowIndex = 0;
	private boolean isSubmenuOpen = false;
	private int submenuIndex = 0;
	private boolean isAmountMenuOpen;
	private int amountMenuIndex = 1;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final BagMenuType type, final List<? extends Item> itemsInBag) {
		this.type = type;
		this.itemsInBag = itemsInBag;
		chatbox.setupMenu("How many?");
	}

	@Override
	public void init(final AssetManager assets) {
	}

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		final int width = 140 * ri.getScale();
		final int height = 90 * ri.getScale();
		final int side = ri.screenWidth - width;
		final int top = ri.screenHeight - 20 * ri.getScale();
		batch.begin();
		ri.border.drawBox(batch, side, top - height, width, height);
		ri.font.setColor(0f, 0f, 0f, 1f);

		if (itemsInBag != null) {
			int textPosition = top - 12 * ri.getScale();
			for (int i = index; i < itemsInBag.size() && i < index + 4; i++) {
				final Item item = itemsInBag.get(i);
				ri.font.draw(batch, item.getName(), side + 18 * ri.getScale(), textPosition);

				if (item instanceof ItemStack) {
					ri.font.draw(batch, "x " + ((ItemStack) item).size(),
							side + 100 * ri.getScale(), textPosition - 9 * ri.getScale());
				}

				textPosition -= 18 * ri.getScale();
			}
			if (itemsInBag.size() - index < 4) {
				ri.font.draw(batch, "Cancel", side + 18 * ri.getScale(), textPosition);
			}
		}

		batch.draw(ri.arrow.rightArrow, side + 6 * ri.getScale(),
				top - 20 * ri.getScale() - 18 * ri.getScale() * arrowIndex,
				ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
				ri.arrow.rightArrow.getRegionHeight() * ri.getScale());

		if (isSubmenuOpen) {
			renderSubmenu(ri, batch);
		}
		if (isAmountMenuOpen) {
			renderAmountMenu(ri, batch);
		}
		batch.end();
	}

	private void renderSubmenu(final RenderInfo ri, final SpriteBatch batch) {
		final int width = 60 * ri.getScale();
		final int height = 50 * ri.getScale();
		final int side = ri.screenWidth - width;
		final int top = ri.screenHeight - 80 * ri.getScale();

		ri.border.drawBox(batch, side, top - height, width, height);
		ri.font.setColor(0f, 0f, 0f, 1f);

		ri.font.draw(batch, "Use", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 0);
		ri.font.draw(batch, "Toss", side + 18 * ri.getScale(),
				top - 12 * ri.getScale() - 18 * ri.getScale() * 1);

		batch.draw(ri.arrow.rightArrow, side + 6 * ri.getScale(),
				top - 20 * ri.getScale() - 18 * ri.getScale() * submenuIndex,
				ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
				ri.arrow.rightArrow.getRegionHeight() * ri.getScale());
	}

	private void renderAmountMenu(final RenderInfo ri, final SpriteBatch batch) {
		final int width = 40 * ri.getScale();
		final int height = 25 * ri.getScale();
		final int side = ri.screenWidth - width;
		final int top = ri.screenHeight - 70 * ri.getScale();

		ri.border.drawBox(batch, side, top - height, width, height);
		ri.font.setColor(0f, 0f, 0f, 1f);

		ri.font.draw(batch, "x" + zeroBuffer(amountMenuIndex), side + 10 * ri.getScale(),
				top - 8 * ri.getScale());

		if (type.equals(BagMenuType.Choose)) {
			chatbox.renderChatbox(batch, ri);
		}
	}

	private String zeroBuffer(final int amount) {
		if (amount < 10) {
			return "0" + Integer.toString(amount);
		}
		return Integer.toString(amount);
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (isAmountMenuOpen) {
			handleAmountMenuKey(key);
			return;
		}
		if (isSubmenuOpen) {
			handleSubMenuKey(key);
			return;
		}

		switch (key) {
		case up:
			if (arrowIndex > 1) {
				arrowIndex--;
			} else if (arrowIndex == 1 && index > 0) {
				index--;
			} else if (arrowIndex > 0) {
				arrowIndex--;
			}
			break;
		case down:
			if (arrowIndex < 2) {
				arrowIndex++;
			} else if (arrowIndex == 2 && index < itemsInBag.size() + 1 - 4) {
				index++;
			} else if (arrowIndex < 3) {
				arrowIndex++;
			}
			break;
		case accept:
			if (index + arrowIndex >= itemsInBag.size()) {
				action = BagMenuAction.Exit;
				ThreadUtils.notifyOnObject(this);
			} else {
				switch (type) {
				case Choose:
					isAmountMenuOpen = true;
					break;
				case View:
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

	private void handleSubMenuKey(final Key key) {
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
			actionOnItem();
			break;
		case deny:
			isSubmenuOpen = false;
			break;
		default:
			break;
		}
	}

	private void actionOnItem() {
		switch (submenuIndex) {
		case 0: // Use
			action = BagMenuAction.Use;
			ThreadUtils.notifyOnObject(this);
			break;
		case 1: // Toss
			if (itemsInBag.get(index + arrowIndex) instanceof ItemStack) {
				isAmountMenuOpen = true;
			} else {
				action = BagMenuAction.Toss;
				ThreadUtils.notifyOnObject(this);
			}
			break;
		default:
		}
	}

	private void handleAmountMenuKey(final Key key) {
		final ItemStack item = (ItemStack) itemsInBag.get(index + arrowIndex);
		switch (key) {
		case up:
			if (amountMenuIndex < item.size()) {
				amountMenuIndex++;
			} else {
				amountMenuIndex = 1;
			}
			break;
		case down:
			if (amountMenuIndex > 1) {
				amountMenuIndex--;
			} else {
				amountMenuIndex = item.size();
			}
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
	}

	@Override
	public int getMenuChoice() {
		return index + arrowIndex;
	}

	@Override
	public BagMenuAction getMenuAction() {
		return action;
	}

	@Override
	public int getAmountChoice() {
		return amountMenuIndex;
	}
}
