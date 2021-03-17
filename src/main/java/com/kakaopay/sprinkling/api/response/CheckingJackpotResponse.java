package com.kakaopay.sprinkling.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakaopay.sprinkling.domain.DividedMoneyEntity;
import com.kakaopay.sprinkling.domain.Money;
import com.kakaopay.sprinkling.domain.SprinkledMoneyEntity;
import com.kakaopay.sprinkling.domain.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CheckingJackpotResponse {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime sprinklingAt;
  private final Money sprinklingMoney;
  private final Money pickedMoney;
  private final List<PickedInfo> pickedInfos;

  @Builder
  public CheckingJackpotResponse(LocalDateTime sprinklingAt, Money sprinklingMoney, Money pickedMoney, List<PickedInfo> pickedInfos) {
    this.sprinklingAt = sprinklingAt;
    this.sprinklingMoney = sprinklingMoney;
    this.pickedMoney = pickedMoney;
    this.pickedInfos = pickedInfos;
  }

  public static CheckingJackpotResponse of(SprinkledMoneyEntity sprinkledMoney) {
    List<PickedInfo> pickedInfos = sprinkledMoney.getPickedDividedMoneys().stream()
            .map(PickedInfo::of)
            .collect(Collectors.toList());

    return CheckingJackpotResponse.builder()
            .sprinklingAt(sprinkledMoney.getCreatedAt())
            .sprinklingMoney(sprinkledMoney.getMoney())
            .pickedMoney(sprinkledMoney.getPickedMoneySum())
            .pickedInfos(pickedInfos)
            .build();
  }

  @Getter
  public static class PickedInfo {

    private final Money money;
    private final UserId picker;

    @Builder
    public PickedInfo(Money money, UserId picker) {
      this.money = money;
      this.picker = picker;
    }

    public static PickedInfo of(DividedMoneyEntity dividedMoney) {
      return PickedInfo.builder()
              .money(dividedMoney.getMoney())
              .picker(dividedMoney.getPicker())
              .build();
    }

  }

}
