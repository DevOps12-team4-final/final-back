package com.bit.boardappbackend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConvertUtils {

    private final  MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

    // JSON 문자열을 DTO로 변환하는 제네릭 메서드
    public <T> T stringToDto(String data, Class<T> className) throws JsonProcessingException {
        return converter.getObjectMapper().readValue(data, className);
    }

    // DTO를 JSON 문자열로 변환하는 메서드
    public String objectToJsonString(Object obj) throws JsonProcessingException {
        return converter.getObjectMapper().writeValueAsString(obj);
    }

}
