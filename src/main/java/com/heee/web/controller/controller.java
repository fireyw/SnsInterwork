package com.heee.web.controller;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NESOY on 2017-02-04.
 */
@Controller
public class controller {

    @RequestMapping(value = "/index.do")
    public String test(HttpSession session) {
        return "index";
    }

    @RequestMapping(value = "/naverLogin.do")
    public String naverLogin(HttpSession session, HttpServletRequest request) {
        return "naverLogin";
    }

    @RequestMapping(value = "/naverCallback.do")
    public String naverCallback(HttpSession session, HttpServletRequest request) {
        return "naverCallback";
    }

    @RequestMapping(value = "/naverPersonalInfo.do")
    @ResponseBody
    public String naverPersonalInfo(HttpServletRequest request) throws Exception {
        String token = "AAAANhMg2DbRmPg_8L7-X3w2O6cs4CW9rN93LX6oTmfbN3vRKPpgDNBQiVpwLZiA6w0fedtjfPrU181yRvpkbahDi2M"; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        String newJSON = jsonObject.getString("message");
        System.out.println(newJSON.toString());

        return responseBody;

//        JSONArray cars = (JSONArray) jsonObject.get("cars");
//        Iterator<String> iterator = cars.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }


    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }


    @RequestMapping(value = "/personalInfo.do")
    public void personalInfo(HttpServletRequest request) throws Exception {
        String token = "AAAANhMg2DbRmPg_8L7-X3w2O6cs4CW9rN93LX6oTmfbN3vRKPpgDNBQiVpwLZiA6w0fedtjfPrU181yRvpkbahDi2M";// 네이버 로그인 접근 토큰; 여기에 복사한 토큰값을 넣어줍니다.
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println("결과값 : " + response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}