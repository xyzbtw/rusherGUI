package dev.sf.ui.utils.shaders;

import com.mojang.blaze3d.systems.RenderSystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL30.*;

public class ShaderUtil {

    public static final Pattern IMPORT = Pattern.compile("#include\\s*<(?<name>.+)>");
    private static final String MULTI_LINE_COMMENT_PATTERN = "/\\*(?:[^*]|\\*+[^*/])*\\*+/";
    private static final String SINGLE_LINE_COMMENT_PATTERN = "//\\V*";


    public static Shader load(String name, String... attributes) {
        RenderSystem.assertOnRenderThread();
        try {
            String fragSource = getSource(name, Dir.FRAG);
            String vertSource = getSource(name, Dir.VERT);

            int frag = glCreateShader(GL_FRAGMENT_SHADER);
            int vert = glCreateShader(GL_VERTEX_SHADER);

            glShaderSource(frag, fragSource);
            glShaderSource(vert, vertSource);

            glCompileShader(frag);
            String log = glGetShaderInfoLog(frag);
            if (!log.isEmpty()) {
                System.out.println(log);
            }
            int program = glCreateProgram();
            glAttachShader(program, frag);
            glAttachShader(program, vert);
            for (String attribute : attributes) {
                int index = Integer.parseInt(attribute.split("/")[1]);
                String attributeName = attribute.split("/")[0];
                glBindAttribLocation(program, index, attributeName);
            }

            glLinkProgram(program);
            log = glGetProgramInfoLog(program);
            if (!log.isEmpty()) {
                System.out.println(log);
            }

            return new Shader(program, name, name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String addImports(String source,String name) throws IOException {
        Matcher matcher = IMPORT.matcher(source);
        while (matcher.find()) {
            if(matcher.group("name").equals(name)) continue;
            String sourceLib = getSource(matcher.group("name"), Dir.LIB);//recursive
            source = source.substring(0, Math.max(matcher.start() - 1, 0))  + source.substring(matcher.end());

            source = source.substring(0, matcher.start()) +
                    sourceLib +
                    source.substring(matcher.start());

        }
        return source;
    }

    private static String getSource(String name, Dir dir) throws IOException {
        String source = new String(ShaderUtil.class.getResourceAsStream("/dev/sf/shaders/" + dir.path + "/" + name + "." + dir.type).readAllBytes(), StandardCharsets.UTF_8);
        source = source.replaceAll(MULTI_LINE_COMMENT_PATTERN, "");
        source = source.replaceAll(SINGLE_LINE_COMMENT_PATTERN, "");
        source = addImports(source, name);
        return source;
    }


    private enum Dir {
        FRAG("frag", "frag"), VERT("vert", "vert"), LIB("lib", "glsl");
        private String path;
        private String type;

        private Dir(String path, String type) {
            this.path = path;
            this.type = type;
        }
    }

}
