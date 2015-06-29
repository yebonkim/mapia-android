package com.mapia.filter;

/**
 * Created by daehyun on 15. 6. 21..
 */

public enum ImageFilterItem
{
    AMARO(60, "Dawn"),
    BLACKANDWHITE(18, "Night"),
    BLACKCAT(64, "Seal"),
    BLUE_VINTAGE(15, "Igloo"),
    BRANNAN(16, "Retro"),
    CALM(14, "Mist"),
    CANDY(35, "Glacier"),
    CLEAR(12, "Polaris"),
    COOL(44, "BlueIce"),
    DELICIOUS(73, "Flavor"),
    DELICIOUS2(77, "Gusto"),
    EMERALD(65, "Reindeer"),
    HEALTHY(78, "Eskimo"),
    LATTE(71, "Cloud"),
    MOCHACHOKO(53, "Walrus"),
    MONOCHROME(80, "Midnight"),
    NERVOUS(21, "Aurora"),
    NONE(-1, "Original"),
    RISE(61, "Sunset"),
    SAKURA(79, "RosySky"),
    SIESTA(34, "Breeze"),
    SOLE(33, "Glow"),
    TENDER(20, "SoftSnow"),
    TONEDOWN(22, "SeaWave"),
    TOY(19, "Pholga"),
    VIVID(4, "Poppy");

    public final int id;
    public int intensity;
    public final String name;

    private ImageFilterItem(final int id, final String name) {
        this.id = id;
        this.name = name;
        this.intensity = 255;
    }
}