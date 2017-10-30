package com.github.khangnt.youtubecrawler.model.youtube;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface Continuation {

    @Nullable String getContinuationToken();

    @Nullable String getClickTrackingParams();

}
