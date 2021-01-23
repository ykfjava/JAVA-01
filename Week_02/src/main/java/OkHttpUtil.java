package main.java;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    // 缓存客户端实例
    public static OkHttpClient client = new OkHttpClient();

    // GET 调用
    public static String getAsString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // POSt 调用
    public static String post(String url) throws IOException {
        final String[] result = {""};
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Post";
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, requestBody)).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                result[0] = e.getLocalizedMessage();
                logger.error("onFailure: " + e.getMessage(), e);

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                logger.info(response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    logger.info(headers.name(i) + ":" + headers.value(i));
                }
                logger.info("onResponse: " + response.body().string());
                result[0] = response.body().string();
            }
        });
        return result[0];
    }


    public static void main(String[] args) throws Exception {

        String url = "https://square.github.io/okhttp/";
        String text = OkHttpUtil.getAsString(url);
        System.out.println("url: " + url + " ; response: \n" + text);

        // 清理资源
        OkHttpUtil.client = null;
    }
}
