package com.github.khangnt.youtubecrawler.internal;

import com.github.khangnt.youtubecrawler.Lazy;
import com.github.khangnt.youtubecrawler.SignatureDecipher;
import com.github.khangnt.youtubecrawler.exception.BadExtractorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Khang NT on 11/28/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SignatureHolder {
    private SignatureDecipher signatureDecipher;
    private String videoId;

    private List<String> encryptedSignature = new ArrayList<>();
    private Lazy<List<String>> decryptSignatureLazy = null;
    private boolean invalidate = true;
    private String playerUrl = null;

    public SignatureHolder(SignatureDecipher signatureDecipher, String videoId, String playerUrl) {
        this.signatureDecipher = signatureDecipher;
        this.videoId = videoId;
        this.playerUrl = checkPlayerUrl(playerUrl);
    }

    public synchronized void addEncryptedSignature(String s) {
        this.encryptedSignature.add(s);
        invalidate = true;
    }

    private String checkPlayerUrl(String playerUrl) {
        if (Utils.isEmpty(playerUrl)) {
            throw new BadExtractorException("playerUrl == null or empty", videoId);
        }
        return playerUrl;
    }

    private synchronized Lazy<List<String>> getDecryptSignatureLazy() {
        if (invalidate) {
            invalidate = false;
            final List<String> finalSignatureList = Collections.unmodifiableList(encryptedSignature);
            decryptSignatureLazy = new Lazy<>(() -> signatureDecipher.decrypt(videoId, playerUrl, finalSignatureList));
        }
        return decryptSignatureLazy;
    }

    public synchronized String getDecryptSignature(String theEncrypted) {
        int index = encryptedSignature.indexOf(theEncrypted);
        if (index > -1) {
            return getDecryptSignatureLazy().get().get(index);
        }
        return theEncrypted;
    }
}
