package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ArtistControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("아티스트 스크롤 조회, 성공")
    void scrollArtistSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        String accessToken = dummyService.getDefaultArtistAccessToken();

        Long artistAccountId = dummyService.getDefaultArtistAccount().getId();
        String artistNickname = dummyService.getDefaultArtistAccount().getNickname();

        ParameterDescriptor[] requestDescriptors = {
                parameterWithName("last_id").description("이전 페이지의 마지막 아티스트 ID").optional().attributes(constraint("NULL 허용")),
                parameterWithName("nickname").description("작성자 닉네임").optional().attributes(constraint("NULL 허용"))
        };

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(sliceResponseFields,
            fieldWithPath("data.content[].id").description("아티스트 ID"),
            fieldWithPath("data.content[].username").description("아티스트 사용자명"),
            fieldWithPath("data.content[].nickname").description("아티스트 닉네임"),
            fieldWithPath("data.content[].profile_image_url").description("아티스트 프로필 이미지 URL")
        );

        mockMvc.perform(
                        get(Uris.ARTIST_ROOT)
                                .queryParam("last_id", String.valueOf(artistAccountId + 1))
                                .queryParam("nickname", artistNickname)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpectAll(jsonPath("$.data.empty", notNullValue()), jsonPath("$.data.empty", is(false)))
                .andExpectAll(jsonPath("$.data.last", notNullValue()), jsonPath("$.data.last", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpectAll(jsonPath("$.data.content[*].id").exists(), jsonPath("$.data.content[*].id", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].username").exists(), jsonPath("$.data.content[*].username", notNullValue()))
                .andExpectAll(jsonPath("$.data.content[*].nickname").exists(), jsonPath("$.data.content[*].nickname", notNullValue()))
                .andExpect(jsonPath("$.data.content[*].profile_image_url").exists())
                .andExpect(jsonPath("$.data.content[0].id", is(artistAccountId)))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                queryParameters(requestDescriptors),
                                responseFields(responseDescriptors),
                                resource(
                                        builder().
                                                description("아티스트 스크롤 조회").
                                                queryParameters(requestDescriptors).
                                                responseFields(responseDescriptors)
                                                .build()
                                )
                        )
                );


    }

    @Transactional
    @Test
    @DisplayName("아티스트 상세 프로필 조회, 성공")
    void findArtistSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        String accessToken = dummyService.getDefaultArtistAccessToken();

        Long artistAccountId = dummyService.getDefaultArtistAccount().getId();

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data.id").description("아티스트 ID"),
                fieldWithPath("data.nickname").description("아티스트 닉네임"),
                fieldWithPath("data.profile_image_url").description("아티스트 프로필 이미지 URL")
        );

        mockMvc.perform(
                        get(Uris.ARTIST_ROOT + "/" + artistAccountId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpectAll(jsonPath("$.data.id").exists(), jsonPath("$.data.id", notNullValue()))
                .andExpectAll(jsonPath("$.data.nickname").exists(), jsonPath("$.data.nickname", notNullValue()))
                .andExpect(jsonPath("$.data.profile_image_url").exists())
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                responseFields(responseDescriptors),
                                resource(
                                        builder().
                                                description("아티스트 상세 조회")
                                                .responseFields(responseDescriptors)
                                                .build()
                                )
                        )
                );
    }

}
