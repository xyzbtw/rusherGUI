package dev.sf.ui.utils.shaders;
import static org.lwjgl.opengl.GL30.*;
public class Shader {

    private final int glId;
    private final String vertexShaderName, fragmentShaderName;

    public Shader(int glId, String vertexShaderName, String fragmentShaderName) {
        this.glId = glId;
        this.vertexShaderName = vertexShaderName;
        this.fragmentShaderName = fragmentShaderName;
    }



    public int getGlId() {
        return glId;
    }

    public String getVertexShaderName() {
        return vertexShaderName;
    }

    public String getFragmentShaderName() {
        return fragmentShaderName;
    }



    public void bind() {
        glUseProgram(glId);
    }
    public  void unBind() {
        glUseProgram(0);
    }

}
