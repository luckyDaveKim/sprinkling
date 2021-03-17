package com.kakaopay.sprinkling.application;

import com.google.gson.Gson;
import com.kakaopay.sprinkling.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasLength;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
            .andDo(print());
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
            .andDo(print());
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
            .andDo(print());
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

}
