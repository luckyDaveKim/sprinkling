package com.kakaopay.sprinkling.service;

import com.kakaopay.sprinkling.api.request.SprinklingJackpotRequest;
import com.kakaopay.sprinkling.api.response.CheckingJackpotResponse;
import com.kakaopay.sprinkling.api.response.PickingJackpotResponse;
import com.kakaopay.sprinkling.api.response.SprinklingJackpotResponse;
import com.kakaopay.sprinkling.common.error.exception.ExpiredCheckMoneyException;
import com.kakaopay.sprinkling.common.error.exception.OverheatingSprinkleException;
import com.kakaopay.sprinkling.common.error.exception.SprinkledMoneyNotFoundException;
import com.kakaopay.sprinkling.domain.*;
import com.kakaopay.sprinkling.repository.DividedMoneyRepository;
import com.kakaopay.sprinkling.repository.SprinkledMoneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SprinklingService {

  private final SprinkledMoneyRepository sprinkledMoneyRepository;
  private final DividedMoneyRepository dividedMoneyRepository;

  public SprinklingService(SprinkledMoneyRepository sprinkledMoneyRepository, DividedMoneyRepository dividedMoneyRepository) {
    this.sprinkledMoneyRepository = sprinkledMoneyRepository;
    this.dividedMoneyRepository = dividedMoneyRepository;
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public SprinklingJackpotResponse sprinkleJackpot(UserId creator, RoomId roomId, SprinklingJackpotRequest sprinklingJackpotRequest) {
    Money money = sprinklingJackpotRequest.getMoney();
    PickerCount pickerCount = sprinklingJackpotRequest.getPickerCount();
    SprinkledMoneyEntity sprinkledMoney = new SprinkledMoneyEntity(this.generateUniqToken(roomId), roomId, creator, money, pickerCount);
    sprinkledMoneyRepository.save(sprinkledMoney);

    return SprinklingJackpotResponse.of(sprinkledMoney);
  }

  public Token generateUniqToken(RoomId roomId) {
    final int TOKEN_GENERATE_RETRY_COUNT = 5;

    for (int i = 0; i < TOKEN_GENERATE_RETRY_COUNT; i++) {
      Token token = Token.generate();
      SprinkledMoneyEntity sprinkledMoney;

      try {
        IdDto idDto = sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId)
                .orElseThrow(SprinkledMoneyNotFoundException::new);
        sprinkledMoney = sprinkledMoneyRepository.findById(idDto.getId())
                .orElseThrow(SprinkledMoneyNotFoundException::new);
      } catch (SprinkledMoneyNotFoundException e) {
        return token;
      }

      boolean isQuiqToken = sprinkledMoney.isExpired();
      if (isQuiqToken) {
        return token;
      }
    }

    throw new OverheatingSprinkleException();
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public PickingJackpotResponse pickJackpot(UserId picker, RoomId roomId, Token token) {
    IdDto idDto = sprinkledMoneyRepository.findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(token, roomId)
            .orElseThrow(SprinkledMoneyNotFoundException::new);
    SprinkledMoneyEntity sprinkledMoneyEntity = sprinkledMoneyRepository.findById(idDto.getId())
            .orElseThrow(SprinkledMoneyNotFoundException::new);

    DividedMoneyEntity dividedMoney = dividedMoneyRepository.save(sprinkledMoneyEntity.pick(picker));

    return PickingJackpotResponse.of(dividedMoney);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  public CheckingJackpotResponse checkJackpot(UserId creator, RoomId roomId, Token token) {
    IdDto idDto = sprinkledMoneyRepository.findFirstIdByCreatorAndTokenAndRoomIdOrderByCreatedAtDesc(creator, token, roomId)
            .orElseThrow(SprinkledMoneyNotFoundException::new);
    SprinkledMoneyEntity sprinkledMoneyEntity = sprinkledMoneyRepository.findById(idDto.getId())
            .orElseThrow(SprinkledMoneyNotFoundException::new);

    if (!sprinkledMoneyEntity.isCheckable()) {
      throw new ExpiredCheckMoneyException();
    }

    return CheckingJackpotResponse.of(sprinkledMoneyEntity);
  }

}
