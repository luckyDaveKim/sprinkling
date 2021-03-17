package com.kakaopay.sprinkling.api.sprinkling;

import com.kakaopay.sprinkling.domain.Token;
import com.kakaopay.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PickJackpotTests {

  @MockBean
  private SprinklingService sprinklingService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("받기 가능함")
  public void shouldPickJackpot() throws Exception {
    /* Given */
    Token token = Token.generate();
    String pickerId = "123";
    String roomId = "a123";

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot/{token}", token.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", pickerId)
                    .header("X-ROOM-ID", roomId)
    );

    /* Then */
    resultActions.andExpect(status().isOk())
            .andDo(print());
  }

  @Test
  @DisplayName("받기는 받는 사람 정보가 필요함")
  public void shouldThrowPickJackpotWithOutPickerId() throws Exception {
    /* Given */
    Token token = Token.generate();
    String roomId = "a123";

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot/{token}", token.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-ROOM-ID", roomId)
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

  @Test
  @DisplayName("받기는 대화방 정보가 필요함")
  public void shouldThrowPickJackpotWithOutRoomId() throws Exception {
    /* Given */
    Token token = Token.generate();
    String pickerId = "123";

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot/{token}", token.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", pickerId)
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

  @Test
  @DisplayName("받기는 토큰 정보가 필요함")
  public void shouldThrowPickJackpotWithOutToken() throws Exception {
    /* Given */
    String pickerId = "123";
    String roomId = "a123";

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot/{token}", " ")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", pickerId)
                    .header("X-ROOM-ID", roomId)
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

}
