package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.RenderInfo;

public class ListBox extends VertBox {

	private int arrowIndent = 12;
	private boolean arrowSelected = false;
	private boolean arrowHidden = false;

	private int index;

	private int topOfList = 0;
	private Integer listSize = null;

	public ListBox(final int x, final int y) {
		super(x, y);
	}

	public ListBox addLine(final String line) {
		addContent(new BoxTextContent(line));
		return this;
	}

	public ListBox setListSize(final int listSize) {
		this.listSize = listSize;
		return this;
	}

	public ListBox setArrowIndent(final int indent) {
		arrowIndent = indent;
		return this;
	}

	public ListBox toggleArrow() {
		arrowSelected = !arrowSelected;
		return this;
	}

	public ListBox toggleArrowHidden() {
		arrowHidden = !arrowHidden;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public void decrement() {
		if (index > 0) {
			index--;
			if (index - topOfList <= 0) {
				topOfList--;
			}
			if (index == 0) {
				topOfList = 0;
			}
		} else {
			index = contents.size() - 1;
			if (listSize != null) {
				topOfList = contents.size() - listSize;
			}
		}
	}

	public void increment() {
		if (index < contents.size() - 1) {
			index++;
			if (listSize != null) {
				if (index - topOfList >= listSize - 1) {
					topOfList++;
				}
				if (index == contents.size() - 1) {
					topOfList = contents.size() - listSize;
				}
			}
		} else {
			index = 0;
			topOfList = 0;
		}
	}

	@Override
	public void render(final RenderInfo ri, final SpriteBatch batch, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;

		final TextureRegion tex = arrowSelected ? ri.arrow.rightArrowAlt : ri.arrow.rightArrow;

		if (!arrowHidden) {
			if (listSize != null) {
				batch.draw(tex, xOffset * ri.getScale(),
						ri.screenHeight - (yOffset + 8 + 18 * (index - topOfList)) * ri.getScale(),
						tex.getRegionWidth() * ri.getScale(),
						tex.getRegionHeight() * ri.getScale());
			} else {
				batch.draw(tex, xOffset * ri.getScale(),
						ri.screenHeight - (yOffset + 8 + 18 * index) * ri.getScale(),
						tex.getRegionWidth() * ri.getScale(),
						tex.getRegionHeight() * ri.getScale());
			}
		}

		super.render(ri, batch, x + arrowIndent, y);
	}

	@Override
	protected void renderContent(final RenderInfo ri, final SpriteBatch batch, final int x,
			final int y, final BoxContent content) {
		if (listSize != null) {
			final int entryIndex = contents.indexOf(content);
			if (entryIndex >= topOfList && entryIndex < topOfList + listSize) {
				final int newY = y - entryIndex * spacing + (entryIndex - topOfList) * spacing;
				content.render(ri, batch, x, newY);
			}
		} else {
			content.render(ri, batch, x, y);
		}

	}
}
