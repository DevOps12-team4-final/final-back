package com.bit.finalproject.service.impl;

import com.bit.finalproject.config.CoolSmsConfiguration;
import com.bit.finalproject.service.CoolSmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CoolSmsServiceImpl implements CoolSmsService {

    private final CoolSmsConfiguration coolSmsConfiguration;

    @Override
    public String sendSms(String tel) {

        try{
            // 랜덤한 6자리 인증번호 생성
            String numStr = generateRandomNumber();

            // APIKey, SecretKey 전달
            Message coolsms = new Message(coolSmsConfiguration.getApiKey(), coolSmsConfiguration.getApiSecret());

            HashMap<String, String> params = new HashMap<>();
            // 수신번호
            params.put("to", tel);
            // 발신번호
            params.put("from", coolSmsConfiguration.getPhoneNumber());
            // 타입
            params.put("type", "sms");
            // 내용
            params.put("text", "인증번호는 [" + numStr + "]입니다.");

            // 메시지 전송
            coolsms.send(params);
            // 생성된 인증번호 반환
            return numStr;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    // 랜덤한 6자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
