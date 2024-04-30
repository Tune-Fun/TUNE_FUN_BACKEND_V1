package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.<LocalDateTime>any(), Mockito.<String>any()))
                .thenReturn(ofResult);
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime deliveryAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime createdAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperMapper.registeredVotePaper(Mockito.<VotePaperJpaEntity>any())).thenReturn(new RegisteredVotePaper(1L,
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
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(votePaperRepository.save(Mockito.<VotePaperJpaEntity>any())).thenReturn(new VotePaperJpaEntity());
        when(votePaperMapper.fromSaveVotePaperBehavior(Mockito.<SaveVotePaper>any(), Mockito.<AccountJpaEntity>any()))
                .thenReturn(new VotePaperJpaEntity());
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime voteEndAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime deliveryAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime createdAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(votePaperMapper.registeredVotePaper(Mockito.<VotePaperJpaEntity>any())).thenReturn(new RegisteredVotePaper(1L,
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
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(votePaperMapper.fromSaveVotePaperBehavior(Mockito.<SaveVotePaper>any(), Mockito.<AccountJpaEntity>any()))
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
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.<String>any())).thenReturn(emptyResult);
        LocalDateTime voteStartAt = LocalDate.of(1970, 1, 1).atStartOfDay();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr",
                "Not all who wander are lost", "JaneDoe", "Option", voteStartAt, LocalDate.of(1970, 1, 1).atStartOfDay())));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.<LocalDateTime>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        when(voteChoiceRepository.saveAll(Mockito.<Iterable<VoteChoiceJpaEntity>>any())).thenReturn(new ArrayList<>());
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.<Set<SaveVoteChoice>>any())).thenReturn(new HashSet<>());

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
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.<LocalDateTime>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.<Set<SaveVoteChoice>>any()))
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
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.<LocalDateTime>any(), Mockito.<Long>any()))
                .thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findAvailableVotePaperById(Long)}
     */
    @Test
    void testFindAvailableVotePaperById() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.<LocalDateTime>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindAvailableVotePaperByIdResult = votePersistenceAdapter
                .findAvailableVotePaperById(1L);

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
        assertSame(ofResult, actualFindAvailableVotePaperByIdResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findAvailableVotePaperById(Long)}
     */
    @Test
    void testFindAvailableVotePaperById2() {
        // Arrange
        when(votePaperRepository.findByVoteEndAtAfterAndId(Mockito.<LocalDateTime>any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findAvailableVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtAfterAndId(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findAvailableVotePaperByAuthor(String)}
     */
    @Test
    void testFindAvailableVotePaperByAuthor() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.<LocalDateTime>any(), Mockito.<String>any()))
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
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(Mockito.<LocalDateTime>any(), Mockito.<String>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.findAvailableVotePaperByAuthor("janedoe"));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsername(isA(LocalDateTime.class), eq("janedoe"));
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
        when(voteChoiceMapper.registeredVoteChoices(Mockito.<List<VoteChoiceJpaEntity>>any()))
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
        when(voteChoiceMapper.registeredVoteChoices(Mockito.<List<VoteChoiceJpaEntity>>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.loadRegisteredVoteChoice(1L));
        verify(voteChoiceMapper).registeredVoteChoices(isA(List.class));
        verify(voteChoiceRepository).findAllByVotePaperId(eq(1L));
    }
}
