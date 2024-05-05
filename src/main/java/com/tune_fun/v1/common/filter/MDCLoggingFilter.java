package com.tune_fun.v1.common.filter;

import com.tune_fun.v1.common.util.StringUtil;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @see <a href="https://0soo.tistory.com/246?category=643528">Logback + MDC를 이용한 request 로깅</a>
 * @see <a href="https://hudi.blog/slf4j-mapped-diagnotics-context">slf4j MDC(Mapped Diagnostics Context)를 사용하여 로그에 맥락 더하기</a>
 */
@Component
@Order(HIGHEST_PRECEDENCE)
class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        MDC.put("CORRELATION_ID", StringUtil.uuid());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

}
