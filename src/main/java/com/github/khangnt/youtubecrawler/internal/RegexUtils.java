package com.github.khangnt.youtubecrawler.internal;

import com.github.khangnt.youtubecrawler.exception.RegexMismatchException;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class RegexUtils {
    public static @Nullable Matcher search(String regex, String source) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        return matcher.find() ? matcher : null;
    }

    public static Matcher search(String regex, String source, String fatalMessage) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        if (!matcher.find()) {
            throw new RegexMismatchException(fatalMessage);
        }
        return matcher;
    }
}
