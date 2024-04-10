package com.tune_fun.v1.common.temp;

import com.tune_fun.v1.account.application.port.output.DeleteAccountPort;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TempController {

    private final DeleteAccountPort deleteAccountPort;
    private final ResponseMapper responseMapper;

    @DeleteMapping(value = Uris.TEMP_ACCOUNT_RESET)
    public ResponseEntity<Response<?>> resetAccount() {
        deleteAccountPort.deleteAll();
        return responseMapper.ok();
    }
}
