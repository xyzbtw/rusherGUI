package org.example.theme;

import org.rusherhack.client.api.events.render.EventRender3D;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.util.LinkedList;

public class FpsManager {
    private int fps;
    private final LinkedList<Long> frames = new LinkedList<>();

    @Subscribe
    public void render3D(EventRender3D render3DEvent) {
        long time = System.nanoTime();

        frames.add(time);

        while (true) {
            long f = frames.getFirst();
            final long ONE_SECOND = 1000000L * 1000L;
            if (time - f > ONE_SECOND) frames.remove();
            else break;
        }

        fps = frames.size();
    }

    public int getFPS() {
        return fps;
    }

    public float getFrametime() {
        return 1.0f / fps;
    }
}
