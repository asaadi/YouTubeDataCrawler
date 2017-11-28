package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/28/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SegmentBaseData {
    private Range index;
    private Range init;

    public SegmentBaseData(Range index, Range init) {
        this.index = index;
        this.init = init;
    }

    public Range getIndex() {
        return index;
    }

    public Range getInit() {
        return init;
    }

    @Override
    public String toString() {
        return "SegmentBase{" +
                "index=" + index +
                ", init=" + init +
                '}';
    }

}
