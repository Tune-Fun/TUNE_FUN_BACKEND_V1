package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountMapperImpl;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.adapter.output.persistence.AccountRepository;
import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountRepository;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VotePersistenceAdapterTest {
    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoterIdsByVotePaperUuid(String)}
     */
    @Test
    void testLoadVoterIdsByVotePaperUuid() {
        // Arrange
        VoteRepository voteRepository = mock(VoteRepository.class);
        ArrayList<Long> resultLongList = new ArrayList<>();
        when(voteRepository.findVoterIdsByVotePaperUuid(Mockito.any())).thenReturn(resultLongList);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        List<Long> actualLoadVoterIdsByVotePaperUuidResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .loadVoterIdsByVotePaperUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Assert
        verify(voteRepository).findVoterIdsByVotePaperUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        assertTrue(actualLoadVoterIdsByVotePaperUuidResult.isEmpty());
        assertSame(resultLongList, actualLoadVoterIdsByVotePaperUuidResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoterIdsByVotePaperUuid(String)}
     */
    @Test
    void testLoadVoterIdsByVotePaperUuid2() {
        // Arrange
        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.findVoterIdsByVotePaperUuid(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                        .loadVoterIdsByVotePaperUuid("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(voteRepository).findVoterIdsByVotePaperUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoteByVoterAndVotePaperId(String, Long)}
     */
    @Test
    void testLoadVoteByVoterAndVotePaperId() {
        // Arrange
        VoteRepository voteRepository = mock(VoteRepository.class);
        RegisteredVote buildResult = RegisteredVote.builder()
                .artistName("Artist Name")
                .id(1L)
                .music("Music")
                .username("janedoe")
                .uuid("01234567-89AB-CDEF-FEDC-BA9876543210")
                .votePaperId(1L)
                .build();
        Optional<RegisteredVote> ofResult = Optional.of(buildResult);
        when(voteRepository.findByVoterUsernameAndVotePaperId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVote> actualLoadVoteByVoterAndVotePaperIdResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadVoteByVoterAndVotePaperId("Voter", 1L);

        // Assert
        verify(voteRepository).findByVoterUsernameAndVotePaperId(eq("Voter"), eq(1L));
        assertSame(ofResult, actualLoadVoteByVoterAndVotePaperIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoteByVoterAndVotePaperId(String, Long)}
     */
    @Test
    void testLoadVoteByVoterAndVotePaperId2() {
        // Arrange
        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.findByVoterUsernameAndVotePaperId(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).loadVoteByVoterAndVotePaperId("Voter",
                        1L));
        verify(voteRepository).findByVoterUsernameAndVotePaperId(eq("Voter"), eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.save(Mockito.any())).thenReturn(new VoteJpaEntity());
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        Optional<VoteChoiceJpaEntity> ofResult2 = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository,
                votePaperMapper, new VoteChoiceMapperImpl())).saveVote(1L, "janedoe");

        // Assert
        verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
        verify(voteChoiceRepository).findById(eq(1L));
        verify(voteRepository).save(isA(VoteJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote2() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        when(voteRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        Optional<VoteChoiceJpaEntity> ofResult2 = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).saveVote(1L, "janedoe"));
        verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
        verify(voteChoiceRepository).findById(eq(1L));
        verify(voteRepository).save(isA(VoteJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote3() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(emptyResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        Optional<VoteChoiceJpaEntity> ofResult = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).saveVote(1L, "janedoe"));
        verify(accountRepository).findActive(eq("janedoe"), isNull(), isNull());
        verify(voteChoiceRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote4() {
        // Arrange
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        Optional<VoteChoiceJpaEntity> emptyResult = Optional.empty();
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).saveVote(1L, "janedoe"));
        verify(voteChoiceRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#scrollVotePaper(Integer, String)}
     */
    @Test
    void testScrollVotePaper() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findFirst10ByEnabledTrue(Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("id"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).scrollVotePaper(1, "Sort Type"));
        verify(votePaperRepository).findFirst10ByEnabledTrue(isA(KeysetScrollPosition.class), isA(Sort.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(Long)}
     */
    @Test
    void testLoadRegisteredVotePaper() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        assertNull(getResult.id());
        assertNull(getResult.author());
        assertNull(getResult.content());
        assertNull(getResult.option());
        assertNull(getResult.title());
        assertNull(getResult.uuid());
        assertNull(getResult.createdAt());
        assertNull(getResult.deliveryAt());
        assertNull(getResult.updatedAt());
        assertNull(getResult.voteEndAt());
        assertNull(getResult.voteStartAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(Long)}
     */
    @Test
    void testLoadRegisteredVotePaper2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        AccountJpaEntity author = new AccountJpaEntity();
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        Optional<VotePaperJpaEntity> ofResult = Optional
                .of(new VotePaperJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", "Dr", "Not all who wander are lost",
                        author, VotePaperOption.ALLOW_ADD_CHOICES, voteStartAt, voteEndAt, LocalDate.of(1970, 1, 1).atStartOfDay(), true,
                        "https://example.org/example", new ArrayList<>()));
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        LocalTime expectedToLocalTimeResult = getResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, getResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(Long)}
     */
    @Test
    void testLoadRegisteredVotePaper3() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        assertFalse(actualLoadRegisteredVotePaperResult.isPresent());
        assertSame(emptyResult, actualLoadRegisteredVotePaperResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(Long)}
     */
    @Test
    void testLoadRegisteredVotePaper4() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).loadRegisteredVotePaper(1L));
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper5() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        assertNull(getResult.id());
        assertNull(getResult.author());
        assertNull(getResult.content());
        assertNull(getResult.option());
        assertNull(getResult.title());
        assertNull(getResult.uuid());
        assertNull(getResult.createdAt());
        assertNull(getResult.deliveryAt());
        assertNull(getResult.updatedAt());
        assertNull(getResult.voteEndAt());
        assertNull(getResult.voteStartAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper6() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        AccountJpaEntity author = new AccountJpaEntity();
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        Optional<VotePaperJpaEntity> ofResult = Optional
                .of(new VotePaperJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", "Dr", "Not all who wander are lost",
                        author, VotePaperOption.ALLOW_ADD_CHOICES, voteStartAt, voteEndAt, LocalDate.of(1970, 1, 1).atStartOfDay(), true,
                        "https://example.org/example", new ArrayList<>()));
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        LocalTime expectedToLocalTimeResult = getResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, getResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper7() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVotePaper("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
        assertFalse(actualLoadRegisteredVotePaperResult.isPresent());
        assertSame(emptyResult, actualLoadRegisteredVotePaperResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper8() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).loadRegisteredVotePaper("janedoe"));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(emptyResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr",
                "Not all who wander are lost", "JaneDoe", "Option", voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay())));
        verify(accountRepository).findActive(eq("JaneDoe"), isNull(), isNull());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper2() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act
        RegisteredVotePaper actualSaveVotePaperResult = votePersistenceAdapter
                .saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe", "allow-add-choices",
                        voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Assert
        verify(accountRepository).findActive(eq("JaneDoe"), isNull(), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualSaveVotePaperResult.id());
        assertNull(actualSaveVotePaperResult.author());
        assertNull(actualSaveVotePaperResult.content());
        assertNull(actualSaveVotePaperResult.option());
        assertNull(actualSaveVotePaperResult.title());
        assertNull(actualSaveVotePaperResult.uuid());
        assertNull(actualSaveVotePaperResult.createdAt());
        assertNull(actualSaveVotePaperResult.deliveryAt());
        assertNull(actualSaveVotePaperResult.updatedAt());
        assertNull(actualSaveVotePaperResult.voteEndAt());
        assertNull(actualSaveVotePaperResult.voteStartAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper3() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(null);
        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act
        RegisteredVotePaper actualSaveVotePaperResult = votePersistenceAdapter
                .saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe", "allow-add-choices",
                        voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Assert
        verify(accountRepository).findActive(eq("JaneDoe"), isNull(), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualSaveVotePaperResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper4() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        AccountJpaEntity author = new AccountJpaEntity();
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperRepository.save(Mockito.any()))
                .thenReturn(new VotePaperJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", "Dr",
                        "Not all who wander are lost", author, VotePaperOption.ALLOW_ADD_CHOICES, voteStartAt, voteEndAt,
                        LocalDate.of(1970, 1, 1).atStartOfDay(), true, "https://example.org/example", new ArrayList<>()));
        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());
        LocalDateTime voteStartAt2 = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act
        RegisteredVotePaper actualSaveVotePaperResult = votePersistenceAdapter
                .saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe", "allow-add-choices",
                        voteStartAt2, LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Assert
        verify(accountRepository).findActive(eq("JaneDoe"), isNull(), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        LocalTime expectedToLocalTimeResult = actualSaveVotePaperResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, actualSaveVotePaperResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper5() {
        // Arrange
        AccountRepository accountRepository = mock(AccountRepository.class);
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountRepository.findActive(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any()))
                .thenThrow(new IllegalArgumentException("allow-add-choices"));
        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe",
                        "allow-add-choices", voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay())));
        verify(accountRepository).findActive(eq("JaneDoe"), isNull(), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act
        RegisteredVotePaper actualUpdateDeliveryAtResult = votePersistenceAdapter.updateDeliveryAt(1L,
                LocalDate.of(1970, 1, 1).atStartOfDay());

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualUpdateDeliveryAtResult.id());
        assertNull(actualUpdateDeliveryAtResult.author());
        assertNull(actualUpdateDeliveryAtResult.content());
        assertNull(actualUpdateDeliveryAtResult.option());
        assertNull(actualUpdateDeliveryAtResult.title());
        assertNull(actualUpdateDeliveryAtResult.uuid());
        assertNull(actualUpdateDeliveryAtResult.createdAt());
        assertNull(actualUpdateDeliveryAtResult.deliveryAt());
        assertNull(actualUpdateDeliveryAtResult.updatedAt());
        assertNull(actualUpdateDeliveryAtResult.voteEndAt());
        assertNull(actualUpdateDeliveryAtResult.voteStartAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, LocalDate.of(1970, 1, 1).atStartOfDay()));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt3() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(null);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act
        RegisteredVotePaper actualUpdateDeliveryAtResult = votePersistenceAdapter.updateDeliveryAt(1L,
                LocalDate.of(1970, 1, 1).atStartOfDay());

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualUpdateDeliveryAtResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt4() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        AccountJpaEntity author = new AccountJpaEntity();
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperRepository.save(Mockito.any()))
                .thenReturn(new VotePaperJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", "Dr",
                        "Not all who wander are lost", author, VotePaperOption.ALLOW_ADD_CHOICES, voteStartAt, voteEndAt,
                        LocalDate.of(1970, 1, 1).atStartOfDay(), true, "https://example.org/example", new ArrayList<>()));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act
        RegisteredVotePaper actualUpdateDeliveryAtResult = votePersistenceAdapter.updateDeliveryAt(1L,
                LocalDate.of(1970, 1, 1).atStartOfDay());

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        LocalTime expectedToLocalTimeResult = actualUpdateDeliveryAtResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, actualUpdateDeliveryAtResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt5() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, LocalDate.of(1970, 1, 1).atStartOfDay()));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        RegisteredVotePaper actualUpdateVideoUrlResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .updateVideoUrl(1L, "https://example.org/example");

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualUpdateVideoUrlResult.id());
        assertNull(actualUpdateVideoUrlResult.author());
        assertNull(actualUpdateVideoUrlResult.authorUsername());
        assertNull(actualUpdateVideoUrlResult.content());
        assertNull(actualUpdateVideoUrlResult.option());
        assertNull(actualUpdateVideoUrlResult.title());
        assertNull(actualUpdateVideoUrlResult.uuid());
        assertNull(actualUpdateVideoUrlResult.videoUrl());
        assertNull(actualUpdateVideoUrlResult.createdAt());
        assertNull(actualUpdateVideoUrlResult.deliveryAt());
        assertNull(actualUpdateVideoUrlResult.updatedAt());
        assertNull(actualUpdateVideoUrlResult.voteEndAt());
        assertNull(actualUpdateVideoUrlResult.voteStartAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).updateVideoUrl(1L,
                        "https://example.org/example"));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl3() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.save(Mockito.any())).thenReturn(null);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        RegisteredVotePaper actualUpdateVideoUrlResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .updateVideoUrl(1L, "https://example.org/example");

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        assertNull(actualUpdateVideoUrlResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl4() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        AccountJpaEntity author = new AccountJpaEntity();
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperRepository.save(Mockito.any()))
                .thenReturn(new VotePaperJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", "Dr",
                        "Not all who wander are lost", author, VotePaperOption.ALLOW_ADD_CHOICES, voteStartAt, voteEndAt,
                        LocalDate.of(1970, 1, 1).atStartOfDay(), true, "https://example.org/example", new ArrayList<>()));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        RegisteredVotePaper actualUpdateVideoUrlResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .updateVideoUrl(1L, "https://example.org/example");

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        LocalTime expectedToLocalTimeResult = actualUpdateVideoUrlResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, actualUpdateVideoUrlResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl5() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).updateVideoUrl(1L,
                        "https://example.org/example"));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVoteChoice(Long)}
     */
    @Test
    void testLoadRegisteredVoteChoice() {
        // Arrange
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        List<RegisteredVoteChoice> actualLoadRegisteredVoteChoiceResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVoteChoice(1L);

        // Assert
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
        assertTrue(actualLoadRegisteredVoteChoiceResult.isEmpty());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVoteChoice(Long)}
     */
    @Test
    void testLoadRegisteredVoteChoice2() {
        // Arrange
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).loadRegisteredVoteChoice(1L));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVoteChoice(Long)}
     */
    @Test
    void testLoadRegisteredVoteChoice3() {
        // Arrange
        ArrayList<VoteChoiceJpaEntity> voteChoiceJpaEntityList = new ArrayList<>();
        VotePaperJpaEntity votePaper = new VotePaperJpaEntity();
        voteChoiceJpaEntityList.add(new VoteChoiceJpaEntity(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", votePaper,
                new Offer(",", ",", ",", "2020-03-01", 1), new ArrayList<>()));
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(voteChoiceJpaEntityList);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        List<RegisteredVoteChoice> actualLoadRegisteredVoteChoiceResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).loadRegisteredVoteChoice(1L);

        // Assert
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
        assertEquals(1, actualLoadRegisteredVoteChoiceResult.size());
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act
        votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>());

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.saveAll(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice3() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice4() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        HashSet<SaveVoteChoice> behavior = new HashSet<>();
        behavior.add(new SaveVoteChoice("Music", "Offer Artist Name", new HashSet<>(), 2, "2020-03-01"));

        // Act
        votePersistenceAdapter.saveVoteChoice(1L, behavior);

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice5() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();
        VotePersistenceAdapter votePersistenceAdapter = new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl());

        HashSet<SaveVoteChoice> behavior = new HashSet<>();
        behavior.add(new SaveVoteChoice(",", ",", new HashSet<>(), 2, "2020-03-01"));
        behavior.add(new SaveVoteChoice("Music", "Offer Artist Name", new HashSet<>(), 2, "2020-03-01"));

        // Act
        votePersistenceAdapter.saveVoteChoice(1L, behavior);

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository,
                votePaperMapper, new VoteChoiceMapperImpl())).disableVotePaper(1L);

        // Assert
        verify(votePaperRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        doThrow(new IllegalArgumentException("foo")).when(votePaperRepository).findById(Mockito.<Long>any());
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).disableVotePaper(1L));
        verify(votePaperRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findOneAvailable(Long, String)}
     */
    @Test
    void testFindOneAvailable() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<VotePaperJpaEntity> actualFindOneAvailableResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .findOneAvailable(1L, "janedoe");

        // Assert
        verify(votePaperRepository).findOneAvailable(eq(1L), eq("janedoe"));
        assertSame(ofResult, actualFindOneAvailableResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findOneAvailable(Long, String)}
     */
    @Test
    void testFindOneAvailable2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).findOneAvailable(1L, "janedoe"));
        verify(votePaperRepository).findOneAvailable(eq(1L), eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findCompleteVotePaperById(Long)}
     */
    @Test
    void testFindCompleteVotePaperById() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtBeforeAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<VotePaperJpaEntity> actualFindCompleteVotePaperByIdResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).findCompleteVotePaperById(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtBeforeAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        assertSame(ofResult, actualFindCompleteVotePaperByIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findCompleteVotePaperById(Long)}
     */
    @Test
    void testFindCompleteVotePaperById2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findByVoteEndAtBeforeAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).findCompleteVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtBeforeAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperByAuthor(String)}
     */
    @Test
    void testFindProgressingVotePaperByAuthor() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<VotePaperJpaEntity> actualFindProgressingVotePaperByAuthorResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).findProgressingVotePaperByAuthor("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
        assertSame(ofResult, actualFindProgressingVotePaperByAuthorResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperByAuthor(String)}
     */
    @Test
    void testFindProgressingVotePaperByAuthor2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                        .findProgressingVotePaperByAuthor("janedoe"));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class), eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperById(Long)}
     */
    @Test
    void testFindProgressingVotePaperById() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        Optional<VotePaperJpaEntity> actualFindProgressingVotePaperByIdResult = (new VotePersistenceAdapter(
                accountPersistenceAdapter, voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper,
                new VoteChoiceMapperImpl())).findProgressingVotePaperById(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        assertSame(ofResult, actualFindProgressingVotePaperByIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperById(Long)}
     */
    @Test
    void testFindProgressingVotePaperById2() {
        // Arrange
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).findProgressingVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#findAllByVotePaperId(Long)}
     */
    @Test
    void testFindAllByVotePaperId() {
        // Arrange
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        ArrayList<VoteChoiceJpaEntity> voteChoiceJpaEntityList = new ArrayList<>();
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(voteChoiceJpaEntityList);
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act
        List<VoteChoiceJpaEntity> actualFindAllByVotePaperIdResult = (new VotePersistenceAdapter(accountPersistenceAdapter,
                voteRepository, votePaperRepository, voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl()))
                .findAllByVotePaperId(1L);

        // Assert
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
        assertTrue(actualFindAllByVotePaperIdResult.isEmpty());
        assertSame(voteChoiceJpaEntityList, actualFindAllByVotePaperIdResult);
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#findAllByVotePaperId(Long)}
     */
    @Test
    void testFindAllByVotePaperId2() {
        // Arrange
        VoteChoiceRepository voteChoiceRepository = mock(VoteChoiceRepository.class);
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenThrow(new IllegalArgumentException("foo"));
        AccountRepository accountRepository = mock(AccountRepository.class);
        OAuth2AccountRepository oauth2AccountRepository = mock(OAuth2AccountRepository.class);
        AccountPersistenceAdapter accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository,
                oauth2AccountRepository, new AccountMapperImpl());

        VoteRepository voteRepository = mock(VoteRepository.class);
        VotePaperRepository votePaperRepository = mock(VotePaperRepository.class);
        VotePaperMapperImpl votePaperMapper = new VotePaperMapperImpl();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> (new VotePersistenceAdapter(accountPersistenceAdapter, voteRepository, votePaperRepository,
                        voteChoiceRepository, votePaperMapper, new VoteChoiceMapperImpl())).findAllByVotePaperId(1L));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
    }
}
