package com.kakaopay.sprinkling.application;

import com.google.gson.Gson;
import com.kakaopay.sprinkling.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasLength;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
        outputDir = "target/snippets",
        uriScheme = "http",
        uriHost = "localhost",
        uriPort = 80
)
public class SprinklingJackpotTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  @Test
  @DisplayName("뿌리기 API")
  public void shouldSprinkleJackpot() throws Exception {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);

    /* When */
    ResultActions resultActions = this.sprinkleJackpot(creator, roomId, money, pickerCount);

    /* Then */
    resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("token", hasLength(3)))
            .andDo(print())
            .andDo(document("sprinkle/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestHeaders(
                            headerWithName("X-USER-ID").description("사용자 식별값"),
                            headerWithName("X-ROOM-ID").description("대화방 식별값"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("제공하는 컨텐츠 타입, " + MediaType.APPLICATION_JSON),
                            headerWithName(HttpHeaders.ACCEPT).description("응답받는 컨텐츠 타입, " + MediaType.APPLICATION_JSON)
                    ),
                    requestFields(
                            fieldWithPath("money").description("뿌릴 금액"),
                            fieldWithPath("pickerCount").description("뿌릴 인원")
                    ),
                    responseFields(
                            fieldWithPath("token").description("고유 토큰")
                    )
            ));
  }

  @Test
  @DisplayName("받기 API")
  public void shouldPickJackpot() throws Exception {
    /* Given */
    UserId creator = new UserId(2L);
    RoomId roomId = new RoomId("dave-2");
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);

    Token token = this.toToken(this.sprinkleJackpot(creator, roomId, money, pickerCount));

    /* When */
    UserId picker = new UserId(10L);
    ResultActions resultActions = this.pickJackpot(picker, roomId, token);

    /* Then */
    resultActions.andExpect(status().isOk())
            .andDo(print())
            .andDo(document("pick/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestHeaders(
                            headerWithName("X-USER-ID").description("사용자 식별값"),
                            headerWithName("X-ROOM-ID").description("대화방 식별값"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("제공하는 컨텐츠 타입, " + MediaType.APPLICATION_JSON),
                            headerWithName(HttpHeaders.ACCEPT).description("응답받는 컨텐츠 타입, " + MediaType.APPLICATION_JSON)
                    ),
                    responseFields(
                            fieldWithPath("money").description("받은 금액")
                    )
            ));
  }

  @Test
  @DisplayName("조회 API")
  public void shouldCheckJackpot() throws Exception {
    /* Given */
    UserId creator = new UserId(3L);
    RoomId roomId = new RoomId("dave-3");
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);

    Token token = this.toToken(this.sprinkleJackpot(creator, roomId, money, pickerCount));

    /* When */
    ResultActions resultActions = this.checkJackpot(creator, roomId, token);

    /* Then */
    resultActions.andExpect(status().isOk())
            .andDo(print())
            .andDo(document("check/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestHeaders(
                            headerWithName("X-USER-ID").description("사용자 식별값"),
                            headerWithName("X-ROOM-ID").description("대화방 식별값"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("제공하는 컨텐츠 타입, " + MediaType.APPLICATION_JSON),
                            headerWithName(HttpHeaders.ACCEPT).description("응답받는 컨텐츠 타입, " + MediaType.APPLICATION_JSON)
                    ),
                    responseFields(
                            fieldWithPath("sprinklingAt").description("뿌린 시각"),
                            fieldWithPath("sprinklingMoney").description("뿌린 금액"),
                            fieldWithPath("pickedMoney").description("받기 완료된 금액"),
                            fieldWithPath("pickedInfos").description("받기 완료된 정보")
                    )
            ));
  }

  private ResultActions sprinkleJackpot(UserId creator, RoomId roomId, Money money, PickerCount pickerCount) throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("money", money.getValue());
    body.put("pickerCount", pickerCount.getValue());

    return this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creator.getValue())
                    .header("X-ROOM-ID", roomId.getValue())
                    .content(gson.toJson(body))
    );
  }

  private Token toToken(ResultActions resultActions) throws UnsupportedEncodingException {
    String resultText = resultActions.andReturn().getResponse().getContentAsString();
    String tokenText = String.valueOf(gson.fromJson(resultText, Map.class).get("token"));
    return new Token(tokenText);
  }

  private ResultActions pickJackpot(UserId picker, RoomId roomId, Token token) throws Exception {
    return this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot/{token}", token.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", picker.getValue())
                    .header("X-ROOM-ID", roomId.getValue())
    );
  }

  private ResultActions checkJackpot(UserId creator, RoomId roomId, Token token) throws Exception {
    return this.mockMvc.perform(
            get("/api/v1/sprinkling/jackpot/{token}", token.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creator.getValue())
                    .header("X-ROOM-ID", roomId.getValue())
    );
  }

  private OperationRequestPreprocessor getDocumentRequest() {
    return preprocessRequest(prettyPrint());
  }

  private OperationResponsePreprocessor getDocumentResponse() {
    return preprocessResponse(prettyPrint());
  }

}
