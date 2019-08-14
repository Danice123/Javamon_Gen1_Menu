package dev.dankins.javamon.display.screen.menu;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.menu.helper.BorderBoxContent;
import dev.dankins.javamon.display.screen.menu.helper.BoxContent;
import dev.dankins.javamon.display.screen.menu.helper.ListBox;
import dev.dankins.javamon.logic.Key;

public class Gen1Choicebox implements Choicebox {

	private final Gen1Chatbox chatbox = new Gen1Chatbox();
	private List<String> variables;

	@Override
	public boolean renderBehind() {
		return true;
	}

	@Override
	public void setupMenu(final String text, final List<String> variables) {
		chatbox.setupMenu(text);
		this.variables = variables;
	}

	@Override
	public void init(final AssetManager assets) {
	}

	private BoxContent window;
	private ListBox menu;

	@Override
	public void renderScreen(final RenderInfo ri, final SpriteBatch batch,
			final ShapeRenderer shape, final float delta) {
		if (menu == null) {
			if (variables.size() > 1) {
				menu = new ListBox(0, 0);
				for (final String var : variables) {
					menu.addLine(var);
				}
				window = new BorderBoxContent(0, 0, 100, menu.getHeight()).addContent(menu);
			} else {
				menu = new ListBox(0, 0).addLine("Yes").addLine("No");
				window = new BorderBoxContent(-50, 80, 50, menu.getHeight()).addContent(menu);
			}
		}

		batch.begin();
		chatbox.renderChatbox(batch, ri);
		if (variables.size() > 1) {
			window.render(ri, batch, 0, 0);
		} else {
			window.render(ri, batch, ri.screenWidth / ri.getScale(), 0);
		}
		batch.end();
	}

	@Override
	public void tickSelf(final float delta) {
	}

	@Override
	public void handleMenuKey(final Key key) {
		if (chatbox.isFinished()) {
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
		} else {
			chatbox.handleMenuKey(key);
		}
	}

	@Override
	public int getChoiceIndex() {
		return menu.getIndex();
	}

}
