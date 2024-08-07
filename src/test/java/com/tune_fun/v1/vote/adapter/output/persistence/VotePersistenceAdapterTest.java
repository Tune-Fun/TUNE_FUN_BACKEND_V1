package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.interaction.adapter.output.persistence.VotePaperLikeJpaEntity;
import com.tune_fun.v1.interaction.adapter.output.persistence.VotePaperLikeRepository;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.VotePaperOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    private VotePaperLikeRepository votePaperLikeRepository;

    @MockBean
    private VotePaperMapper votePaperMapper;

    @MockBean
    private VotePaperRepository votePaperRepository;

    @MockBean
    private VotePaperStatisticsRepository votePaperStatisticsRepository;

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
     * {@link LoadVotePaperPort#scrollVotePaper(Integer, String, String)}
     */
    @Test
    void testScrollVotePaper() {
        // Arrange
        when(votePaperRepository.findFirst10ByEnabledTrueAndAuthorNicknameContaining(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("id"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.scrollVotePaper(1, "Sort Type", "test"));
        verify(votePaperRepository).findFirst10ByEnabledTrueAndAuthorNicknameContaining(isA(String.class), isA(KeysetScrollPosition.class), isA(Sort.class));
    }

    /**
     * Method under test:
     * {@link LoadVotePaperPort#scrollVotePaper(Integer, String, String)}
     */
    @Test
    void testScrollVotePaper2() {
        // Arrange
        when(votePaperRepository.findFirst10ByEnabledTrueAndAuthorNicknameContaining(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("id"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.scrollVotePaper(0, "Sort Type", "test"));
        verify(votePaperRepository).findFirst10ByEnabledTrueAndAuthorNicknameContaining(isA(String.class), isA(KeysetScrollPosition.class), isA(Sort.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(Long)}
     */
    @Test
    void testLoadRegisteredVotePaper() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        when(votePaperMapper.registeredVotePaper(Mockito.any()))
                .thenReturn(new RegisteredVotePaper(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", 1L, "JaneDoe", "janedoe", "Dr",
                        "Not all who wander are lost", VotePaperOption.ALLOW_ADD_CHOICES, "https://example.org/example",
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN,
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN));

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = votePersistenceAdapter
                .loadRegisteredVotePaper(1L);

        // Assert
        verify(votePaperMapper).registeredVotePaper(isA(VotePaperJpaEntity.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        LocalDateTime expectedVoteEndAtResult = getResult.voteStartAt();
        assertSame(expectedVoteEndAtResult, getResult.voteEndAt());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#loadRegisteredVotePaper(String)}
     */
    @Test
    void testLoadRegisteredVotePaper2() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(),
                Mockito.any())).thenReturn(ofResult);
        when(votePaperMapper.registeredVotePaper(Mockito.any()))
                .thenReturn(new RegisteredVotePaper(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", 1L, "JaneDoe", "janedoe", "Dr",
                        "Not all who wander are lost", VotePaperOption.ALLOW_ADD_CHOICES, "https://example.org/example",
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN,
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN));

        // Act
        Optional<RegisteredVotePaper> actualLoadRegisteredVotePaperResult = votePersistenceAdapter
                .loadRegisteredVotePaper("janedoe");

        // Assert
        verify(votePaperMapper).registeredVotePaper(isA(VotePaperJpaEntity.class));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class),
                eq("janedoe"));
        RegisteredVotePaper getResult = actualLoadRegisteredVotePaperResult.get();
        LocalDateTime expectedVoteEndAtResult = getResult.voteStartAt();
        assertSame(expectedVoteEndAtResult, getResult.voteEndAt());
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
        when(votePaperMapper.registeredVotePaper(Mockito.any()))
                .thenReturn(new RegisteredVotePaper(1L, "01234567-89AB-CDEF-FEDC-BA9876543210", 1L, "JaneDoe", "janedoe", "Dr",
                        "Not all who wander are lost", VotePaperOption.ALLOW_ADD_CHOICES, "https://example.org/example",
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN,
                        Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN));

        // Act
        RegisteredVotePaper actualSaveVotePaperResult = votePersistenceAdapter
                .saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe",
                        VotePaperOption.ALLOW_ADD_CHOICES, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN));

        // Assert
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
        verify(votePaperMapper).fromSaveVotePaperBehavior(isA(SaveVotePaper.class), isA(AccountJpaEntity.class));
        verify(votePaperMapper).registeredVotePaper(isA(VotePaperJpaEntity.class));
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
        LocalDateTime expectedVoteEndAtResult = actualSaveVotePaperResult.voteStartAt();
        assertSame(expectedVoteEndAtResult, actualSaveVotePaperResult.voteEndAt());
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

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe",
                        VotePaperOption.ALLOW_ADD_CHOICES, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN)));
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

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.saveVotePaper(new SaveVotePaper("Dr", "Not all who wander are lost", "JaneDoe",
                        VotePaperOption.ALLOW_ADD_CHOICES, Constants.LOCAL_DATE_TIME_MIN, Constants.LOCAL_DATE_TIME_MIN)));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("JaneDoe"));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper() {
        // Arrange
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        votePersistenceAdapter.disableVotePaper(1L);

        // Assert
        verify(votePaperRepository).findById(eq(1L));
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper2() {
        // Arrange
        when(votePaperRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.disableVotePaper(1L));
        verify(votePaperRepository).findById(eq(1L));
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper3() {
        // Arrange
        VotePaperJpaEntity votePaperJpaEntity = mock(VotePaperJpaEntity.class);
        doNothing().when(votePaperJpaEntity).disable();
        Optional<VotePaperJpaEntity> ofResult = Optional.of(votePaperJpaEntity);
        when(votePaperRepository.save(Mockito.any())).thenReturn(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        votePersistenceAdapter.disableVotePaper(1L);

        // Assert that nothing has changed
        verify(votePaperJpaEntity).disable();
        verify(votePaperRepository).findById(eq(1L));
        verify(votePaperRepository).save(isA(VotePaperJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper4() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act
        votePersistenceAdapter.disableVotePaper(1L);

        // Assert that nothing has changed
        verify(votePaperRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#disableVotePaper(Long)}
     */
    @Test
    void testDisableVotePaper5() {
        // Arrange
        VotePaperJpaEntity votePaperJpaEntity = mock(VotePaperJpaEntity.class);
        doThrow(new IllegalArgumentException("foo")).when(votePaperJpaEntity).disable();
        Optional<VotePaperJpaEntity> ofResult = Optional.of(votePaperJpaEntity);
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.disableVotePaper(1L));
        verify(votePaperJpaEntity).disable();
        verify(votePaperRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        Mockito.<VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?>>when(votePaperMapper.updateDeliveryAt(
                        any(), any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, Constants.LOCAL_DATE_TIME_MIN));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperMapper).updateDeliveryAt(isA(LocalDateTime.class),
                isA(VotePaperJpaEntity.VotePaperJpaEntityBuilder.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateDeliveryAt(Long, LocalDateTime)}
     */
    @Test
    void testUpdateDeliveryAt2() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, Constants.LOCAL_DATE_TIME_MIN));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
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
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateDeliveryAt(1L, Constants.LOCAL_DATE_TIME_MIN));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperJpaEntity).toBuilder();
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        Mockito.<VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?>>when(votePaperMapper.updateVideoUrl(
                        any(), any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateVideoUrl(1L, "https://example.org/example"));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperMapper).updateVideoUrl(eq("https://example.org/example"),
                isA(VotePaperJpaEntity.VotePaperJpaEntityBuilder.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl2() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateVideoUrl(1L, "https://example.org/example"));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#updateVideoUrl(Long, String)}
     */
    @Test
    void testUpdateVideoUrl3() {
        // Arrange
        VotePaperJpaEntity votePaperJpaEntity = mock(VotePaperJpaEntity.class);
        Mockito.<VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?>>when(votePaperJpaEntity.toBuilder())
                .thenThrow(new IllegalArgumentException("foo"));
        Optional<VotePaperJpaEntity> ofResult = Optional.of(votePaperJpaEntity);
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.updateVideoUrl(1L, "https://example.org/example"));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(votePaperJpaEntity).toBuilder();
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
     * Method under test:
     * {@link VotePersistenceAdapter#loadVoteChoiceByUsername(Long, String)}
     */
    @Test
    void testLoadVoteChoiceByUsername() {
        // Arrange
        Optional<VoteChoiceJpaEntity> ofResult = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findByVotePaperIdAndCreatedBy(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(ofResult);
        when(voteChoiceMapper.registeredVoteChoice(Mockito.any()))
                .thenReturn(new RegisteredVoteChoice(1L, 1L, "42", "Music", "Music Image", "Artist Name"));

        // Act
        votePersistenceAdapter.loadVoteChoiceByUsername(1L, "janedoe");

        // Assert
        verify(voteChoiceMapper).registeredVoteChoice(isA(VoteChoiceJpaEntity.class));
        verify(voteChoiceRepository).findByVotePaperIdAndCreatedBy(eq(1L), eq("janedoe"));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        when(voteChoiceRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.any())).thenReturn(new HashSet<>());

        // Act
        votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>());

        // Assert
        verify(voteChoiceMapper).fromSaveVoteChoiceBehaviors(isA(Set.class));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
        verify(voteChoiceRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice2() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);
        when(voteChoiceMapper.fromSaveVoteChoiceBehaviors(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(voteChoiceMapper).fromSaveVoteChoiceBehaviors(isA(Set.class));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#saveVoteChoice(Long, Set)}
     */
    @Test
    void testSaveVoteChoice3() {
        // Arrange
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVoteChoice(1L, new HashSet<>()));
        verify(votePaperRepository).findOneAvailable(eq(1L), isNull());
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaperLike(Long, String)}
     */
    @Test
    void testSaveVotePaperLike() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        Optional<VotePaperJpaEntity> ofResult2 = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(votePaperLikeRepository.save(Mockito.any())).thenReturn(new VotePaperLikeJpaEntity());

        // Act
        votePersistenceAdapter.saveVotePaperLike(1L, "janedoe");

        // Assert
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(votePaperRepository).findById(eq(1L));
        verify(votePaperLikeRepository).save(isA(VotePaperLikeJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaperLike(Long, String)}
     */
    @Test
    void testSaveVotePaperLike2() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        Optional<VotePaperJpaEntity> ofResult2 = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(votePaperLikeRepository.save(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaperLike(1L, "janedoe"));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(votePaperRepository).findById(eq(1L));
        verify(votePaperLikeRepository).save(isA(VotePaperLikeJpaEntity.class));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaperLike(Long, String)}
     */
    @Test
    void testSaveVotePaperLike3() {
        // Arrange
        Optional<AccountJpaEntity> emptyResult = Optional.empty();
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaperLike(1L, "janedoe"));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#saveVotePaperLike(Long, String)}
     */
    @Test
    void testSaveVotePaperLike4() {
        // Arrange
        Optional<AccountJpaEntity> ofResult = Optional.of(new AccountJpaEntity());
        when(accountPersistenceAdapter.loadAccountByUsername(Mockito.any())).thenReturn(ofResult);
        Optional<VotePaperJpaEntity> emptyResult = Optional.empty();
        when(votePaperRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.saveVotePaperLike(1L, "janedoe"));
        verify(accountPersistenceAdapter).loadAccountByUsername(eq("janedoe"));
        verify(votePaperRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#deleteVotePaperLike(Long, String)}
     */
    @Test
    void testDeleteVotePaperLike() {
        // Arrange
        doNothing().when(votePaperLikeRepository)
                .deleteByVotePaperIdAndLikerUsername(Mockito.<Long>any(), Mockito.<String>any());

        // Act
        votePersistenceAdapter.deleteVotePaperLike(1L, "janedoe");

        // Assert that nothing has changed
        verify(votePaperLikeRepository).deleteByVotePaperIdAndLikerUsername(eq(1L), eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#deleteVotePaperLike(Long, String)}
     */
    @Test
    void testDeleteVotePaperLike2() {
        // Arrange
        doThrow(new IllegalArgumentException("foo")).when(votePaperLikeRepository)
                .deleteByVotePaperIdAndLikerUsername(Mockito.<Long>any(), Mockito.<String>any());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.deleteVotePaperLike(1L, "janedoe"));
        verify(votePaperLikeRepository).deleteByVotePaperIdAndLikerUsername(eq(1L), eq("janedoe"));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#initializeStatistics(Long)}
     */
    @Test
    void testInitializeStatistics() {
        // Arrange
        when(votePaperStatisticsRepository.save(Mockito.<VotePaperStatisticsJpaEntity>any()))
                .thenReturn(new VotePaperStatisticsJpaEntity());

        // Act
        votePersistenceAdapter.initializeStatistics(1L);

        // Assert
        verify(votePaperStatisticsRepository).save(isA(VotePaperStatisticsJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#initializeStatistics(Long)}
     */
    @Test
    void testInitializeStatistics2() {
        // Arrange
        when(votePaperStatisticsRepository.save(Mockito.<VotePaperStatisticsJpaEntity>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.initializeStatistics(1L));
        verify(votePaperStatisticsRepository).save(isA(VotePaperStatisticsJpaEntity.class));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#updateLikeCount(Long, Long)}
     */
    @Test
    void testUpdateLikeCount() {
        // Arrange
        doNothing().when(votePaperStatisticsRepository).updateLikeCount(Mockito.<Long>any(), Mockito.<Long>any());

        // Act
        votePersistenceAdapter.updateLikeCount(1L, 3L);

        // Assert that nothing has changed
        verify(votePaperStatisticsRepository).updateLikeCount(eq(1L), eq(3L));
    }

    /**
     * Method under test: {@link VotePersistenceAdapter#updateLikeCount(Long, Long)}
     */
    @Test
    void testUpdateLikeCount2() {
        // Arrange
        doThrow(new IllegalArgumentException("foo")).when(votePaperStatisticsRepository)
                .updateLikeCount(Mockito.<Long>any(), Mockito.<Long>any());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.updateLikeCount(1L, 3L));
        verify(votePaperStatisticsRepository).updateLikeCount(eq(1L), eq(3L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findOneAvailable(Long, String)}
     */
    @Test
    void testFindOneAvailable() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any())).thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindOneAvailableResult = votePersistenceAdapter.findOneAvailable(1L, "janedoe");

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
        when(votePaperRepository.findOneAvailable(Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findOneAvailable(1L, "janedoe"));
        verify(votePaperRepository).findOneAvailable(eq(1L), eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findCompleteVotePaperById(Long)}
     */
    @Test
    void testFindCompleteVotePaperById() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(
                votePaperRepository.findByVoteEndAtBeforeAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindCompleteVotePaperByIdResult = votePersistenceAdapter
                .findCompleteVotePaperById(1L);

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
        when(
                votePaperRepository.findByVoteEndAtBeforeAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findCompleteVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtBeforeAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperByAuthor(String)}
     */
    @Test
    void testFindProgressingVotePaperByAuthor() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(),
                Mockito.any())).thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindProgressingVotePaperByAuthorResult = votePersistenceAdapter
                .findProgressingVotePaperByAuthor("janedoe");

        // Assert
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class),
                eq("janedoe"));
        assertSame(ofResult, actualFindProgressingVotePaperByAuthorResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperByAuthor(String)}
     */
    @Test
    void testFindProgressingVotePaperByAuthor2() {
        // Arrange
        when(votePaperRepository.findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(Mockito.any(),
                Mockito.any())).thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.findProgressingVotePaperByAuthor("janedoe"));
        verify(votePaperRepository).findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(isA(LocalDateTime.class),
                eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findProgressingVotePaperById(Long)}
     */
    @Test
    void testFindProgressingVotePaperById() {
        // Arrange
        Optional<VotePaperJpaEntity> ofResult = Optional.of(new VotePaperJpaEntity());
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

        // Act
        Optional<VotePaperJpaEntity> actualFindProgressingVotePaperByIdResult = votePersistenceAdapter
                .findProgressingVotePaperById(1L);

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
        when(votePaperRepository.findByVoteEndAtAfterAndIdAndEnabledTrue(Mockito.any(), Mockito.<Long>any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> votePersistenceAdapter.findProgressingVotePaperById(1L));
        verify(votePaperRepository).findByVoteEndAtAfterAndIdAndEnabledTrue(isA(LocalDateTime.class), eq(1L));
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

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findByVotePaperIdAndUsername(Long, String)}
     */
    @Test
    void testFindByVotePaperIdAndUsername() {
        // Arrange
        Optional<VoteChoiceJpaEntity> ofResult = Optional.of(new VoteChoiceJpaEntity());
        when(voteChoiceRepository.findByVotePaperIdAndCreatedBy(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(ofResult);

        // Act
        Optional<VoteChoiceJpaEntity> actualFindByVotePaperIdAndUsernameResult = votePersistenceAdapter
                .findByVotePaperIdAndUsername(1L, "janedoe");

        // Assert
        verify(voteChoiceRepository).findByVotePaperIdAndCreatedBy(eq(1L), eq("janedoe"));
        assertSame(ofResult, actualFindByVotePaperIdAndUsernameResult);
    }

    /**
     * Method under test:
     * {@link VotePersistenceAdapter#findByVotePaperIdAndUsername(Long, String)}
     */
    @Test
    void testFindByVotePaperIdAndUsername2() {
        // Arrange
        when(voteChoiceRepository.findByVotePaperIdAndCreatedBy(Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> votePersistenceAdapter.findByVotePaperIdAndUsername(1L, "janedoe"));
        verify(voteChoiceRepository).findByVotePaperIdAndCreatedBy(eq(1L), eq("janedoe"));
    }
}
