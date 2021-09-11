package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.graphics.Color;

import dev.dankins.javamon.FontHelper;

public class DynamicTextContent extends TextContent {

	private DynamicTextContentLambda content;

	public DynamicTextContent(FontHelper font, DynamicTextContentLambda content) {
		this(font, content, Color.BLACK);
	}

	public DynamicTextContent(FontHelper font, DynamicTextContentLambda content, Color textColor) {
		super(font, content.content(), textColor);
		this.content = content;
	}

	@Override
	public void update() {
		if (getText() != content.content()) {
			setText(content.content());
		}
	}
}
