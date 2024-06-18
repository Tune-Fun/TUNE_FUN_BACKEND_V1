package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.stereotype.PersistenceAdapter;
import com.tune_fun.v1.interaction.application.port.output.SaveNotificationHistoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationHistoryPersistenceAdapter implements SaveNotificationHistoryPort {

    private final NotificationHistoryRepository notificationHistoryRepository;

}
