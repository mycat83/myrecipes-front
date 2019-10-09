package io.myrecipes.front.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class RestTemplateHelperImpl implements RestTemplateHelper {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestTemplateHelperImpl(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    // TODO: 반환값이 에러메시지인 경우에 대한 처리 ApiErrorInfo에 대한 매핑 처리
    public <T> T getForObject(Class<T> clazz, String url, Object... uriVariables) {
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class, uriVariables);
        JavaType type =this. objectMapper.getTypeFactory().constructType(clazz);
        T result = null;

        try {
            result = this.objectMapper.readValue(response.getBody(), type);
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return result;
    }

    @Override
    public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables) {
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class, uriVariables);
        CollectionType type = this.objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        List<T> result = null;

        try {
            result = this.objectMapper.readValue(response.getBody(), type);
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return result;
    }
}
