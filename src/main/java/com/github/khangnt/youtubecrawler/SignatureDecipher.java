package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.exception.SignatureDecryptException;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface SignatureDecipher {
    String decrypt(String vid, String playerUrl, String encryptedSignature) throws SignatureDecryptException;
}
