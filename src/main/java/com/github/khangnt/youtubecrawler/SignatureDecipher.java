package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.exception.SignatureDecryptException;

import java.util.List;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface SignatureDecipher {
    List<String> decrypt(String vid, String playerUrl, List<String> encryptedSignature) throws SignatureDecryptException;
}
