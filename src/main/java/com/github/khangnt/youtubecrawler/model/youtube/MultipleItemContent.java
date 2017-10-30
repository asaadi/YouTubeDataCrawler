package com.github.khangnt.youtubecrawler.model.youtube;

import java.util.List;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class MultipleItemContent extends Content {
    private List<Content> items;

    public MultipleItemContent(String itemType, List<Content> items) {
        super(itemType);
        this.items = items;
    }

    public List<Content> getItems() {
        return items;
    }
}
