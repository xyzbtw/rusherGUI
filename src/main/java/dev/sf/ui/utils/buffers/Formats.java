package dev.sf.ui.utils.buffers;
import static org.lwjgl.opengl.GL30.*;
public enum Formats {

    QUAD(4,GL_QUADS),
    TRIANGLE(3,GL_TRIANGLES),
    LINE(2,GL_LINES);


    int size;
    int glType;

    Formats(int size, int glType) {
        this.size = size;
        this.glType = glType;
    }
}
