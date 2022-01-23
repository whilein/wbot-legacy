/*
 *    Copyright 2022 Whilein
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package w.bot.quote;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author whilein
 */
@Setter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SimpleQuoteGenerator implements QuoteGenerator {

    private static final Font FIRA_CODE = new Font("Fira Code", Font.PLAIN, 25);
    private static final Font FIRA_CODE_SMALL = new Font("Fira Code", Font.PLAIN, 15);

    private static final Font TEXT = new Font("Sans Serif", Font.PLAIN, 23);

    BufferedImage profileImage;
    BufferedImage backgroundImage; // todo

    String text;
    String title;
    String profileFirstName;
    String profileLastName;

    String profileDescription;

    public static @NotNull QuoteGenerator create() {
        return new SimpleQuoteGenerator();
    }

    @Override
    public @NotNull BufferedImage generate() {
        val dummy = new Canvas();

        val textMetrics = dummy.getFontMetrics(TEXT);
        val firaCodeMetrics = dummy.getFontMetrics(FIRA_CODE);
        val firaCodeSmallMetrics = dummy.getFontMetrics(FIRA_CODE_SMALL);

        val lines = text == null ? null : split(textMetrics, text);
        val textHeight = text == null ? 0 : lines.size() * textMetrics.getHeight();

        val image = new BufferedImage(1024, Math.max(443, 50 + firaCodeMetrics.getHeight()
                + textMetrics.getHeight() + textHeight), BufferedImage.TYPE_3BYTE_BGR);

        val graphics = image.createGraphics();

        graphics.setFont(FIRA_CODE);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        graphics.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (profileImage != null) {
            graphics.drawImage(profileImage, 25, 25, 270, 270, null);
        }

        if (title != null) {
            graphics.drawString(title,
                    305 + (1024 - 305) / 2 - firaCodeMetrics.stringWidth(title) / 2,
                    25 + firaCodeMetrics.getHeight());
        }

        int nameOffset = 305;

        boolean shouldRenderFirstName = true;
        boolean shouldRenderLastName = true;

        if (profileFirstName != null && profileLastName != null) {
            val both = profileFirstName + " " + profileLastName;
            val widthBoth = firaCodeMetrics.stringWidth(both);

            if (widthBoth <= 270) {
                nameOffset += firaCodeMetrics.getHeight();

                graphics.drawString(both,
                        25 + (270 / 2) - widthBoth / 2,
                        nameOffset);

                shouldRenderFirstName = false;
                shouldRenderLastName = false;
            }
        }

        // блять)) код дублируется пиздец)

        if (shouldRenderFirstName && profileFirstName != null) {
            nameOffset += firaCodeMetrics.getHeight() + 10;

            graphics.drawString(profileFirstName,
                    25 + (270 / 2) - firaCodeMetrics.stringWidth(profileFirstName) / 2,
                    nameOffset);
        }

        if (shouldRenderLastName && profileLastName != null) {
            nameOffset += firaCodeMetrics.getHeight() + 10;

            graphics.drawString(profileLastName,
                    25 + (270 / 2) - firaCodeMetrics.stringWidth(profileLastName) / 2,
                    nameOffset);
        }

        if (profileDescription != null) {
            nameOffset += firaCodeSmallMetrics.getHeight() + 10;

            graphics.setFont(FIRA_CODE_SMALL);
            graphics.setColor(new Color(200, 200, 200));

            graphics.drawString(profileDescription,
                    25 + (270 / 2) - firaCodeSmallMetrics.stringWidth(profileDescription) / 2,
                    nameOffset);
        }

        if (text != null) {
            graphics.setFont(TEXT);
            graphics.setColor(new Color(200, 200, 200));

            int y = 25 + firaCodeMetrics.getHeight() + 25 + textMetrics.getHeight();

            for (val line : split(textMetrics, text)) {
                graphics.drawString(line, 325, y);
                y += textMetrics.getHeight();
            }
        }

        return image;
    }

    private static final int MAX_WIDTH = 674;

    private static List<String> split(final FontMetrics fontMetrics, final String string) {
        val width = fontMetrics.stringWidth(string);

        if (width <= MAX_WIDTH) {
            return List.of(string);
        }

        val result = new ArrayList<String>();

        val words = string.split("\s");

        val line = new StringBuilder();

        int lineWidth = 0;

        val spaceWidth = fontMetrics.charWidth(' ');

        boolean insertSpace = false;

        for (val word : words) {
            val wordWidth = fontMetrics.stringWidth(word);

            if (wordWidth > MAX_WIDTH) { // слово не вмещается в строку
                for (int i = 0, j = word.length(); i < j; i++) {
                    val ch = word.charAt(i);
                    val charWidth = fontMetrics.charWidth(ch);

                    if (lineWidth + charWidth > MAX_WIDTH) { // символ следует перенести на новую строку
                        result.add(line.toString()); // код дублируетса((
                        line.setLength(0);
                        lineWidth = 0;
                        insertSpace = false;
                    }

                    lineWidth += charWidth;
                    line.append(ch);
                }
            } else { // слово вмещается в строку
                if (lineWidth + wordWidth > MAX_WIDTH) { // слово не влезет в строку
                    result.add(line.toString());
                    line.setLength(0);
                    lineWidth = 0;
                    insertSpace = false;
                }

                if (insertSpace) {
                    line.append(' ');
                    lineWidth += spaceWidth;
                }

                insertSpace = true;

                lineWidth += wordWidth;
                line.append(word);
            }
        }

        if (!line.isEmpty()) {
            result.add(line.toString());
        }

        return result;
    }
}
