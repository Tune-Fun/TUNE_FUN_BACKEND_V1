package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {VotePersistenceAdapter.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class VotePersistenceAdapterTest {

    @MockBean
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @MockBean
    private VoteChoiceMapper voteChoiceMapper;

    @MockBean
    private VoteChoiceRepository voteChoiceRepository;

    @MockBean
    private VotePaperMapper votePaperMapper;

    @MockBean
    private VotePaperRepository votePaperRepository;

    @Autowired
    private VotePersistenceAdapter votePersistenceAdapter;

    @MockBean
    private VoteRepository voteRepository;

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoterIdsByVotePaperUuid(String)}
     */
    @Test
    void testLoadVoterIdsByVotePaperUuid() {
        // Arrange
        ArrayList<Long> resultLongList = new ArrayList<>();
        when(voteRepository.findVoterIdsByVotePaperUuid(Mockito.any())).thenReturn(resultLongList);

        // Act
        List<Long> actualLoadVoterIdsByVotePaperUuidResult = votePersistenceAdapter
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
        when(voteRepository.findVoterIdsByVotePaperUuid(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.loadVoterIdsByVotePaperUuid("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(voteRepository).findVoterIdsByVotePaperUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoteByVoterAndVotePaperId(String, Long)}
     */
    @Test
    void testLoadVoteByVoterAndVotePaperId() {
        // Arrange
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

        // Act
        Optional<RegisteredVote> actualLoadVoteByVoterAndVotePaperIdResult = votePersistenceAdapter
                .loadVoteByVoterAndVotePaperId("Voter", 1L);

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
        when(voteRepository.findByVoterUsernameAndVotePaperId(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.loadVoteByVoterAndVotePaperId("Voter", 1L));
        verify(voteRepository).findByVoterUsernameAndVotePaperId(eq("Voter"), eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        when(voteRepository.save(Mockito.any())).thenReturn(new VoteJpaEntity());
        Optional<VoteChoiceJpaEntity> ofResult2 = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        votePersistenceAdapter.saveVote(1L, "janedoe");

        // Assert
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(voteChoiceRepository).findById(eq(1L));
        verify(voteRepository).save(isA(VoteJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote2() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        when(voteRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        Optional<VoteChoiceJpaEntity> ofResult2 = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVote(1L, "janedoe"));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(voteChoiceRepository).findById(eq(1L));
        verify(voteRepository).save(isA(VoteJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote3() {
        // Arrange
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(emptyResult);
        Optional<VoteChoiceJpaEntity> ofResult = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVote(1L, "janedoe"));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(voteChoiceRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVote(Long, String)}
     */
    @Test
    void testSaveVote4() {
        // Arrange
        Optional<VoteChoiceJpaEntity> emptyResult = Optional.empty();
        when(voteChoiceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVote(1L, "janedoe"));
        verify(voteChoiceRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime deliveryAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime createdAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperMapper.registeredVotePaper(Mockito.any())).thenReturn(new RegisteredVotePaper(1L,
                "01234567-89AB-CDEF-FEDC-BA9876543210", "JaneDoe", "Dr", "Not all who wander are lost", "Option", voteStartAt,
                voteEndAt, deliveryAt, createdAt, LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = votePersistenceAdapter
                .loadRegisteredVotePaper("janedoe");

        // Assert
        verify(votePaperMapper).registeredVotePaper(isA(VotePaperJpaEntity.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsername(isA(LocalDateTime.class), eq("janedoe"));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        LocalTime expectedToLocalTimeResult = getResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, getResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        when(votePaperMapper.fromSaveVotePaperBehavior(Mockito.any(), Mockito.any()))
                .thenReturn(new VotePaperJpaEntity());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime deliveryAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime createdAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperMapper.registeredVotePaper(Mockito.any())).thenReturn(new RegisteredVotePaper(1L,
                "01234567-89AB-CDEF-FEDC-BA9876543210", "JaneDoe", "Dr", "Not all who wander are lost", "Option", voteStartAt,
                voteEndAt, deliveryAt, createdAt, LocalDate.of(1970, 1, 1).atStartOfDay()));
        LocalDateTime voteStartAt2 = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act
        RegisteredVotePaper actualSaveVotePaperResult = votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr",
                "Not all who wander are lost", "JaneDoe", "Option", voteStartAt2, LocalDate.of(1970, 1, 1).atStartOfDay()));

        // Assert
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
        verify(votePaperMapper).fromSaveVotePaperBehavior(isA(SaveVotePaper.class), isA(AccountJpaEntity.class));
        verify(votePaperMapper).registeredVotePaper(isA(VotePaperJpaEntity.class));
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        LocalTime expectedToLocalTimeResult = actualSaveVotePaperResult.voteStartAt().toLocalTime();
        assertSame(expectedToLocalTimeResult, actualSaveVotePaperResult.voteEndAt().toLocalTime());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper2() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        when(votePaperMapper.fromSaveVotePaperBehavior(Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr",
                "Not all who wander are lost", "JaneDoe", "Option", voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay())));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
        verify(votePaperMapper).fromSaveVotePaperBehavior(isA(SaveVotePaper.class), isA(AccountJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaper(SaveVotePaper)}
     */
    @Test
    void testSaveVotePaper3() {
        // Arrange
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(emptyResult);
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr",
                "Not all who wander are lost", "JaneDoe", "Option", voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay())));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        Mockito.<VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?>>when(votePaperMapper.updateDeliveryAt(
                        Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, LocalDate.of(1970, 1, 1).atStartOfDay()));
        verify(votePaperMapper).updateDeliveryAt(isA(LocalDateTime.class),
                isA(VotePaperJpaEntity.VotePaperJpaEntityBuilder.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt2() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, LocalDate.of(1970, 1, 1).atStartOfDay()));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt3() {
        // Arrange
        VotePaperJpaEntity votePaperJpaEntity = mock(VotePaperJpaEntity.class);
        Mockito.<VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?>>when(votePaperJpaEntity.toBuilder())
                .thenThrow(new IllegalArgumentException("foo"));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(votePaperJpaEntity);
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, LocalDate.of(1970, 1, 1).atStartOfDay()));
        verify(votePaperJpaEntity).toBuilder();
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVoteChoice(Long)}
     */
    @Test
    void testLoadRegisteredVoteChoice() {
        // Arrange
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<RegisteredVoteChoice> registeredVoteChoiceList = new ArrayList<>();
        when(voteChoiceMapper.registeredVoteChoices(Mockito.any()))
                .thenReturn(registeredVoteChoiceList);

        // Act
        List<RegisteredVoteChoice> actualLoadRegisteredVoteChoiceResult = votePersistenceAdapter
                .loadRegisteredVoteChoice(1L);

        // Assert
        verify(voteChoiceMapper).registeredVoteChoices(isA(List.class));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
        assertTrue(actualLoadRegisteredVoteChoiceResult.isEmpty());
        assertSame(registeredVoteChoiceList, actualLoadRegisteredVoteChoiceResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVoteChoice(Long)}
     */
    @Test
    void testLoadRegisteredVoteChoice2() {
        // Arrange
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(voteChoiceMapper.registeredVoteChoices(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.loadRegisteredVoteChoice(1L));
        verify(voteChoiceMapper).registeredVoteChoices(isA(List.class));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        when(voteChoiceRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.any())).thenReturn(new HashSet<>());

        // Act
        votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>());

        // Assert
        verify(voteChoiceMapper).fromSaveVoteChoiceBehaviors(isA(Set.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice2() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(voteChoiceMapper).fromSaveVoteChoiceBehaviors(isA(Set.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice3() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperById(Long)}
     */
    @Test
    void testFindProgressingVotePaperById() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindProgressingVotePaperByIdResult = votePersistenceAdapter
                .findProgressingVotePaperById(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
        assertSame(ofResult, actualFindProgressingVotePaperByIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperById(Long)}
     */
    @Test
    void testFindProgressingVotePaperById2() {
        // Arrange
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findProgressingVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findCompleteVotePaperById(Long)}
     */
    @Test
    void testFindCompleteVotePaperById() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtBeforeAndId(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindCompleteVotePaperByIdResult = votePersistenceAdapter
                .findCompleteVotePaperById(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtBeforeAndId(isA(LocalDateTime.class), eq(1L));
        assertSame(ofResult, actualFindCompleteVotePaperByIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findCompleteVotePaperById(Long)}
     */
    @Test
    void testFindCompleteVotePaperById2() {
        // Arrange
        when(votePaperRepository.findByVoteEndAtBeforeAndId(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findCompleteVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtBeforeAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findAvailableVotePaperByAuthor(String)}
     */
    @Test
    void testFindAvailableVotePaperByAuthor() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.any(), Mockito.any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindAvailableVotePaperByAuthorResult = votePersistenceAdapter
                .findAvailableVotePaperByAuthor("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsername(isA(LocalDateTime.class), eq("janedoe"));
        assertSame(ofResult, actualFindAvailableVotePaperByAuthorResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findAvailableVotePaperByAuthor(String)}
     */
    @Test
    void testFindAvailableVotePaperByAuthor2() {
        // Arrange
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.findAvailableVotePaperByAuthor("janedoe"));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsername(isA(LocalDateTime.class), eq("janedoe"));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#findAllByVotePaperId(Long)}
     */
    @Test
    void testFindAllByVotePaperId() {
        // Arrange
        ArrayList<VoteChoiceJpaEntity> voteChoiceJpaEntityList = new ArrayList<>();
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenReturn(voteChoiceJpaEntityList);

        // Act
        List<VoteChoiceJpaEntity> actualFindAllByVotePaperIdResult = votePersistenceAdapter.findAllByVotePaperId(1L);

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
        when(voteChoiceRepository.findAllByVotePaperId(Mockito.<Long>any())).thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findAllByVotePaperId(1L));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
    }
}
