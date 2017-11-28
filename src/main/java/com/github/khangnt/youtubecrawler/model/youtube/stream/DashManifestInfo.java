package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/26/17.
 * Email: khang.neon.1997@gmail.com
 */

public class DashManifestInfo {
    private String url;
    private String content;

    public DashManifestInfo(Builder builder) {
        this(builder.url, builder.content);
    }

    public DashManifestInfo(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "DashManifestInfo{" +
                "url='" + url + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private String content;

        Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public DashManifestInfo build() {
            return new DashManifestInfo(this);
        }
    }
}
