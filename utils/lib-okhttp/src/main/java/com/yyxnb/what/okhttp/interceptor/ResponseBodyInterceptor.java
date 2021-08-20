package com.yyxnb.what.okhttp.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/06/08
 * 描    述：ResponseBodyInterceptor
 * ================================================
 */
public abstract class ResponseBodyInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (null != responseBody) {
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            if ("gzip".equals(response.headers().get("Content-Encoding"))) {
                GzipSource gzippedResponseBody = new GzipSource(buffer.clone());
                buffer = new Buffer();
                buffer.writeAll(gzippedResponseBody);
            }

            MediaType contentType = responseBody.contentType();
            Charset charset;
            if (null == contentType || null == contentType.charset(StandardCharsets.UTF_8)) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }

            if (null != charset && 0L != contentLength) {
                return intercept(response, url, buffer.clone().readString(charset));
            }
        }
        return response;
    }

    abstract Response intercept(@NonNull Response response, String url, String body);
}