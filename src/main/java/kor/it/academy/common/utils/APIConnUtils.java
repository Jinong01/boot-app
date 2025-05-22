package kor.it.academy.common.utils;

import kor.it.academy.common.vo.APIResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class APIConnUtils {

    public APIResponse toGetAPI(String tagetUrl, Map<String , Object> params){
        return getRequest(tagetUrl, params, null);
    }

    public APIResponse toGetAPI(String tagetUrl, Map<String , Object> params, String tokenKey){
        return getRequest(tagetUrl, params, tokenKey);
    }

    private APIResponse getRequest(String targetUrl, Map<String , Object> dataParams, String tokenKey){
        String responseBody = "";
        StringBuilder queryString = new StringBuilder();

        boolean isFirst = true;

        //queryString 만들기
        if (dataParams != null && !dataParams.isEmpty()){
            //map이 keySet 을 추출
            Set<String> paramSet = dataParams.keySet();
            //keySet을 loop
            for (String key : paramSet){
                if (isFirst){
                    queryString.append("?");
                    isFirst = false;
                }else {
                    queryString.append("&");
                }
                //한글 깨짐을 방지하기 위해서 URLEncoder.encode 처리
                queryString.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
                queryString.append("=");
                queryString.append(URLEncoder.encode(dataParams.get(key).toString(), StandardCharsets.UTF_8));
            }
        }

        if (queryString.length() > 0){
            targetUrl = targetUrl + queryString;
        }

        HttpGet getRequest = new HttpGet(targetUrl);

        if(tokenKey != null){
            getRequest.addHeader("Authorization","Bearer" + tokenKey);
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(60, TimeUnit.SECONDS))
                .build();
        APIResponse response = null;

        //Http 통신을 조금더 디테일하게 할 수 있는 CloseableHttpClient 클래스이다
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig).build();

             CloseableHttpResponse httpResponse = client.execute(getRequest);
        ){
            //데이터를 가져올 때 String 으로 변환하면서 인코딩은 UTF-8 처리
            responseBody = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);

            if (httpResponse.getCode() != HttpStatus.SC_OK){
                throw new Exception("Error API GET url : " + targetUrl + ", response :" + responseBody);
            }

            return APIResponse.builder()
                    .resultCode(httpResponse.getCode())
                    .data(responseBody)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            response = APIResponse.builder()
                    .resultCode(500)
                    .data("{}")
                    .build();

        }

        return response;
    }
}
