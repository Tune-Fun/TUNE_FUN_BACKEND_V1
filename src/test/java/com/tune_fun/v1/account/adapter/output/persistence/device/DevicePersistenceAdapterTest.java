package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountMapperImpl;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.adapter.output.persistence.AccountRepository;
import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountRepository;
import com.tune_fun.v1.account.domain.behavior.DeleteDevice;
import com.tune_fun.v1.account.domain.behavior.SaveDevice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@Execution(CONCURRENT)
class DevicePersistenceAdapterTest {
    /**
     * Method under test: {@link DevicePersistenceAdapter#saveDevice(SaveDevice)}
     */
    @Test
    void testSaveDevice() {
        // Arrange
        DeviceRepository deviceRepository = mock(DeviceRepository.class);
        when(deviceRepository.save(Mockito.any())).thenReturn(new DeviceJpaEntity());
        Optional<DeviceJpaEntity> ofResult = Optional.of(new DeviceJpaEntity());
        when(deviceRepository.findByDeviceToken(Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        DevicePersistenceAdapter devicePersistenceAdapter = new DevicePersistenceAdapter(accountPersistenceAdapter,
                deviceRepository, new DeviceMapperImpl());

        // Act
        devicePersistenceAdapter.saveDevice(new SaveDevice("janedoe", "ABC123", "ABC123"));

        // Assert
        verify(deviceRepository).findByDeviceToken(eq("ABC123"));
        verify(deviceRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link DevicePersistenceAdapter#saveDevice(SaveDevice)}
     */
    @Test
    void testSaveDevice2() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        DeviceRepository deviceRepository = mock(DeviceRepository.class);
        when(deviceRepository.save(Mockito.any())).thenReturn(new DeviceJpaEntity());
        Optional<DeviceJpaEntity> emptyResult = Optional.empty();
        when(deviceRepository.findByDeviceToken(Mockito.any())).thenReturn(emptyResult);
        DevicePersistenceAdapter devicePersistenceAdapter = new DevicePersistenceAdapter(accountPersistenceAdapter,
                deviceRepository, new DeviceMapperImpl());

        // Act
        devicePersistenceAdapter.saveDevice(new SaveDevice("janedoe", "ABC123", "ABC123"));

        // Assert
        verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
        verify(deviceRepository).findByDeviceToken(eq("ABC123"));
        verify(deviceRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link DevicePersistenceAdapter#saveDevice(SaveDevice)}
     */
    @Test
    void testSaveDevice3() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(emptyResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        DeviceRepository deviceRepository = mock(DeviceRepository.class);
        Optional<DeviceJpaEntity> emptyResult2 = Optional.empty();
        when(deviceRepository.findByDeviceToken(Mockito.any())).thenReturn(emptyResult2);
        DevicePersistenceAdapter devicePersistenceAdapter = new DevicePersistenceAdapter(accountPersistenceAdapter,
                deviceRepository, new DeviceMapperImpl());

        // Act
        devicePersistenceAdapter.saveDevice(new SaveDevice("janedoe", "ABC123", "ABC123"));

        // Assert
        verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
        verify(deviceRepository).findByDeviceToken(eq("ABC123"));
    }

    /**
     * Method under test: {@link DevicePersistenceAdapter#delete(DeleteDevice)}
     */
    @Test
    void testDelete() {
        // Arrange
        DeviceRepository deviceRepository = mock(DeviceRepository.class);
        doNothing().when(deviceRepository).delete(Mockito.any());
        Optional<DeviceJpaEntity> ofResult = Optional.of(new DeviceJpaEntity());
        when(deviceRepository.findByFcmTokenOrDeviceToken(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        DevicePersistenceAdapter devicePersistenceAdapter = new DevicePersistenceAdapter(accountPersistenceAdapter,
                deviceRepository, new DeviceMapperImpl());

        // Act
        devicePersistenceAdapter.delete(new DeleteDevice("janedoe", "ABC123", "ABC123"));

        // Assert
        verify(deviceRepository).findByFcmTokenOrDeviceToken(eq("janedoe"), eq("ABC123"), eq("ABC123"));
        verify(deviceRepository).delete(Mockito.any());
    }

    /**
     * Method under test:
     * {@link DevicePersistenceAdapter#findByFcmTokenOrDeviceToken(String, String, String)}
     */
    @Test
    void testFindByFcmTokenOrDeviceToken() {
        // Arrange
        DeviceRepository deviceRepository = mock(DeviceRepository.class);
        Optional<DeviceJpaEntity> ofResult = Optional.of(new DeviceJpaEntity());
        when(deviceRepository.findByFcmTokenOrDeviceToken(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        // Act
        Optional<DeviceJpaEntity> actualFindByFcmTokenOrDeviceTokenResult = (new DevicePersistenceAdapter(
                accountPersistenceAdapter, deviceRepository, new DeviceMapperImpl())).findByFcmTokenOrDeviceToken("janedoe",
                "ABC123", "ABC123");

        // Assert
        verify(deviceRepository).findByFcmTokenOrDeviceToken(eq("janedoe"), eq("ABC123"), eq("ABC123"));
        assertSame(ofResult, actualFindByFcmTokenOrDeviceTokenResult);
    }
}
