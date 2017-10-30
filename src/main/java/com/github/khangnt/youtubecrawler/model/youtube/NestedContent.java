package com.github.khangnt.youtubecrawler.model.youtube;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class NestedContent extends Content {
    private Content subContent;

    public NestedContent(String itemType, Content subContent) {
        super(itemType);
        this.subContent = subContent;
    }

    public Content getSubContent() {
        return subContent;
    }

}
