package com.tune_fun.v1.account.adapter.output.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountRepository;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveOAuth2Account;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;

class AccountPersistenceAdapterTest {
  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#loadCustomUserByUsername(String)}
   */
  @Test
  void testLoadCustomUserByUsername() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    NotificationConfig notificationConfig = new NotificationConfig();
    ArrayList<Role> roles = new ArrayList<>();
    LocalDateTime lastLoginAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime lastLogoutAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime emailVerifiedAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime withdrawalAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210",
            "janedoe", "iloveyou", "jane.doe@example.org", "Nickname", notificationConfig, roles, lastLoginAt, lastLogoutAt,
            emailVerifiedAt, withdrawalAt, LocalDate.of(1970, 1, 1).atStartOfDay(), true, true, true, true));
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<User> actualLoadCustomUserByUsernameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).loadCustomUserByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    User getResult = actualLoadCustomUserByUsernameResult.get();
    assertEquals("iloveyou", getResult.getPassword());
    assertEquals("janedoe", getResult.getUsername());
    assertTrue(getResult.getAuthorities().isEmpty());
    assertTrue(getResult.isAccountNonExpired());
    assertTrue(getResult.isAccountNonLocked());
    assertTrue(getResult.isCredentialsNonExpired());
    assertTrue(getResult.isEnabled());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#loadCustomUserByUsername(String)}
   */
  @Test
  void testLoadCustomUserByUsername2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<User> actualLoadCustomUserByUsernameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).loadCustomUserByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    assertFalse(actualLoadCustomUserByUsernameResult.isPresent());
    assertSame(emptyResult, actualLoadCustomUserByUsernameResult);
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#loadCustomUserByUsername(String)}
   */
  @Test
  void testLoadCustomUserByUsername3() {
    // Arrange
    ArrayList<Role> roles = new ArrayList<>();
    roles.add(Role.CLIENT_0);
    NotificationConfig notificationConfig = new NotificationConfig();
    LocalDateTime lastLoginAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime lastLogoutAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime emailVerifiedAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime withdrawalAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210",
            "janedoe", "iloveyou", "jane.doe@example.org", "Nickname", notificationConfig, roles, lastLoginAt, lastLogoutAt,
            emailVerifiedAt, withdrawalAt, LocalDate.of(1970, 1, 1).atStartOfDay(), true, true, true, true));
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<User> actualLoadCustomUserByUsernameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).loadCustomUserByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    User getResult = actualLoadCustomUserByUsernameResult.get();
    assertEquals("iloveyou", getResult.getPassword());
    assertEquals("janedoe", getResult.getUsername());
    assertEquals(1, getResult.getAuthorities().size());
    assertTrue(getResult.isAccountNonExpired());
    assertTrue(getResult.isAccountNonLocked());
    assertTrue(getResult.isCredentialsNonExpired());
    assertTrue(getResult.isEnabled());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#loadCustomUserByUsername(String)}
   */
  @Test
  void testLoadCustomUserByUsername4() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    NotificationConfig notificationConfig = new NotificationConfig();
    ArrayList<Role> roles = new ArrayList<>();
    LocalDateTime lastLoginAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime lastLogoutAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime emailVerifiedAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime withdrawalAt = LocalDate.of(1970, 1, 1).atStartOfDay();
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210",
            "janedoe", "iloveyou", "jane.doe@example.org", "Nickname", notificationConfig, roles, lastLoginAt, lastLogoutAt,
            emailVerifiedAt, withdrawalAt, LocalDate.of(1970, 1, 1).atStartOfDay(), false, true, true, true));
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<User> actualLoadCustomUserByUsernameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).loadCustomUserByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    User getResult = actualLoadCustomUserByUsernameResult.get();
    assertEquals("iloveyou", getResult.getPassword());
    assertEquals("janedoe", getResult.getUsername());
    assertFalse(getResult.isAccountNonExpired());
    assertTrue(getResult.getAuthorities().isEmpty());
    assertTrue(getResult.isAccountNonLocked());
    assertTrue(getResult.isCredentialsNonExpired());
    assertTrue(getResult.isEnabled());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#currentAccountInfo(String)}
   */
  @Test
  void testCurrentAccountInfo() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<CurrentAccount> actualCurrentAccountInfoResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).currentAccountInfo("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    CurrentAccount getResult = actualCurrentAccountInfoResult.get();
    assertNull(getResult.email());
    assertNull(getResult.nickname());
    assertNull(getResult.username());
    assertNull(getResult.uuid());
    assertNull(getResult.createdAt());
    assertNull(getResult.emailVerifiedAt());
    assertTrue(getResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#currentAccountInfo(String)}
   */
  @Test
  void testCurrentAccountInfo2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional
            .of(new AccountJpaEntity(mock(AccountJpaEntity.AccountJpaEntityBuilder.class)));
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<CurrentAccount> actualCurrentAccountInfoResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).currentAccountInfo("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    CurrentAccount getResult = actualCurrentAccountInfoResult.get();
    assertNull(getResult.email());
    assertNull(getResult.nickname());
    assertNull(getResult.username());
    assertNull(getResult.uuid());
    assertNull(getResult.createdAt());
    assertNull(getResult.emailVerifiedAt());
    assertTrue(getResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#currentAccountInfo(String)}
   */
  @Test
  void testCurrentAccountInfo3() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<CurrentAccount> actualCurrentAccountInfoResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).currentAccountInfo("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    assertFalse(actualCurrentAccountInfoResult.isPresent());
    assertSame(emptyResult, actualCurrentAccountInfoResult);
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByUsername(String)}
   */
  @Test
  void testRegisteredAccountInfoByUsername() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByUsernameResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    RegisteredAccount getResult = actualRegisteredAccountInfoByUsernameResult.get();
    assertNull(getResult.password());
    assertNull(getResult.username());
    assertTrue(getResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByUsername(String)}
   */
  @Test
  void testRegisteredAccountInfoByUsername2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByUsernameResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    assertFalse(actualRegisteredAccountInfoByUsernameResult.isPresent());
    assertSame(emptyResult, actualRegisteredAccountInfoByUsernameResult);
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByEmail(String)}
   */
  @Test
  void testRegisteredAccountInfoByEmail() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByEmailResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByEmail("jane.doe@example.org");

    // Assert
    verify(accountRepository).findActive(isNull(), eq("jane.doe@example.org"), isNull());
    RegisteredAccount getResult = actualRegisteredAccountInfoByEmailResult.get();
    assertNull(getResult.password());
    assertNull(getResult.username());
    assertTrue(getResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByEmail(String)}
   */
  @Test
  void testRegisteredAccountInfoByEmail2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByEmailResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByEmail("jane.doe@example.org");

    // Assert
    verify(accountRepository).findActive(isNull(), eq("jane.doe@example.org"), isNull());
    assertFalse(actualRegisteredAccountInfoByEmailResult.isPresent());
    assertSame(emptyResult, actualRegisteredAccountInfoByEmailResult);
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByNickname(String)}
   */
  @Test
  void testRegisteredAccountInfoByNickname() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByNicknameResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByNickname("Nickname");

    // Assert
    verify(accountRepository).findActive(isNull(), isNull(), eq("Nickname"));
    RegisteredAccount getResult = actualRegisteredAccountInfoByNicknameResult.get();
    assertNull(getResult.password());
    assertNull(getResult.username());
    assertTrue(getResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#registeredAccountInfoByNickname(String)}
   */
  @Test
  void testRegisteredAccountInfoByNickname2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<RegisteredAccount> actualRegisteredAccountInfoByNicknameResult = (new AccountPersistenceAdapter(
            accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .registeredAccountInfoByNickname("Nickname");

    // Assert
    verify(accountRepository).findActive(isNull(), isNull(), eq("Nickname"));
    assertFalse(actualRegisteredAccountInfoByNicknameResult.isPresent());
    assertSame(emptyResult, actualRegisteredAccountInfoByNicknameResult);
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#recordLastLoginAt(String)}
   */
  @Test
  void testRecordLastLoginAt() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .recordLastLoginAt("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#recordLastLoginAt(String)}
   */
  @Test
  void testRecordLastLoginAt2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .recordLastLoginAt("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#recordEmailVerifiedAt(String)}
   */
  @Test
  void testRecordEmailVerifiedAt() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .recordEmailVerifiedAt("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#recordEmailVerifiedAt(String)}
   */
  @Test
  void testRecordEmailVerifiedAt2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .recordEmailVerifiedAt("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#loadAccountByUsername(String)}
   */
  @Test
  void testLoadAccountByUsername() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<AccountJpaEntity> actualLoadAccountByUsernameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).loadAccountByUsername("janedoe");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    assertSame(ofResult, actualLoadAccountByUsernameResult);
  }

  /**
   * Method under test: {@link AccountPersistenceAdapter#findByEmail(String)}
   */
  @Test
  void testFindByEmail() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<AccountJpaEntity> actualFindByEmailResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).findByEmail("jane.doe@example.org");

    // Assert
    verify(accountRepository).findActive(isNull(), eq("jane.doe@example.org"), isNull());
    assertSame(ofResult, actualFindByEmailResult);
  }

  /**
   * Method under test: {@link AccountPersistenceAdapter#findByNickname(String)}
   */
  @Test
  void testFindByNickname() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    Optional<AccountJpaEntity> actualFindByNicknameResult = (new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl())).findByNickname("Nickname");

    // Assert
    verify(accountRepository).findActive(isNull(), isNull(), eq("Nickname"));
    assertSame(ofResult, actualFindByNicknameResult);
  }

  /**
   * Method under test: {@link AccountPersistenceAdapter#saveAccount(SaveAccount)}
   */
  @Test
  void testSaveAccount() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
    AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl());

    // Act
    CurrentAccount actualSaveAccountResult = accountPersistenceAdapter
            .saveAccount(new SaveAccount("01234567-89AB-CDEF-FEDC-BA9876543210", "janedoe", "iloveyou",
                    "jane.doe@example.org", "Nickname", true, true, true));

    // Assert
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
    assertNull(actualSaveAccountResult.email());
    assertNull(actualSaveAccountResult.nickname());
    assertNull(actualSaveAccountResult.username());
    assertNull(actualSaveAccountResult.uuid());
    assertNull(actualSaveAccountResult.createdAt());
    assertNull(actualSaveAccountResult.emailVerifiedAt());
    assertTrue(actualSaveAccountResult.roles().isEmpty());
  }

  /**
   * Method under test: {@link AccountPersistenceAdapter#saveAccount(SaveAccount)}
   */
  @Test
  void testSaveAccount2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(null);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
    AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl());

    // Act
    CurrentAccount actualSaveAccountResult = accountPersistenceAdapter
            .saveAccount(new SaveAccount("01234567-89AB-CDEF-FEDC-BA9876543210", "janedoe", "iloveyou",
                    "jane.doe@example.org", "Nickname", true, true, true));

    // Assert
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
    assertNull(actualSaveAccountResult);
  }

  /**
   * Method under test: {@link AccountPersistenceAdapter#saveAccount(SaveAccount)}
   */
  @Test
  void testSaveAccount3() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    CurrentAccount actualSaveAccountResult = (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository,
            new AccountMapperImpl())).saveAccount(null);

    // Assert
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
    assertNull(actualSaveAccountResult.email());
    assertNull(actualSaveAccountResult.nickname());
    assertNull(actualSaveAccountResult.username());
    assertNull(actualSaveAccountResult.uuid());
    assertNull(actualSaveAccountResult.createdAt());
    assertNull(actualSaveAccountResult.emailVerifiedAt());
    assertTrue(actualSaveAccountResult.roles().isEmpty());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#saveOAuth2Account(SaveOAuth2Account)}
   */
  @Test
  void testSaveOAuth2Account() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
    when(oauth2AccountRepository.save(Mockito.<OAuth2AccountJpaEntity>any())).thenReturn(new OAuth2AccountJpaEntity());
    AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl());

    // Act
    accountPersistenceAdapter
            .saveOAuth2Account(new SaveOAuth2Account("jane.doe@example.org", "Nickname", "Oauth2 Provider", "janedoe"));

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    verify(oauth2AccountRepository).save(Mockito.<OAuth2AccountJpaEntity>any());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#saveOAuth2Account(SaveOAuth2Account)}
   */
  @Test
  void testSaveOAuth2Account2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
    AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
            oauth2AccountRepository, new AccountMapperImpl());

    // Act
    accountPersistenceAdapter
            .saveOAuth2Account(new SaveOAuth2Account("jane.doe@example.org", "Nickname", "Oauth2 Provider", "janedoe"));

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#updatePassword(String, String)}
   */
  @Test
  void testUpdatePassword() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .updatePassword("janedoe", "secret");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#updatePassword(String, String)}
   */
  @Test
  void testUpdatePassword2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .updatePassword("janedoe", "secret");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#updateNickname(String, String)}
   */
  @Test
  void testUpdateNickname() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    when(accountRepository.save(Mockito.<AccountJpaEntity>any())).thenReturn(new AccountJpaEntity());
    Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(ofResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .updateNickname("janedoe", "Nickname");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
    verify(accountRepository).save(Mockito.<AccountJpaEntity>any());
  }

  /**
   * Method under test:
   * {@link AccountPersistenceAdapter#updateNickname(String, String)}
   */
  @Test
  void testUpdateNickname2() {
    // Arrange
    AccountRepository accountRepository = mock(AccountRepository.class);
    Optional<AccountJpaEntity> emptyResult = Optional.empty();
    when(accountRepository.findActive(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
            .thenReturn(emptyResult);
    OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);

    // Act
    (new AccountPersistenceAdapter(accountRepository, oauth2AccountRepository, new AccountMapperImpl()))
            .updateNickname("janedoe", "Nickname");

    // Assert
    verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
  }
}
