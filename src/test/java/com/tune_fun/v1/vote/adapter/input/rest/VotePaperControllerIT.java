package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

class VotePaperControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadVotePaperPort loadVotePaperPort;

    @Test
    @Order(1)
    @DisplayName("투표 게시물 등록, 성공")
    void registerVotePaperSuccess() throws Exception {
        dummyService.initArtistAccount();

        dummyService.login(dummyService.getDefaultAccount());
        String accessToken = dummyService.getDefaultAccessToken();

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

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("title").description("투표 게시물 제목").attributes(constraint("NOT BLANK")),
                fieldWithPath("content").description("투표 게시물 내용").attributes(constraint("NOT BLANK")),
                fieldWithPath("option").description("투표 종류").attributes(constraint("NOT BLANK")),
                fieldWithPath("vote_start_at").description("투표 시작 시간").attributes(constraint("NOT NULL & FUTURE & BEFORE(endAt)")),
                fieldWithPath("vote_end_at").description("투표 종료 시간").attributes(constraint("NOT NULL & FUTURE & AFTER(startAt)")),
                fieldWithPath("offers").description("투표 선택지 목록").attributes(constraint("NOT EMPTY")),
                fieldWithPath("offers[].music").description("노래명").attributes(constraint("NOT BLANK")),
                fieldWithPath("offers[].artist_name").description("가수명").attributes(constraint("NOT BLANK")),
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
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                requestFields(requestDescriptors),
                                resource(
                                        builder().
                                                description("투표 게시물 등록").
                                                requestFields(requestDescriptors)
                                                .build()
                                )
                        )
                );

        Optional<RegisteredVotePaper> votePaperOptional = loadVotePaperPort.loadRegisteredVotePaper(dummyService.getDefaultUsername());
        assertTrue(votePaperOptional.isPresent());

        RegisteredVotePaper votePaper = votePaperOptional.get();
        assertEquals(votePaper.title(), "First Vote Paper");
        assertEquals(votePaper.content(), "test");
        assertEquals(votePaper.voteStartAt(), voteStartAt);
        assertEquals(votePaper.voteEndAt(), voteEndAt);
        assertNotNull(votePaper.id());
        assertNotNull(votePaper.createdAt());
        assertNotNull(votePaper.updatedAt());
    }

}