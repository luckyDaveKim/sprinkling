package com.kakaopay.sprinkling.api;

import com.kakaopay.sprinkling.api.request.SprinklingJackpotRequest;
import com.kakaopay.sprinkling.api.response.CheckingJackpotResponse;
import com.kakaopay.sprinkling.api.response.PickingJackpotResponse;
import com.kakaopay.sprinkling.api.response.SprinklingJackpotResponse;
import com.kakaopay.sprinkling.common.error.exception.*;
import com.kakaopay.sprinkling.domain.RoomId;
import com.kakaopay.sprinkling.domain.Token;
import com.kakaopay.sprinkling.domain.UserId;
import com.kakaopay.sprinkling.service.SprinklingService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SprinklingApi {

  private final SprinklingService sprinklingService;

  public SprinklingApi(SprinklingService sprinklingService) {
    this.sprinklingService = sprinklingService;
  }

  @PostMapping("/sprinkling/jackpot")
  public SprinklingJackpotResponse sprinkleJackpot(HttpServletRequest req, @Valid @RequestBody SprinklingJackpotRequest sprinklingJackpotRequest) {
    UserId creator = UserId.of(req);
    RoomId roomId = RoomId.of(req);

    return sprinklingService.sprinkleJackpot(creator, roomId, sprinklingJackpotRequest);
  }

  @PostMapping("/sprinkling/jackpot/{token}")
  public PickingJackpotResponse pickJackpot(HttpServletRequest req, @PathVariable(name = "token") String tokenText) {
    try {
      UserId picker = UserId.of(req);
      RoomId roomId = RoomId.of(req);
      Token token = new Token(tokenText);

      return sprinklingService.pickJackpot(picker, roomId, token);
    } catch (ExpiredPickMoneyException e) {
      throw new FailedPickException(e);
    }
  }

  @GetMapping("/sprinkling/jackpot/{token}")
  public CheckingJackpotResponse checkJackpot(HttpServletRequest req, @PathVariable(name = "token") String tokenText) {
    try {
      UserId creator = UserId.of(req);
      RoomId roomId = RoomId.of(req);
      Token token = new Token(tokenText);

      return sprinklingService.checkJackpot(creator, roomId, token);
    } catch (SprinkledMoneyNotFoundException | InvalidTokenException e) {
      throw new FailedCheckException(e);
    }
  }

}
