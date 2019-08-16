package dev.dankins.javamon.display.screen.menu;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Chatbox implements Chatbox {

	private BorderBox box;
	private TextContent textContent;

	private String[] text;
	private int index = 0;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final String text) {
		index = 0;
		if (text.contains("\\p")) {
			this.text = text.split("\\\\p");
		} else {
			this.text = new String[] { text };
		}
	}

	public boolean isFinished() {
		return !(index < text.length - 1);
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		box = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth);
		box.setMinHeight(50).addContent(() -> {
			textContent = new TextContent(font, text[index]).setWrappingWidth(ri.screenWidth - 20)
					.setIsProgressive();
			return textContent;
		}).setLeftPadding(8).setTopPadding(10);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> box.render(rh, 0, rh.ri.screenHeight - box.getHeight()));
	}

	@Override
	public void tickSelf(final float delta) {
		textContent.tickSelf(delta);
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (key == Key.accept || key == Key.deny) {
			if (!textContent.isFinished()) {
				textContent.finishText();
				ThreadUtils.sleep(100);
				return;
			}
			if (!isFinished()) {
				index++;
				textContent.setText(text[index]);
				return;
			}
			ThreadUtils.notifyOnObject(this);
		}
	}

}
