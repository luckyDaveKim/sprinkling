package com.kakaopay.sprinkling.service.sprinkling;

import com.kakaopay.sprinkling.api.response.PickingJackpotResponse;
import com.kakaopay.sprinkling.common.error.exception.*;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
public class PickJackpotTests {

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
  @DisplayName("줍기가 가능하다")
  public void shouldPickJackpot() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();
    Money money = new Money(100L);

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    given(dividedMoneyRepository.save(any()))
            .willReturn(dividedMoney);
    given(dividedMoney.getMoney())
            .willReturn(money);

    /* When */
    PickingJackpotResponse pickingJackpotResponse = sprinklingService.pickJackpot(picker, roomId, token);

    /* Then */
    Assertions.assertEquals(money, pickingJackpotResponse.getMoney());
  }

  @Test
  @DisplayName("주울 뿌리기 정보가 있어야함")
  public void shouldThrowNotFoundSprinkledMoney() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.ofNullable(null));

    /* When */
    /* Then */
    Assertions.assertThrows(SprinkledMoneyNotFoundException.class, () -> sprinklingService.pickJackpot(picker, roomId, token));
  }

  @Test
  @DisplayName("뿌린이는 줍기가 불가능함")
  public void shouldThrowPickCreator() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    willThrow(new CreatorCannotPickMoneyException())
            .given(sprinkledMoneyEntity).pick(any());

    /* When */
    /* Then */
    Assertions.assertThrows(CreatorCannotPickMoneyException.class, () -> sprinklingService.pickJackpot(picker, roomId, token));
  }

  @Test
  @DisplayName("줍기는 만료 기간이 있음")
  public void shouldThrowPickExpired() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    willThrow(new ExpiredPickMoneyException())
            .given(sprinkledMoneyEntity).pick(any());

    /* When */
    /* Then */
    Assertions.assertThrows(ExpiredPickMoneyException.class, () -> sprinklingService.pickJackpot(picker, roomId, token));
  }

  @Test
  @DisplayName("줍기는 뿌리기당 한번만 가능함")
  public void shouldThrowPickParticipated() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    willThrow(new AlreadyParticipatedPickMoneyException())
            .given(sprinkledMoneyEntity).pick(any());

    /* When */
    /* Then */
    Assertions.assertThrows(AlreadyParticipatedPickMoneyException.class, () -> sprinklingService.pickJackpot(picker, roomId, token));
  }

  @Test
  @DisplayName("모든 줍기가 끝나면 더이상 주울 수 없음")
  public void shouldThrowAllPicked() {
    /* Given */
    UserId picker = new UserId(1L);
    RoomId roomId = new RoomId("dave-1");
    Token token = Token.generate();

    given(sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId))
            .willReturn(Optional.of(idDto));
    given(sprinkledMoneyRepository.findById(any()))
            .willReturn(Optional.of(sprinkledMoneyEntity));
    willThrow(new AllPickedMoneyException())
            .given(sprinkledMoneyEntity).pick(any());

    /* When */
    /* Then */
    Assertions.assertThrows(AllPickedMoneyException.class, () -> sprinklingService.pickJackpot(picker, roomId, token));
  }

}
