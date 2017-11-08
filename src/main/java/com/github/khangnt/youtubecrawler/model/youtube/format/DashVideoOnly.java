package com.github.khangnt.youtubecrawler.model.youtube.format;

import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.MP4;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.WEBM;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum DashVideoOnly implements YouTubeFormat {
    /* Dash mp4 video */
    I133(133, MP4, 240),
    I134(134, MP4, 360),
    I135(135, MP4, 480),
    I136(136, MP4, 720),
    I137(137, MP4, 1080),
    I138(138, MP4, 4320),
    I160(160, MP4, 144),
    I212(212, MP4, 480),
    I264(264, MP4, 1440),
    I298(298, MP4, 720),
    I299(299, MP4, 1080),
    I266(266, MP4, 2160),


    /* Dash webm video */
    I167(167, WEBM, 360),
    I168(168, WEBM, 480),
    I169(169, WEBM, 720),
    I170(170, WEBM, 1080),

    I218(218, WEBM, 480),
    I219(219, WEBM, 480),

    I278(278, WEBM, 144),
    I242(242, WEBM, 240),
    I243(243, WEBM, 360),
    I244(244, WEBM, 480),
    I245(245, WEBM, 480),
    I246(246, WEBM, 480),
    I247(247, WEBM, 720),
    I248(248, WEBM, 1080),
    I271(271, WEBM, 1440),
    I272(272, WEBM, 4320),

    I302(302, WEBM, 720),
    I303(303, WEBM, 1080),
    I308(308, WEBM, 1440),
    I313(313, WEBM, 2160),
    I315(315, WEBM, 2160),

    I330(330, WEBM, 144),
    I331(331, WEBM, 240),
    I332(332, WEBM, 360),
    I333(333, WEBM, 480),
    I334(334, WEBM, 720),
    I335(335, WEBM, 1080),
    I336(336, WEBM, 1440),
    I337(337, WEBM, 2160),

    ;

    private int itag;
    private Container container;
    private int videoResolution;

    DashVideoOnly(int itag, Container container, int videoResolution) {
        this.itag = itag;
        this.container = container;
        this.videoResolution = videoResolution;
    }

    @Override
    public int getItag() {
        return itag;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public int getVideoResolution() {
        return videoResolution;
    }

    @Override
    public String toString() {
        return "DashVideoOnly{" +
                "itag=" + itag +
                ", container=" + container +
                ", videoResolution=" + videoResolution +
                '}';
    }
}
