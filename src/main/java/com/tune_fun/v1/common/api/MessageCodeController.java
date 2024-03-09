package com.tune_fun.v1.common.api;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.i18n.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

@RestController
@RequiredArgsConstructor
public class MessageCodeController {

    private final MessageSourceUtil messageSourceUtil;

    @GetMapping(value = Uris.MESSAGE_CODES, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ErrorCodeResponse> getMessageCodes() {
        return stream(MessageCode.values())
                .collect(
                        toMap(MessageCode::name,
                                m -> new ErrorCodeResponse(
                                        m.getCode(),
                                        messageSourceUtil.getMessage(m.getCode()),
                                        m.getHttpStatus().value())
                        )
                );
    }

    public record ErrorCodeResponse(String code, String message, int status) {
    }

}
