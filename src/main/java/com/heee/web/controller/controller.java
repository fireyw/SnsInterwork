package com.heee.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @RequestMapping(value = "/personalInfo.do")
    public void personalInfo(HttpServletRequest request) throws Exception {
        String token = "AAAANrhDbtNE3s1-IGbG3-YVLKjEYyjey_Bn9OE3PbgzDa8NK5yAj1HzLrM2xm02tdI0LJ6b7QMaH4uqZXZv5LLpFZo";// 네이버 로그인 접근 토큰; 여기에 복사한 토큰값을 넣어줍니다.
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
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}