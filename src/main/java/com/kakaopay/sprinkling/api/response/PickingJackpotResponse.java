package com.kakaopay.sprinkling.api.response;

import com.kakaopay.sprinkling.domain.DividedMoneyEntity;
import com.kakaopay.sprinkling.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PickingJackpotResponse {

  private final Money money;

  @Builder
  public PickingJackpotResponse(Money money) {
    this.money = money;
  }

  public static PickingJackpotResponse of(DividedMoneyEntity dividedMoney) {
    return PickingJackpotResponse.builder()
            .money(dividedMoney.getMoney())
            .build();
  }

}
