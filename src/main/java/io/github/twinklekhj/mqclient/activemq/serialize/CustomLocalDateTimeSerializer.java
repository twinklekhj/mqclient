package io.github.twinklekhj.mqclient.activemq.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    protected CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDateTime.format(DATE_TIME_FORMATTER));
    }
}