package com.seniors.domain.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.ControllerTestSupport;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.repository.post.PostRepository;
import com.seniors.domain.resume.dto.CareerDto;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.resume.service.ResumeService;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    ResumeService resumeService;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("신규 이력서를 등록한다. 직종은 비워둘 수 없다.")
    void resumeAddWithOccupationBlankConstraint() throws Exception {
        // given
        ResumeDto.SaveResumeReq saveResumeReq = new ResumeDto.SaveResumeReq();
        saveResumeReq.setIntroduce("안녕하세요");
        saveResumeReq.setIsOpened(true);
        saveResumeReq.setName("철수");

        String accessToken = "access_token";
        MockMultipartFile image = new MockMultipartFile("image", "abc.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile data = new MockMultipartFile("data", null, "application/json", objectMapper.writeValueAsString(saveResumeReq).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart(HttpMethod.POST, "/api/resumes")
                                .file(image)
                                .file(data)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", accessToken)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("직종은 비워둘 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @DisplayName("신규 이력서를 등록한다. 직종은 30자 이하여야 한다.")
    void resumeAddWithOccupationLengthConstraint() throws Exception {
        // given
        ResumeDto.SaveResumeReq saveResumeReq = new ResumeDto.SaveResumeReq();
        saveResumeReq.setIntroduce("안녕하세요");
        saveResumeReq.setIsOpened(true);
        saveResumeReq.setOccupation("가나다라가나다라다나다라가나다라가나다라가나다라가나다라가나다라가나다라가");
        saveResumeReq.setName("철수");

        String accessToken = "access_token";
        MockMultipartFile image = new MockMultipartFile("image", "abc.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile data = new MockMultipartFile("data", null, "application/json", objectMapper.writeValueAsString(saveResumeReq).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart(HttpMethod.POST, "/api/resumes")
                                .file(image)
                                .file(data)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", accessToken)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("직종은 30자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("신규 이력서를 등록한다. 이름은 비워둘 수 없다.")
    void resumeAddWithNameBlankConstraint() throws Exception {
        // given
        ResumeDto.SaveResumeReq saveResumeReq = new ResumeDto.SaveResumeReq();
        saveResumeReq.setIntroduce("안녕하세요");
        saveResumeReq.setIsOpened(true);
        saveResumeReq.setOccupation("의사");

        String accessToken = "access_token";
        MockMultipartFile image = new MockMultipartFile("image", "abc.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile data = new MockMultipartFile("data", null, "application/json", objectMapper.writeValueAsString(saveResumeReq).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart(HttpMethod.POST, "/api/resumes")
                                .file(image)
                                .file(data)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", accessToken)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 비워둘 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("신규 이력서를 등록한다. 이름은 30자 이하여야 한다.")
    void resumeAddWithNameLengthConstraint() throws Exception {
        // given
        ResumeDto.SaveResumeReq saveResumeReq = new ResumeDto.SaveResumeReq();
        saveResumeReq.setIntroduce("안녕하세요");
        saveResumeReq.setIsOpened(true);
        saveResumeReq.setOccupation("의사");
        saveResumeReq.setName("가나다라가나다라가나다라가나다라가나다라가나다라가나다라가나다라가나다라가나다라");

        String accessToken = "access_token";
        MockMultipartFile image = new MockMultipartFile("image", "abc.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile data = new MockMultipartFile("data", null, "application/json", objectMapper.writeValueAsString(saveResumeReq).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .multipart(HttpMethod.POST, "/api/resumes")
                                .file(image)
                                .file(data)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", accessToken)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 30자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void resumeDetails() throws Exception{

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUserId()).thenReturn(123l); // 예시로 설정한 값, 실제 값으로 대체

        Resume resume = Resume.builder()
                        .occupation("의사")
                        .name("철수").build();
        Long resumeId = 400l;
        // given
        when(resumeService.findResume(resumeId, 123l))
                .thenReturn(ResumeDto.GetResumeRes.from(resume));

        mockMvc.perform(
                        get("/api/resumes/" + resumeId)
                                .principal(new UsernamePasswordAuthenticationToken(customUserDetails, null, null))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void myResumeDetails() {
    }

    @Test
    void resumeList() {
    }

    @Test
    void resumeModify() {
    }

    @Test
    void resumeRemove() {
    }

    @Test
    void resumeViewerList() {
    }
}