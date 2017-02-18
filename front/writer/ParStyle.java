package front.writer;

import static javafx.scene.text.TextAlignment.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import org.fxmisc.richtext.model.Codec;

/**
 * Holds information about the style of a paragraph.
 */
class ParStyle {

    public static final ParStyle EMPTY = new ParStyle();

    public static final Codec<ParStyle> CODEC = new Codec<ParStyle>() {

        private final Codec<Optional<TextAlignment>> OPT_ALIGNMENT_CODEC =
                Codec.optionalCodec(Codec.enumCodec(TextAlignment.class));
        private final Codec<Optional<Color>> OPT_COLOR_CODEC =
                Codec.optionalCodec(Codec.COLOR_CODEC);
        private final Codec<Optional<String>> OPT_PADDING_CODEC = 
        		Codec.optionalCodec(Codec.STRING_CODEC);

        @Override
        public String getName() {
            return "par-style";
        }

        @Override
        public void encode(DataOutputStream os, ParStyle t) throws IOException {
            OPT_ALIGNMENT_CODEC.encode(os, t.alignment);
            OPT_COLOR_CODEC.encode(os, t.backgroundColor);
        }

        @Override
        public ParStyle decode(DataInputStream is) throws IOException {
            return new ParStyle(
                    OPT_ALIGNMENT_CODEC.decode(is),
                    OPT_COLOR_CODEC.decode(is),
                    OPT_PADDING_CODEC.decode(is));
        }

    };

    public static ParStyle alignLeft() { return EMPTY.updateAlignment(LEFT); }
    public static ParStyle alignCenter() { return EMPTY.updateAlignment(CENTER); }
    public static ParStyle alignRight() { return EMPTY.updateAlignment(RIGHT); }
    public static ParStyle alignJustify() { return EMPTY.updateAlignment(JUSTIFY); }
    public static ParStyle backgroundColor(Color color) { return EMPTY.updateBackgroundColor(color); }
    public static ParStyle padding(String padding) { return EMPTY.updatePadding(padding);  }

    final Optional<TextAlignment> alignment;
    final Optional<Color> backgroundColor;
    final Optional<String> padding;

    public ParStyle() {
        this(Optional.empty(), Optional.empty(), Optional.empty());
    }

    public ParStyle(Optional<TextAlignment> alignment, Optional<Color> backgroundColor, Optional<String> padding) {
        this.alignment = alignment;
        this.backgroundColor = backgroundColor;
        this.padding = padding;
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(alignment, backgroundColor, padding);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof ParStyle) {
            ParStyle that = (ParStyle) other;
            return Objects.equals(this.alignment, that.alignment) &&
                   Objects.equals(this.backgroundColor, that.backgroundColor) &&
                   Objects.equals(this.padding, that.padding);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toCss();
    }

    public String toCss() {
        StringBuilder sb = new StringBuilder();

        alignment.ifPresent(al -> {
            String cssAlignment;
            switch(al) {
                case LEFT:    cssAlignment = "left";    break;
                case CENTER:  cssAlignment = "center";  break;
                case RIGHT:   cssAlignment = "right";   break;
                case JUSTIFY: cssAlignment = "justify"; break;
                default: throw new AssertionError("unreachable code");
            }
            sb.append("-fx-text-alignment: " + cssAlignment + ";");
        });

        backgroundColor.ifPresent(color -> {
            sb.append("-fx-background-color: " + TextStyle.cssColor(color) + ";");
        });
        
        padding.ifPresent(padding -> {
        	sb.append("-fx-padding: " + padding);
        });

        return sb.toString();
    }

    public ParStyle updateWith(ParStyle mixin) {
        return new ParStyle(
                mixin.alignment.isPresent() ? mixin.alignment : alignment,
                mixin.backgroundColor.isPresent() ? mixin.backgroundColor : backgroundColor,
                mixin.padding.isPresent() ? mixin.padding : padding
               );
    }

    public ParStyle updateAlignment(TextAlignment alignment) {
        return new ParStyle(Optional.of(alignment), backgroundColor, padding);
    }

    public ParStyle updateBackgroundColor(Color backgroundColor) {
        return new ParStyle(alignment, Optional.of(backgroundColor), padding);
    }
    
    public ParStyle updatePadding(String padding) {
    	return new ParStyle(alignment, backgroundColor, Optional.of(padding));
    }

}