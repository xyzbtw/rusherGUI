package dev.sf.ui.utils.shaders;

public class Shaders {
    public static Shader FILL;

    public static void init() {
        FILL = ShaderUtil.load("fill", "color/0", "pos/1");
    }

}
