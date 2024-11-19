package ch.maystre.gilbert.vorodraw.gui;

import android.graphics.Color;

public abstract class Palette {

    public static Palette getPaletteForInt(int i){
        switch(i){
            case 0: return new Palette.HortensiusPalette();
            case 1: return new Palette.PopPalette();
            case 2: return new Palette.BubbleGumPalette();
            default: return new Palette.PastelRainbowPalette();
        }
    }

    public abstract int getColorFor(int i);

    public static final class BubbleGumPalette extends Palette{
        @Override
        public int getColorFor(int i) {
            switch(i){
                case 0: return Color.rgb(255,221,209);
                case 1: return Color.rgb(240,201,215);
                case 2: return Color.rgb(212,194,225);
                case 3: return Color.rgb(189,215,234);
                case 4: return Color.rgb(192,240,244);
            }
            return Color.BLACK;
        }
    }

    public static final class PopPalette extends Palette{
        @Override
        public int getColorFor(int i) {
            switch(i){
                case 0: return Color.RED;
                case 1: return Color.BLUE;
                case 2: return Color.CYAN;
                case 3: return Color.YELLOW;
                case 4: return Color.GREEN;
            }
            return Color.BLACK;
        }
    }

    public static final class HortensiusPalette extends Palette{
        @Override
        public int getColorFor(int i) {
            switch(i){
                case 0: return Color.rgb(254,101,148);
                case 1: return Color.rgb(123,119,200);
                case 2: return Color.rgb(76,195,248);
                case 3: return Color.rgb(254,183,148);
                case 4: return Color.rgb(255,219,92);
            }
            return Color.BLACK;
        }
    }

    public static final class PastelRainbowPalette extends Palette{
        @Override
        public int getColorFor(int i) {
            switch(i){
                case 0: return Color.rgb(255,179,186);
                case 1: return Color.rgb(255,223,186);
                case 2: return Color.rgb(255,255,186);
                case 3: return Color.rgb(186,255,201);
                case 4: return Color.rgb(186,225,255);
            }
            return Color.BLACK;
        }
    }


}
