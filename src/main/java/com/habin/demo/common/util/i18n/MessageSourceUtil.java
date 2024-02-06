package com.habin.demo.common.util.i18n;

import com.habin.demo.common.response.MessageCode;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class MessageSourceUtil {

    private final MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    public String getMessage(MessageCode code) {
        return getMessage(code.getCode());
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

}
