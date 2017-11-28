package com.github.khangnt.youtubecrawler.model.youtube.format;

import com.github.khangnt.youtubecrawler.Const;

import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.MP4;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.WEBM;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum DashVideoOnly implements YouTubeFormat {
    /* Dash mp4 video */
    I133("133", MP4, "h264", 240),
    I134("134", MP4, "h264", 360),
    I135("135", MP4, "h264", 480),
    I136("136", MP4, "h264", 720),
    I137("137", MP4, "h264", 1080),
    I138("138", MP4, "h264", 4320),
    I160("160", MP4, "h264", 144),
    I212("212", MP4, "h264", 480),
    I264("264", MP4, "h264", 1440),
    I298("298", MP4, "h264", 720),
    I299("299", MP4, "h264", 1080),
    I266("266", MP4, "h264", 2160),


    /* Dash webm video */
    I167("167", WEBM, "vp8", 360, 640),
    I168("168", WEBM, "vp8", 480, 854),
    I169("169", WEBM, "vp8", 720, 1280),
    I170("170", WEBM, "vp8", 1080, 1920),

    I218("218", WEBM, "vp8", 480, 854),
    I219("219", WEBM, "vp8", 480, 854),

    I278("278", WEBM, "vp9", 144),
    I242("242", WEBM, "vp9", 240),
    I243("243", WEBM, "vp9", 360),
    I244("244", WEBM, "vp9", 480),
    I245("245", WEBM, "vp9", 480),
    I246("246", WEBM, "vp9", 480),
    I247("247", WEBM, "vp9", 720),
    I248("248", WEBM, "vp9", 1080),
    I271("271", WEBM, "vp9", 1440),
    I272("272", WEBM, "vp9", 4320),

    I302("302", WEBM, "vp9", 720),
    I303("303", WEBM, "vp9", 1080),
    I308("308", WEBM, "vp9", 1440),
    I313("313", WEBM, "vp9", 2160),
    I315("315", WEBM, "vp9", 2160),

    I330("330", WEBM, "vp9", 144),
    I331("331", WEBM, "vp9", 240),
    I332("332", WEBM, "vp9", 360),
    I333("333", WEBM, "vp9", 480),
    I334("334", WEBM, "vp9", 720),
    I335("335", WEBM, "vp9", 1080),
    I336("336", WEBM, "vp9", 1440),
    I337("337", WEBM, "vp9", 2160),

    ;

    private String itag;
    private Container container;
    private String codec;
    private int height;
    private int width;

    DashVideoOnly(String itag, Container container, String codec, int height) {
        this(itag, container, codec, height, Const.UNKNOWN_VALUE);
    }

    DashVideoOnly(String itag, Container container, String codec, int height, int width) {
        this.itag = itag;
        this.container = container;
        this.codec = codec;
        this.height = height;
        this.width = width;
    }

    @Override
    public String getItag() {
        return itag;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public String getCodec() {
        return codec;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "DashVideoOnly{" +
                "itag='" + itag + '\'' +
                ", container=" + container +
                ", codec='" + codec + '\'' +
                ", height=" + height +
                ", width=" + width +
                "} ";
    }
}
