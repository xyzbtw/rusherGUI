package dev.sf.ui.utils.buffers;

import dev.sf.ui.utils.shaders.Shader;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


import static org.lwjgl.opengl.GL20.*;

public class VertexConsumer {

    private int sortSize;
    private int glType;
    private BufferId colorBuffer = new BufferId(Float.BYTES, GL_FLOAT, 2048, 0, BufferId.VEC4);
    private BufferId posBuffer = new BufferId(Float.BYTES, GL_FLOAT, 2048, 0, BufferId.VEC4);
    private ByteBuffer indexBuffer = MemoryUtil.memAlloc(2048);
    private FloatBuffer projectionMatrix = MemoryUtil.memAllocFloat(16), modelMatrix = MemoryUtil.memAllocFloat(16);
    private int index = 0, vertexCount = 0;


    public void begin(Formats format, Matrix4f modelMatrix, Matrix4f projectionMatrix) {
        sortSize = format.size;
        glType = format.glType;
        modelMatrix.get(this.modelMatrix);
        projectionMatrix.get(this.projectionMatrix);
        index = 0;
    }

    public VertexConsumer color(Color color) {
        return color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public VertexConsumer color(float r, float g, float b, float a) {
        colorBuffer.put(r);
        colorBuffer.put(g);
        colorBuffer.put(b);
        colorBuffer.put(a);
        return this;
    }

    public VertexConsumer vertex(float x, float y, float z) {
        posBuffer.put(x);
        posBuffer.put(y);
        posBuffer.put(z);
        return this;
    }
    public void next() {
        updateIndex();
    }

    public void draw(Shader shader) {
        shader.bind();
        colorBuffer.end();
        posBuffer.end();
        colorBuffer.loadData();
        posBuffer.loadData();
        indexBuffer.flip();
        glUniformMatrix4fv(glGetUniformLocation(shader.getGlId(),"projection"), false, projectionMatrix);
        glUniformMatrix4fv(glGetUniformLocation(shader.getGlId(),"model"), false, modelMatrix);
        glDrawElements(glType, GL_UNSIGNED_BYTE, indexBuffer);
        posBuffer.endGL();
        colorBuffer.endGL();
        indexBuffer.clear();
        shader.unBind();
    }


    private void updateIndex() {
        vertexCount++;
        if(vertexCount >= sortSize) {
            if(indexBuffer.position() >= indexBuffer.capacity()) {
                indexBuffer = MemoryUtil.memRealloc(indexBuffer,indexBuffer.capacity() * 2);
            }
            indexBuffer.putInt(index++);
            vertexCount = 0;
        }
    }

}
