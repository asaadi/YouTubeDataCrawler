package com.github.khangnt.youtubecrawler;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface SignatureDecipher {
    String decrypt(String playerUrl, String encryptedSignature);
}
