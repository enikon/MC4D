package com.cyanheron.magiccube4d.gui;

public interface StringMapper {
    public String map();
    public static StringMapper IDENTITY = () -> "";
}
