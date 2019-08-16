package dev.dankins.javamon.display.screen.menu.content.box;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.FontHelper;
import dev.dankins.javamon.MenuLoader;
import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;
import dev.dankins.javamon.display.screen.menu.content.RightArrow;
import dev.dankins.javamon.display.screen.menu.content.TextContent;

public class BoxKeyboard extends VertBox {

	private static final String[][] rows = { { "A", "B", "C", "D", "E", "F", "G", "H", "I" },
			{ "J", "K", "L", "M", "N", "O", "P", "Q", "R" },
			{ "S", "T", "U", "V", "W", "X", "Y", "Z", "" },
			{ "x", "(", ")", ":", ";", "[", "]", "", "" },
			{ "-", "?", "!", "♂", "♀", "/", ".", ",", "E" } };

	private final RightArrow arrow;
	private final TextContent entry;
	// private final BoxTextContent caseDisplay;

	private int row;
	private int col;

	public BoxKeyboard(final AssetManager assets, final RenderInfo ri, final int x, final int y) {
		super(x, y);
		spacing = 12;
		arrow = new RightArrow(assets);
		final FontHelper font = MenuLoader.getFont(assets, ri, 8);

		entry = new TextContent(font, "");
		addContent(entry);
		for (final String[] row : rows) {
			final HorzBox row1Box = new HorzBox(0, 0).setSpacing(18);
			for (final String c : row) {
				row1Box.addContent(new TextContent(font, c));
			}
			addContent(row1Box);
		}
		// caseDisplay = new BoxTextContent("lower case").setVertIndent(12);
		// addContent(caseDisplay);
	}

	@Override
	public void renderContent(final RenderHelper rh, final int x, final int y) {
		final int xOffset = x + this.x;
		final int yOffset = y + this.y;

		arrow.render(rh, xOffset - 2 + 25 * col, yOffset + 8 + 19 * (row + 1));

		// batch.draw(ri.arrow.rightArrow, (xOffset + 8 + 25 * col) *
		// ri.getScale(),
		// ri.screenHeight - (yOffset + 8 + 18 * (row + 1)) * ri.getScale(),
		// ri.arrow.rightArrow.getRegionWidth() * ri.getScale(),
		// ri.arrow.rightArrow.getRegionHeight() * ri.getScale());

		super.renderContent(rh, xOffset + 8, yOffset);
	}

	public boolean accept() {
		if (row == 4 && col == 8) {
			return true;
		}
		String text = entry.getText();
		text = text + rows[row][col];
		entry.setText(text);
		return false;
	}

	public boolean deny() {
		if (entry.getText().isEmpty()) {
			return true;
		}
		String text = entry.getText();
		text = text.substring(0, text.length() - 1);
		entry.setText(text);
		return false;
	}

	public void up() {
		if (row > 0) {
			row--;
		}
	}

	public void down() {
		if (row < 4) {
			row++;
		} else if (row == 4) {
			// row++;
			// col = 0;
		}
	}

	public void left() {
		if (col < 8 && row < 5) {
			col++;
		}
	}

	public void right() {
		if (col > 0 && row < 5) {
			col--;
		}
	}

	public String getInput() {
		return entry.getText();
	}

}
