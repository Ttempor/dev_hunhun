package com.hun.api.sys.controller.yg;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private final String dayHead;
    private final List<String> content;

    public Day(String dayHead) {
        this.dayHead = dayHead;
        this.content = new ArrayList<>();
    }

    public void addContent(String content) {
        this.content.add(content);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n");
        result.append(dayHead);
        result.append("\n");
        for (String s : content) {
            result.append(s);
            result.append("\n\n");
        }
        return result.toString();
    }

    public void remove(int index) {
        if (content.size() >= (index + 1)) {
            content.remove(index);
        }
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }
}
