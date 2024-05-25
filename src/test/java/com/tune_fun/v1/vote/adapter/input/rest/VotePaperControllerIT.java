package com.tune_fun.v1.vote.adapter.input.rest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.vote.adapter.input.event.VoteEventListener;
import com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.output.LoadVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.event.VotePaperDeadlineEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.VotePaperOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.common.constant.Constants.EMPTY_STRING;
import static com.tune_fun.v1.common.util.StringUtil.ulid;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @SpyBean
    private VoteEventListener voteEventListener;

    @Value("${event.sqs.send-vote-paper-upload-notification.queue-name}")
    private String votePaperUploadQueue;

    @Value("${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}")
    private String votePaperUpdateDeliveryDateQueue;

    @Value("${event.sqs.send-vote-paper-update-video-url-notification.queue-name}")
    private String votePaperUpdateVideoUrlQueue;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("투표 게시물 스크롤 조회, 성공")
    void getVotePapersSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaperBatch();

        dummyService.initAndLogin();
        String accessToken = dummyService.getDefaultAccessToken();


        ParameterDescriptor[] requestDescriptors = {
                parameterWithName("last_id").description("이전 페이지의 마지막 투표 게시물 ID").attributes(constraint("NOT NULL")),
                parameterWithName("sort_type").description("정렬 방식").attributes(constraint("RECENT or VOTE_COUNT"))
        };

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data.empty").description("데이터가 비어있는지 여부").attributes(constraint("NOT NULL")),
                fieldWithPath("data.last").description("마지막 페이지인지 여부").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content").description("투표 게시물 목록").attributes(constraint("NOT EMPTY")),
                fieldWithPath("data.content[].id").description("투표 게시물 ID").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].uuid").description("투표 게시물 UUID").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].title").description("투표 게시물 제목").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].author_username").description("투표 게시물 작성자 아이디").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].author_profile_image_url").description("투표 게시물 작성자 프로필 이미지 URL").attributes(constraint("NULL or NOT NULL")),
                fieldWithPath("data.content[].author_nickname").description("투표 게시물 작성자 닉네임").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].remain_days").description("투표 게시물 남은 일수").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].total_vote_count").description("투표 게시물 총 투표 수").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content[].total_like_count").description("투표 게시물 총 좋아요 수").attributes(constraint("NOT NULL"))
        );

        mockMvc.perform(
                        get(Uris.VOTE_PAPER_ROOT)
                                .queryParam("last_id", "1000")
                                .queryParam("sort_type", "RECENT")
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpectAll(jsonPath("$.data.empty", notNullValue()), jsonPath("$.data.empty", is(false)))
                .andExpectAll(jsonPath("$.data.last", notNullValue()), jsonPath("$.data.last", is(false)))
                .andExpect(jsonPath("$.data.content", hasSize(10)))
                .andExpectAll(jsonPath("$.data.content[*].id").exists(), jsonPath("$.data.content[*].id", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].uuid").exists(), jsonPath("$.data.content[*].uuid", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].title").exists(), jsonPath("$.data.content[*].title", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].author_username").exists(), jsonPath("$.data.content[*].author_username", notNullValue()))
                .andExpect(jsonPath("$.data.content[*].author_profile_image_url").exists())
                .andExpectAll(jsonPath("$.data.content[*].author_nickname").exists(), jsonPath("$.data.content[*].author_nickname", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].remain_days").exists(), jsonPath("$.data.content[*].remain_days", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].total_vote_count").exists(), jsonPath("$.data.content[*].total_vote_count", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].total_like_count").exists(), jsonPath("$.data.content[*].total_like_count", notNullValue()))
                .andExpect(jsonPath("$.data.content[0].id", is(999)))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                queryParameters(requestDescriptors),
                                responseFields(responseDescriptors),
                                resource(
                                        builder().
                                                description("투표 게시물 스크롤 조회").
                                                queryParameters(requestDescriptors).
                                                responseFields(responseDescriptors)
                                                .build()
                                )
                        )
                );

    }

    @Transactional
    @Test
    @Order(2)
    @DisplayName("투표 게시물 상세 조회, 성공")
    void getVotePaperSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaper();
        
        dummyService.initAndLogin();
        dummyService.registerVote();

        Long votePaperId = dummyService.getDefaultVotePaper().getId();
        String accessToken = dummyService.getDefaultAccessToken();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data.id").description("투표 게시물 ID").attributes(constraint("NOT NULL")),
                fieldWithPath("data.uuid").description("투표 게시물 UUID").attributes(constraint("NOT NULL")),
                fieldWithPath("data.author").description("투표 게시물 작성자 닉네임").attributes(constraint("NOT NULL")),
                fieldWithPath("data.author_username").description("투표 게시물 작성자 아이디").attributes(constraint("NOT NULL")),
                fieldWithPath("data.title").description("투표 게시물 제목").attributes(constraint("NOT NULL")),
                fieldWithPath("data.content").description("투표 게시물 내용").attributes(constraint("NOT NULL")),
                fieldWithPath("data.option").description("투표 종류").attributes(constraint("NOT NULL, allow-add-choices || deny-add-choices")),
                fieldWithPath("data.video_url").description("투표 게시물 영상 URL").attributes(constraint("NULL or NOT NULL")),
                fieldWithPath("data.is_voted").description("투표 여부").attributes(constraint("NOT NULL")),
                fieldWithPath("data.vote_start_at").description("투표 시작 시간").attributes(constraint("NOT NULL")),
                fieldWithPath("data.vote_end_at").description("투표 종료 시간").attributes(constraint("NOT NULL")),
                fieldWithPath("data.delivery_at").description("투표 게시물 영상 제공일").attributes(constraint("NULL or NOT NULL")),
                fieldWithPath("data.created_at").description("투표 게시물 생성 시간").attributes(constraint("NOT NULL")),
                fieldWithPath("data.updated_at").description("투표 게시물 수정 시간").attributes(constraint("NOT NULL")),
                fieldWithPath("data.choices").description("투표 선택지 목록").attributes(constraint("NOT EMPTY")),
                fieldWithPath("data.choices[].id").description("아이디").attributes(constraint("NOT NULL")),
                fieldWithPath("data.choices[].offer_id").description("Spotify Music ID").attributes(constraint("NOT BLANK")),
                fieldWithPath("data.choices[].music").description("노래명").attributes(constraint("NOT BLANK")),
                fieldWithPath("data.choices[].music_image").description("노래 이미지 URL").attributes(constraint(EMPTY_STRING)),
                fieldWithPath("data.choices[].artist_name").description("아티스트명").attributes(constraint("NOT BLANK")),
                fieldWithPath("data.choices[].vote_paper_id").description("투표 게시물 ID").attributes(constraint("NOT NULL"))
        );

        mockMvc.perform(
                        get(Uris.VOTE_PAPER_ROOT + "/{votePaperId}", votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("data.id", notNullValue()))
                .andExpect(jsonPath("data.uuid", notNullValue()))
                .andExpect(jsonPath("data.author", notNullValue()))
                .andExpect(jsonPath("data.author_username", notNullValue()))
                .andExpect(jsonPath("data.title", notNullValue()))
                .andExpect(jsonPath("data.content", notNullValue()))
                .andExpect(jsonPath("data.option", notNullValue()))
                .andExpect(jsonPath("data.video_url", nullValue()))
                .andExpectAll(jsonPath("data.is_voted", notNullValue()), jsonPath("data.is_voted", is(true)))
                .andExpect(jsonPath("data.vote_start_at", notNullValue()))
                .andExpect(jsonPath("data.vote_end_at", notNullValue()))
                .andExpect(jsonPath("data.delivery_at", nullValue()))
                .andExpect(jsonPath("data.created_at", notNullValue()))
                .andExpect(jsonPath("data.updated_at", notNullValue()))
                .andExpect(jsonPath("data.choices", notNullValue()))
                .andExpect(jsonPath("data.choices[*].id", notNullValue()))
                .andExpect(jsonPath("data.choices[*].offer_id", notNullValue()))
                .andExpect(jsonPath("data.choices[*].music", notNullValue()))
                .andExpect(jsonPath("data.choices[*].music_image", notNullValue()))
                .andExpect(jsonPath("data.choices[*].artist_name", notNullValue()))
                .andExpect(jsonPath("data.choices[*].vote_paper_id", notNullValue()))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                responseFields(responseDescriptors),
                                resource(
                                        builder().
                                                description("투표 게시물 상세 조회").
                                                pathParameters(pathParameter).
                                                responseFields(responseDescriptors)
                                                .build()
                                )
                        )
                );

    }

    @Transactional
    @Test
    @Order(3)
    @DisplayName("투표 게시물 등록, 성공")
    void registerVotePaperSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.initArtistAndLogin();

        String accessToken = dummyService.getDefaultArtistAccessToken();

        Set<VotePaperCommands.Offer> offers = Set.of(
                new VotePaperCommands.Offer(ulid(), "KNOCK (With 박문치)", ulid(), "권진아"),
                new VotePaperCommands.Offer(ulid(), "Orange, You're Not a Joke to Me!", ulid(), "스텔라장 (Stella Jang)")
        );

        LocalDateTime voteStartAt = now().plusSeconds(3);
        LocalDateTime voteEndAt = now().plusDays(5);

        VotePaperCommands.Register command = new VotePaperCommands.Register("First Vote Paper", "test",
                VotePaperOption.DENY_ADD_CHOICES, voteStartAt, voteEndAt, offers);

        doNothing().when(firebaseMessagingMediator).sendMulticastMessageByTokens(any());

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("title").description("투표 게시물 제목").attributes(constraint("NOT BLANK")),
                fieldWithPath("content").description("투표 게시물 내용").attributes(constraint(EMPTY_STRING)),
                fieldWithPath("option").description("투표 종류").attributes(constraint("NOT BLANK, allow-add-choices || deny-add-choices")),
                fieldWithPath("vote_start_at").description("투표 시작 시간").attributes(constraint("NOT NULL & FUTURE & BEFORE(vote_end_at)")),
                fieldWithPath("vote_end_at").description("투표 종료 시간").attributes(constraint("NOT NULL & FUTURE & AFTER(vote_start_at)")),
                fieldWithPath("offers").description("투표 선택지 목록").attributes(constraint("NOT EMPTY")),
                fieldWithPath("offers[].id").description("아이디").attributes(constraint("NOT NULL")),
                fieldWithPath("offers[].music").description("노래명").attributes(constraint("NOT BLANK")),
                fieldWithPath("offers[].music_image").description("노래 이미지 URL").attributes(constraint(EMPTY_STRING)),
                fieldWithPath("offers[].artist_name").description("아티스트명").attributes(constraint("NOT BLANK"))
        };

        mockMvc.perform(
                        post(Uris.VOTE_PAPER_ROOT)
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

        awaitReceiveMessage(votePaperUploadQueue);
        verify(voteMessageConsumer).consumeVotePaperUploadEvent(any(VotePaperRegisterEvent.class));
        verify(voteEventListener).handleVotePaperDeadlineEvent(any(VotePaperDeadlineEvent.class));

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

        DocumentContext parsedChoices = JsonPath.parse(toJson(registeredVoteChoices));

        List<String> readMusic = parsedChoices.read("$[*].music");
        log.info("readMusic: {}", readMusic);
        assertThat(readMusic, hasItems("KNOCK (With 박문치)", "Orange, You're Not a Joke to Me!"));

        List<String> readArtistName = parsedChoices.read("$[*].artist_name");
        log.info("readArtistName: {}", readArtistName);
        assertThat(readArtistName, hasItems("권진아", "스텔라장 (Stella Jang)"));


    }

    @Transactional
    @Test
    @Order(4)
    @DisplayName("투표 선택지 등록, 성공")
    void registerVotePaperChoiceSuccess() throws Exception {
        dummyService.initAccount();
        dummyService.initArtistAndLogin();

        dummyService.initVotePaperAllowAddChoices();
        dummyService.login(dummyService.getDefaultAccount());

        String accessToken = dummyService.getDefaultAccessToken();
        Long votePaperId = dummyService.getDefaultVotePaper().getId();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("id").description("Spotify Music ID").attributes(constraint("NOT BLANK")),
                fieldWithPath("music").description("노래명").attributes(constraint("NOT BLANK")),
                fieldWithPath("music_image").description("노래 이미지 URL").attributes(constraint(EMPTY_STRING)),
                fieldWithPath("artist_name").description("아티스트명").attributes(constraint("NOT BLANK")),
        };

        VotePaperCommands.Offer command = new VotePaperCommands.Offer(ulid(), "이별이란 어느 별에 (Feat. 조광일)", ulid(), "HYNN (박혜원)");

        mockMvc.perform(
                        post(Uris.VOTE_PAPER_CHOICE, votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding(UTF_8)
                )
                .andExpectAll(baseAssertion(MessageCode.CREATED))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                requestFields(requestDescriptors),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 선택지 등록").
                                                pathParameters(pathParameter).
                                                requestFields(requestDescriptors).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        List<RegisteredVoteChoice> registeredVoteChoices = loadVoteChoicePort.loadRegisteredVoteChoice(votePaperId);
        assertEquals(registeredVoteChoices.size(), 3);

        DocumentContext parsedVoteChoices = JsonPath.parse(toJson(registeredVoteChoices));

        List<String> readMusic = parsedVoteChoices.read("$[*].music");
        assertThat(readMusic, hasItem("이별이란 어느 별에 (Feat. 조광일)"));

        List<String> readArtistName = parsedVoteChoices.read("$[*].artist_name");
        assertThat(readArtistName, hasItem("HYNN (박혜원)"));

    }

    @Transactional
    @Test
    @Order(5)
    @DisplayName("투표 게시물 영상 제공일 등록, 성공")
    void updateDeliveryDateSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.initArtistAndLogin();

        dummyService.initVotePaper();
        dummyService.registerVote();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));
        FieldDescriptor requestDescriptor = fieldWithPath("delivery_at").description("투표 게시물 영상 제공일").attributes(constraint("NOT NULL & FUTURE"));

        String accessToken = dummyService.getDefaultArtistAccessToken();

        Long votePaperId = dummyService.getDefaultVotePaper().getId();
        LocalDateTime deliveryAt = now().plusDays(5);
        VotePaperCommands.UpdateDeliveryDate command = new VotePaperCommands.UpdateDeliveryDate(deliveryAt);

        doNothing().when(firebaseMessagingMediator).sendMulticastMessageByTokens(any());

        mockMvc.perform(
                        patch(Uris.UPDATE_VOTE_PAPER_DELIVERY_DATE, votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding(UTF_8)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                requestFields(requestDescriptor),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 영상 제공일 등록").
                                                pathParameters(pathParameter).
                                                requestFields(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );


        awaitReceiveMessage(votePaperUpdateDeliveryDateQueue);
        verify(voteMessageConsumer).consumeVotePaperUpdateDeliveryDateEvent(any(VotePaperUpdateDeliveryDateEvent.class));

        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(() -> new AssertionError("투표 게시물을 찾을 수 없습니다."));
        assertEquals(registeredVotePaper.deliveryAt(), deliveryAt.withNano(0));
    }

    @Transactional
    @Test
    @Order(6)
    @DisplayName("투표 게시물 영상 업로드, 성공")
    void updateVideoUrlSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.initArtistAndLogin();

        dummyService.initVotePaper();
        dummyService.registerVote();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));
        FieldDescriptor requestDescriptor = fieldWithPath("video_url").description("영상 URL").attributes(constraint("NOT BLANK & URL FORMAT"));

        String accessToken = dummyService.getDefaultArtistAccessToken();

        Long votePaperId = dummyService.getDefaultVotePaper().getId();
        String videoUrl = "https://www.youtube.com/watch?v=u9-ISLtq1g0";

        VotePaperCommands.UpdateVideoUrl command = new VotePaperCommands.UpdateVideoUrl(videoUrl);

        doNothing().when(firebaseMessagingMediator).sendMulticastMessageByTokens(any());

        mockMvc.perform(
                        patch(Uris.UPDATE_VOTE_PAPER_VIDEO_URL, votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding(UTF_8)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                requestFields(requestDescriptor),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 영상 업로드").
                                                pathParameters(pathParameter).
                                                requestFields(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );


        awaitReceiveMessage(votePaperUpdateVideoUrlQueue);
        verify(voteMessageConsumer).consumeVotePaperUpdateVideoUrlEvent(any(VotePaperUpdateVideoUrlEvent.class));

        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(() -> new AssertionError("투표 게시물을 찾을 수 없습니다."));
        assertEquals(registeredVotePaper.videoUrl(), videoUrl);
    }

    @Transactional
    @Test
    @Order(7)
    @DisplayName("투표 게시물 삭제, 성공")
    void deleteVotePaperSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.initArtistAndLogin();

        dummyService.initVotePaper();
        dummyService.registerVote();

        String accessToken = dummyService.getDefaultArtistAccessToken();
        Long votePaperId = dummyService.getDefaultVotePaper().getId();

        ParameterDescriptor queryParameter = parameterWithName("vote_paper_id").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        mockMvc.perform(
                        delete(Uris.VOTE_PAPER_ROOT)
                                .queryParam("vote_paper_id", String.valueOf(votePaperId))
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                queryParameters(queryParameter),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 삭제").
                                                queryParameters(queryParameter).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        assertThrows(AssertionError.class,
                () -> loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                        .orElseThrow(() -> new AssertionError("투표 게시물을 찾을 수 없습니다.")));
    }

    private void awaitReceiveMessage(final String queueName) {
        ThrowingRunnable receiveMessageAssertionRunnable = () ->
                sqsAsyncClient.getQueueUrl(r -> r.queueName(queueName))
                        .thenApply(GetQueueUrlResponse::queueUrl)
                        .thenCompose(queueUrl -> sqsAsyncClient.receiveMessage(getReceiveMessageRequest(queueUrl)));
        await().untilAsserted(receiveMessageAssertionRunnable);
    }

    private static ReceiveMessageRequest getReceiveMessageRequest(String queueUrl) {
        return ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(1)
                .build();
    }

}
