package com.github.khangnt.youtubecrawler;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

import com.github.khangnt.youtubecrawler.exception.HttpClientException;
import com.github.khangnt.youtubecrawler.exception.SignatureDecryptException;
import com.github.khangnt.youtubecrawler.internal.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * {@link SignatureDecipher} implementation using public api:
 * [POST] https://music-app.me/api/public/decrypter
 * Body:
 * - vid        :   youtube video id
 * - player_url :   link to player url (js/swf)
 * - s          :   the encrypted signature
 */
public class MusicAppSignatureDecipher implements SignatureDecipher {
    private static final String ENDPOINT = "https://music-app.me/api/public/decrypter";
    private static final String VID_PARAM = "vid";
    private static final String PLAYER_URL_PARAM = "player_url";
    private static final String ENCRYPTED_SIGNATURE_PARAM = "s";
    private static final String RESULT_KEY = "res";
    private static final String DELIMITER = "|";

    private OkHttpClient okHttpClient;
    private Gson gson;

    public MusicAppSignatureDecipher(OkHttpClient okHttpClient, Gson gson) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }

    @Override
    public List<String> decrypt(String vid, String playerUrl, List<String> encryptedSignature) throws SignatureDecryptException {
        if (Utils.isEmpty(encryptedSignature)) return Collections.emptyList();
        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(new FormBody.Builder()
                        .add(VID_PARAM, vid)
                        .add(PLAYER_URL_PARAM, playerUrl)
                        .add(ENCRYPTED_SIGNATURE_PARAM, Utils.join(encryptedSignature, DELIMITER))
                        .build())
                .build();
        Response response = null;
        Exception exception;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.code() / 100 == 2) {
                //noinspection ConstantConditions
                String body = response.body().string();
                JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
                JsonArray result = jsonObject.getAsJsonArray(RESULT_KEY);
                List<String> decryptedSignature = new ArrayList<>();
                for (JsonElement element : result) {
                    decryptedSignature.add(element.getAsString());
                }
                return decryptedSignature;
            } else {
                exception = new HttpClientException(response.code(), response.message());
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            Utils.closeQuietly(response);
        }
        throw new SignatureDecryptException("Decrypt signature failed", exception, vid);
    }


}
