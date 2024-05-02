package com.tune_fun.v1.vote.adapter.input.rest;

import com.jayway.jsonpath.JsonPath;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.output.LoadVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.annotation.DirtiesContext;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static java.time.LocalDateTime.now;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.oneOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class VotePaperControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadVotePaperPort loadVotePaperPort;

    @Autowired
    private LoadVoteChoicePort loadVoteChoicePort;

    @Autowired
    private SqsAsyncClient sqsAsyncClient;

    @MockBean
    private FirebaseMessagingMediator firebaseMessagingMediator;

    @SpyBean
    private VoteMessageConsumer voteMessageConsumer;

    @Test
    @Order(1)
    @DisplayName("투표 게시물 등록, 성공")
    void registerVotePaperSuccess() throws Exception {
        dummyService.initArtistAccount();

        dummyService.login(dummyService.getDefaultArtistAccount());
        String accessToken = dummyService.getDefaultArtistAccessToken();

        Set<VotePaperCommands.Offer> offers = Set.of(
                new VotePaperCommands.Offer("Love Lee", "AKMU", List.of("R&B", "Soul"),
                        300000, "2024-04-28"),
                new VotePaperCommands.Offer("Dolphin", "오마이걸", List.of("Dance", "Pop"),
                        200000, "2020-04-27")
        );

        LocalDateTime voteStartAt = now().plusDays(1);
        LocalDateTime voteEndAt = now().plusDays(2);

        VotePaperCommands.Register command = new VotePaperCommands.Register("First Vote Paper", "test",
                "deny-add-choices", voteStartAt, voteEndAt, offers);

        doNothing().when(firebaseMessagingMediator).sendMulticastMessageByTokens(any());

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("title").description("투표 게시물 제목").attributes(constraint("NOT BLANK")),
                fieldWithPath("content").description("투표 게시물 내용").attributes(constraint("NOT BLANK")),
                fieldWithPath("option").description("투표 종류").attributes(constraint("NOT BLANK")),
                fieldWithPath("vote_start_at").description("투표 시작 시간").attributes(constraint("NOT NULL & FUTURE & BEFORE(endAt)")),
                fieldWithPath("vote_end_at").description("투표 종료 시간").attributes(constraint("NOT NULL & FUTURE & AFTER(startAt)")),
                fieldWithPath("offers").description("투표 선택지 목록").attributes(constraint("NOT EMPTY")),
                fieldWithPath("offers[].music").description("노래명").attributes(constraint("NOT BLANK")),
                fieldWithPath("offers[].artist_name").description("아티스트명").attributes(constraint("NOT BLANK")),
                fieldWithPath("offers[].genres").description("장르").attributes(constraint("NOT EMPTY")),
                fieldWithPath("offers[].duration_ms").description("재생 시간(ms)").attributes(constraint("NOT NULL & POSITIVE")),
                fieldWithPath("offers[].release_date").description("발매일").attributes(constraint("NOT BLANK"))
        };

        mockMvc.perform(
                        post(Uris.REGISTER_VOTE_PAPER)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.CREATED))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                requestFields(requestDescriptors),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 등록").
                                                requestFields(requestDescriptors).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        ThrowingRunnable receiveMessageAssertionRunnable = () ->
                sqsAsyncClient.getQueueUrl(r -> r.queueName("send-vote-paper-upload-notification-dev"))
                        .thenApply(GetQueueUrlResponse::queueUrl)
                        .thenCompose(queueUrl -> sqsAsyncClient.receiveMessage(getReceiveMessageRequest(queueUrl)));
        await().untilAsserted(receiveMessageAssertionRunnable);
        verify(voteMessageConsumer).consumeVotePaperUploadEvent(any(VotePaperRegisterEvent.class));

        // TODO : GitHub Actions 에서는 테스트 실패함. 원인 파악 필요
//        verify(firebaseMessagingMediator).sendMulticastMessageByTokens(any());

        Optional<RegisteredVotePaper> votePaperOptional = loadVotePaperPort.loadRegisteredVotePaper(dummyService.getDefaultArtistUsername());
        assertTrue(votePaperOptional.isPresent());

        RegisteredVotePaper votePaper = votePaperOptional.get();
        assertEquals(votePaper.title(), "First Vote Paper");
        assertEquals(votePaper.content(), "test");
        assertNotNull(votePaper.voteStartAt());
        assertNotNull(votePaper.voteEndAt());
        assertNotNull(votePaper.id());
        assertNotNull(votePaper.createdAt());
        assertNotNull(votePaper.updatedAt());

        List<RegisteredVoteChoice> registeredVoteChoices = loadVoteChoicePort.loadRegisteredVoteChoice(votePaper.id());
        assertEquals(registeredVoteChoices.size(), 2);

        List<String> readMusic = JsonPath.parse(registeredVoteChoices).read("$[*].music");
        log.info("readMusic: {}", readMusic);
        readMusic.forEach(music -> assertThat(music, oneOf("Love Lee", "Dolphin")));

        List<String> readArtistName = JsonPath.parse(registeredVoteChoices).read("$[*].artistName");
        log.info("readArtistName: {}", readArtistName);
        readArtistName.forEach(artistName -> assertThat(artistName, oneOf("AKMU", "오마이걸")));

        List<List<String>> readGenres = JsonPath.parse(registeredVoteChoices).read("$[*].genres");
        log.info("readGenres: {}", readGenres);
        readGenres.forEach(genres -> assertThat(genres, oneOf(List.of("R&B", "Soul"), List.of("Dance", "Pop"))));
    }

    private static ReceiveMessageRequest getReceiveMessageRequest(String queueUrl) {
        return ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(1)
                .build();
    }

    @Test
    @Order(2)
    @DisplayName("투표 게시물 영상 제공일 등록, 성공")
    void updateDeliveryDateSuccess() throws NoSuchAlgorithmException {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaper();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        
    }

}
