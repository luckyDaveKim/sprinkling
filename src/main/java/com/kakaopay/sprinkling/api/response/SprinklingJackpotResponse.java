package com.kakaopay.sprinkling.api.response;

import com.kakaopay.sprinkling.domain.SprinkledMoneyEntity;
import com.kakaopay.sprinkling.domain.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SprinklingJackpotResponse {

  private final Token token;

  @Builder
  public SprinklingJackpotResponse(Token token) {
    this.token = token;
  }

  public static SprinklingJackpotResponse of(SprinkledMoneyEntity sprinkledMoney) {
    return SprinklingJackpotResponse.builder()
            .token(sprinkledMoney.getToken())
            .build();
  }

}
