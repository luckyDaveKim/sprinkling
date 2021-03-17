package com.kakaopay.sprinkling.service.sprinkling;

import com.kakaopay.sprinkling.api.request.SprinklingJackpotRequest;
import com.kakaopay.sprinkling.api.response.SprinklingJackpotResponse;
import com.kakaopay.sprinkling.common.error.exception.OverheatingSprinkleException;
import com.kakaopay.sprinkling.domain.*;
import com.kakaopay.sprinkling.repository.SprinkledMoneyRepository;
import com.kakaopay.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
public class SprinkleJackpotTests {

  @Mock
  private SprinkledMoneyRepository sprinkledMoneyRepository;

  @Mock
  private SprinkledMoneyEntity sprinkledMoneyEntity;

  @Mock
  private IdDto idDto;

  @InjectMocks
  @Spy
  private SprinklingService sprinklingService;

  @Test
  @DisplayName("뿌리기가 가능함")
  public void shouldSprinkleJackpot() {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);
    SprinklingJackpotRequest sprinklingJackpotRequest = SprinklingJackpotRequest.builder()
            .money(money)
            .pickerCount(pickerCount)
            .build();

    /* When */
    SprinklingJackpotResponse sprinklingJackpotResponse = sprinklingService.sprinkleJackpot(creator, roomId, sprinklingJackpotRequest);

    /* Then */
    Assertions.assertNotNull(sprinklingJackpotResponse.getToken());
  }

  @Test
  @DisplayName("유효한 토큰을 찾지 못하면 뿌리지 못함")
  public void shouldThrowCannotFoundUniqToken() {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);
    SprinklingJackpotRequest sprinklingJackpotRequest = SprinklingJackpotRequest.builder()
            .money(money)
            .pickerCount(pickerCount)
            .build();

    willThrow(new OverheatingSprinkleException())
            .given(sprinklingService).generateUniqToken(any());

    /* When */
    /* Then */
    Assertions.assertThrows(OverheatingSprinkleException.class, () -> sprinklingService.sprinkleJackpot(creator, roomId, sprinklingJackpotRequest));
  }

  @Test
  @DisplayName("유효한 토큰을 찾을 수 있어야 함")
  public void shouldThrowDuplicatedToken() {
    /* Given */
    RoomId roomId = new RoomId("dave-1");
    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(any(), any()))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    given(sprinkledMoneyEntity.isExpired())
            .willReturn(false);

    /* When */
    /* Then */
    Assertions.assertThrows(OverheatingSprinkleException.class, () -> sprinklingService.generateUniqToken(roomId));
  }

}
