package com.bit.finalproject.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class PageSerializer extends JsonSerializer<Page<?>> {
    @Override
    public void serialize(Page<?> page, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("content", page.getContent());
        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeNumberField("size", page.getSize());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeBooleanField("last", page.isLast());
        jsonGenerator.writeEndObject();
    }
}
