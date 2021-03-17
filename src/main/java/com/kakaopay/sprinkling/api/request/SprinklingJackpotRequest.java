package com.kakaopay.sprinkling.api.request;

import com.kakaopay.sprinkling.domain.Money;
import com.kakaopay.sprinkling.domain.PickerCount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SprinklingJackpotRequest {

  @NotNull(message = "뿌릴 금액을 입력하세요.")
  private Money money;

  @NotNull(message = "뿌릴 인원을 입력하세요.")
  private PickerCount pickerCount;

  @Builder
  public SprinklingJackpotRequest(Money money, PickerCount pickerCount) {
    this.money = money;
    this.pickerCount = pickerCount;
  }

}
