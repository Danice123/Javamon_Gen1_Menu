package dev.dankins.javamon.display.screen.menu;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.Content;
import dev.dankins.javamon.display.screen.menu.content.TextContent;
import dev.dankins.javamon.display.screen.menu.content.box.BorderBox;
import dev.dankins.javamon.display.screen.menu.content.box.ListBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Choicebox implements Choicebox {

	private String text;
	private List<String> variables;

	private Content window;
	private ListBox menu;
	private Content textbox;
	private TextContent textContent;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final String text, final List<String> variables) {
		this.text = text;
		this.variables = variables;
	}

	@Override
	public void init(final AssetManager assets, final RenderInfo ri) {
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		window = new BorderBox(assets, 0, 0).addContent(() -> {
			menu = new ListBox(assets, 0, 0);
			for (final String var : variables) {
				menu.addContent(new TextContent(font, var));
			}
			return menu;
		}).setLeftPadding(6);

		textbox = new BorderBox(assets, 0, 0).setMinWidth(ri.screenWidth).setMinHeight(50)
				.addContent(() -> {
					textContent = new TextContent(font, text).setWrappingWidth(ri.screenWidth - 20)
							.setIsProgressive();
					return textContent;
				}).setLeftPadding(8).setTopPadding(10);
	}

	@Override
	public void renderScreen(final RenderHelper rh, final float delta) {
		rh.withSpriteBatch((batch) -> {
			if (textContent.isFinished()) {
				window.render(rh, 0, 0);
			}
			textbox.render(rh, 0, rh.ri.screenHeight - textbox.getHeight());
		});
	}

	@Override
	public void tickSelf(final float delta) {
		textContent.tickSelf(delta);
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (!textContent.isFinished()) {
			if (key == Key.accept || key == Key.deny) {
				textContent.finishText();
			}
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
			ThreadUtils.notifyOnObject(this);
			break;
		default:
			break;
		}
	}

	@Override
	public int getChoiceIndex() {
		return menu.getIndex();
	}

}
