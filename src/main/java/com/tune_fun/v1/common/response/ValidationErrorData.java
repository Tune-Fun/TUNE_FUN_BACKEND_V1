package com.tune_fun.v1.common.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ValidationErrorData implements BasePayload {

    @JsonIgnore
    private Map<String, String> errors = new HashMap<>();

    @JsonAnySetter
    public void add(String key, String value) {
        errors.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getMap() {
        return errors;
    }

}
