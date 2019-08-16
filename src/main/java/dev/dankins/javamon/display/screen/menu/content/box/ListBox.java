package dev.dankins.javamon.display.screen.menu.content.box;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.RightArrow;

public class ListBox extends VertBox {

	private final RightArrow arrow;
	private int arrowIndent = 12;

	private int index;
	private int topOfList = 0;
	private Integer listSize = null;

	public ListBox(final AssetManager assets, final int x, final int y) {
		super(x, y);
		arrow = new RightArrow(assets);
	}

	@Override
	public int getContentWidth() {
		return super.getContentWidth() + arrowIndent;
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
		arrow.toggleSelected();
		return this;
	}

	public ListBox toggleArrowHidden() {
		arrow.toggleVisibilty();
		return this;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
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
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		final int xOffset = x + this.x;
		int yOffset = y + this.y;

		if (listSize != null) {
			final Integer arrowIndexOffset = contents.stream()
					.map(content -> content.getHeight() + spacing).skip(topOfList)
					.limit(index - topOfList).reduce(0, Integer::sum);
			arrow.render(rh, xOffset, yOffset + 8 + arrowIndexOffset);
			for (int i = topOfList; i - topOfList < listSize; i++) {
				renderInternal(rh, xOffset + arrowIndent, yOffset, contents.get(i));
				yOffset += contents.get(i).getHeight() + spacing;
			}
		} else {
			final Integer arrowIndexOffset = contents.stream()
					.map(content -> content.getHeight() + spacing).limit(index)
					.reduce(0, Integer::sum);
			arrow.render(rh, xOffset, yOffset + 8 + arrowIndexOffset);
			super.renderContent(rh, xOffset + arrowIndent, yOffset);
		}
	}
}
