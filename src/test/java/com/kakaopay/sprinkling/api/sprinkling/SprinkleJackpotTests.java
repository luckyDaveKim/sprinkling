package com.kakaopay.sprinkling.api.sprinkling;

import com.google.gson.Gson;
import com.kakaopay.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class SprinkleJackpotTests {

  @MockBean
  private SprinklingService sprinklingService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  @Test
  @DisplayName("뿌리기 가능함")
  public void shouldSprinkleJackpot() throws Exception {
    /* Given */
    String creatorId = "123";
    String roomId = "a123";
    Map<String, Object> body = new HashMap<>();
    body.put("money", 10);
    body.put("pickerCount", 20);

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creatorId)
                    .header("X-ROOM-ID", roomId)
                    .content(gson.toJson(body))
    );

    /* Then */
    resultActions.andExpect(status().isOk())
            .andDo(print());
  }

  @Test
  @DisplayName("뿌리기는 뿌리는 사람 정보가 필요함")
  public void shouldThrowSprinkleJackpotWithOutCreatorId() throws Exception {
    /* Given */
    String roomId = "a123";
    Map<String, Object> body = new HashMap<>();
    body.put("money", 10);
    body.put("pickerCount", 20);

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-ROOM-ID", roomId)
                    .content(gson.toJson(body))
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

  @Test
  @DisplayName("뿌리기는 대화방 정보가 필요함")
  public void shouldThrowSprinkleJackpotWithOutRoomId() throws Exception {
    /* Given */
    String creatorId = "123";
    Map<String, Object> body = new HashMap<>();
    body.put("money", 10);
    body.put("pickerCount", 20);

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creatorId)
                    .content(gson.toJson(body))
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

  @Test
  @DisplayName("뿌리기는 금액 정보가 필요함")
  public void shouldThrowSprinkleJackpotWithOutMoney() throws Exception {
    /* Given */
    String creatorId = "123";
    String roomId = "a123";
    Map<String, Object> body = new HashMap<>();
    body.put("pickerCount", 20);

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creatorId)
                    .header("X-ROOM-ID", roomId)
                    .content(gson.toJson(body))
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

  @Test
  @DisplayName("뿌리기는 받는사람 수 정보가 필요함")
  public void shouldThrowSprinkleJackpotWithOutPickerCount() throws Exception {
    /* Given */
    String creatorId = "123";
    String roomId = "a123";
    Map<String, Object> body = new HashMap<>();
    body.put("money", 10);

    /* When */
    ResultActions resultActions = this.mockMvc.perform(
            post("/api/v1/sprinkling/jackpot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", creatorId)
                    .header("X-ROOM-ID", roomId)
                    .content(gson.toJson(body))
    );

    /* Then */
    resultActions.andExpect(status().is4xxClientError())
            .andDo(print());
  }

}
