package ykf.util;

import io.netty.handler.codec.http.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HttpClientHelper {

    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager () {
        SSLContext sslcontext = SSLContexts.createDefault();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" },
                null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());


        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", factory)  // 用来配置支持的协议
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(10);
        return connectionManager;
    }

    // GET 调用
    public static String getAsString(String url, HttpHeaders headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> entry : headers.entries()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        CloseableHttpResponse response1 = httpClient.execute(httpGet);
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            return EntityUtils.toString(entity1, "UTF-8");
        } finally {
            response1.close();
        }
    }


    // POST 调用
    public static String postAsJSON(String url, String json, Map<String, String> headers) throws IOException {
        //
        // json = URLEncoder.encode(json, "UTF-8");
        //
        HttpPost httpPost = new HttpPost(url);
        Set<String> keySet = headers.keySet();
        for (String name : keySet) {
            httpPost.setHeader(name, headers.get(name));
        }
        final String JSON_TYPE = "application/json;charset=UTF-8";
        httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_TYPE);
        StringEntity entity = new StringEntity(json, "UTF-8");

//        httpPost.setHeader("ICK-Content-Encoding", "gzip");
//        httpPost.setEntity(new GzipCompressingEntity(entity));

//        entity.setContentType(JSON_TYPE);
//        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, JSON_TYPE));
        httpPost.setEntity(entity);

        CloseableHttpResponse response1 = null;
        try {
            response1 = httpClient.execute(httpPost);
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            return EntityUtils.toString(entity1, "UTF-8");
        } finally {
            if (null != response1) {
                response1.close();
            }
        }
    }

    public static String postTwo(String scheme, String host, String path, int port) {
        URI uri = null;
        String result = "";
        // 普通参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("flag", "4"));
        params.add(new BasicNameValuePair("meaning", "4"));



        try {
            uri = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(host)
                    .setPort(port)
                    .setPath(path).setParameters(params)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);
        Map jsonObject = new HashMap<String, Object>();

        jsonObject.put("Name", "潘晓婷");
        jsonObject.put("Age", 18);
        jsonObject.put("Gender", "女");
        jsonObject.put("Motto", "姿势要优雅~");

        StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");


        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            PoolingHttpClientConnectionManager connectionManager = HttpClientHelper.poolingHttpClientConnectionManager();
            httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setConnectionManagerShared(true).build();

            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            result = EntityUtils.toString(responseEntity);
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + result);
            }
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static void main(String[] args) throws Exception {

//        String url = "http://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html";
//        String text = HttpClientHelper.getAsString(url);
//        System.out.println("url: " + url + " ; response: \n" + text);

    }
}
