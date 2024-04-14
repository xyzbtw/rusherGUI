package dev.sf.ui.utils.buffers;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class BufferId {
    public static final int
            SINGLE = 1,
            VEC2 = 2,
            VEC3 = 3,
            VEC4 = 4;


    private ByteBuffer buffer;//сам буфер

    private int bufferId = -1;//имя буфера для GL

    private final int
            byteWeight, // вес в байта
            size, //размер буфера
            attributeIndex, //индекс атрибута
            attributeSize, //размер от 1 до 4
            glType; //тип буфера для GL
    private final List<Object> values; //лист значений

    public BufferId(int byteWeight, int glType, int size, int attributeIndex, int attributeSize) {
        this.byteWeight = byteWeight;
        this.glType = glType;
        this.attributeSize = attributeSize;
        this.attributeIndex = attributeIndex;
        this.size = size;
        this.buffer = MemoryUtil.memAlloc(size * byteWeight);//аллоцируем буфер
        this.values = new ArrayList<>();
    }


    public void put(Object value) {
        if(value == null) throw new IllegalStateException("Value cannot be null");
        values.add(value);
    }

    public void clear() {
        values.clear();
    }

    public void end() {
        RenderSystem.assertOnRenderThread();//проверка на поток, ибо гл крашит, если не рендер поток
        if (bufferId == -1) {
            bufferId = GL20.glGenBuffers();//генерируем имя буфера

        }
        buffer.clear();//Очищаем. Почему именно этот метод? Ответ: почитай документацию к java.nio.ByteBuffer
        if (buffer.capacity() < values.size() * byteWeight) {//чтобы буфер не переполнялся
            buffer = MemoryUtil.memRealloc(buffer, values.size() * byteWeight);
          //  Global.logger.warn("Reallocated a new {} for ByteBufferId({}): {} bytes({} * {})", buffer.getClass().getName(), hashCode(), values.size() * byteWeight, values.size(), byteWeight);
        }
        //заполняем буфер
        for (Object value : values) {
            if (value instanceof Byte) {
                buffer.put((Byte) value);
            } else if (value instanceof Short) {
                buffer.putShort((Short) value);
            } else if (value instanceof Integer) {
                buffer.putInt((Integer) value);
            } else if (value instanceof Float) {
                buffer.putFloat((Float) value);
            } else if (value instanceof Double) {
                buffer.putDouble((Double) value);
            } else {
                throw new IllegalStateException("Unknown value type: " + value.getClass().getName());
            }
        }
        buffer.flip();//готовим для чтения GL'ом


        clear();//очищаем
    }

    public void loadData() {
        RenderSystem.assertOnRenderThread();//проверка на поток, ибо гл крашит, если не рендер поток
        glEnableVertexAttribArray(attributeIndex);// включаем атрибут
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);// привязываем буфер
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);// задаем данные привязанному буферу
        glVertexAttribPointer(attributeIndex, attributeSize, glType, false, 0, 0);// заполняем данными, которые до задали
    }

    public void endGL() {
        RenderSystem.assertOnRenderThread();//проверка на поток, ибо гл крашит, если не рендер поток
        glBindBuffer(GL_ARRAY_BUFFER, 0);// отвязываем буфер
        glDisableVertexAttribArray(attributeIndex);// отключаем атрибут
    }

    public static <T> boolean compareLists(List<T> list, List<T> list2) {
        if (list.size() != list2.size()) {
            return false; // Размеры списков разные, данные не могут совпадать
        }

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(list2.get(i))) {
                return false; // Найдено несовпадение данных на позиции i
            }
        }

        return true; // Все данные и их позиции совпадают
    }

    public int getGlType() {
        return glType;
    }

    public int getSize() {
        return size;
    }

    public int getBufferId() {
        return bufferId;
    }
}
