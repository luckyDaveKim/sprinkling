package com.kakaopay.sprinkling.service.sprinkling;

import com.kakaopay.sprinkling.api.response.CheckingJackpotResponse;
import com.kakaopay.sprinkling.common.error.exception.ExpiredCheckMoneyException;
import com.kakaopay.sprinkling.common.error.exception.SprinkledMoneyNotFoundException;
import com.kakaopay.sprinkling.domain.*;
import com.kakaopay.sprinkling.repository.DividedMoneyRepository;
import com.kakaopay.sprinkling.repository.SprinkledMoneyRepository;
import com.kakaopay.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CheckJackpotTests {

  @Mock
  private SprinkledMoneyRepository sprinkledMoneyRepository;

  @Mock
  private DividedMoneyRepository dividedMoneyRepository;

  @Mock
  private DividedMoneyEntity dividedMoney;

  @Mock
  private SprinkledMoneyEntity sprinkledMoneyEntity;

  @Mock
  private IdDto idDto;

  @InjectMocks
  private SprinklingService sprinklingService;

  @Test
  @DisplayName("뿌리기 조회 정보를 반환함")
  public void shouldCheckJackpot() {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();
    LocalDateTime createdAt = LocalDateTime.now();
    Money money = new Money(10000L);
    Money pickedMoney = new Money(0L);

    given(sprinkledMoneyRepository.findFirstIdByCreatorAndTokenAndRoomIdOrderByCreatedAtDesc(creator, token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    given(sprinkledMoneyEntity.isCheckable())
            .willReturn(true);
    given(sprinkledMoneyEntity.getCreatedAt())
            .willReturn(createdAt);
    given(sprinkledMoneyEntity.getMoney())
            .willReturn(money);
    given(sprinkledMoneyEntity.getPickedMoneySum())
            .willReturn(pickedMoney);

    /* When */
    CheckingJackpotResponse checkingJackpotResponse = sprinklingService.checkJackpot(creator, roomId, token);

    /* Then */
    Assertions.assertEquals(createdAt, checkingJackpotResponse.getSprinklingAt());
    Assertions.assertEquals(money, checkingJackpotResponse.getSprinklingMoney());
    Assertions.assertEquals(pickedMoney, checkingJackpotResponse.getPickedMoney());
  }

  @Test
  @DisplayName("조회할 뿌리기 정보가 있어야함")
  public void shouldThrowWhenNotFoundSprinkledMoney() {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByCreatorAndTokenAndRoomIdOrderByCreatedAtDesc(creator, token, roomId))
            .willReturn(Optional.ofNullable(null));

    /* When */
    /* Then */
    Assertions.assertThrows(SprinkledMoneyNotFoundException.class, () -> sprinklingService.checkJackpot(creator, roomId, token));
  }

  @Test
  @DisplayName("조회 가능 기한에 제한이 있음")
  public void shouldThrowWhenNotCheckable() {
    /* Given */
    UserId creator = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByCreatorAndTokenAndRoomIdOrderByCreatedAtDesc(creator, token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    given(sprinkledMoneyEntity.isCheckable())
            .willReturn(false);

    /* When */
    /* Then */
    Assertions.assertThrows(ExpiredCheckMoneyException.class, () -> sprinklingService.checkJackpot(creator, roomId, token));
  }

}