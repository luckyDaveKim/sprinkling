package com.kakaopay.sprinkling.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

public class JackpotDividedWayTests {

  @Test
  @DisplayName("분배 전과 후의 총합은 같음")
  public void shouldCreateJackpotDivideWay() {
    /* Given */
    Money money = Money.of(100L);
    PickerCount pickerCount = new PickerCount(5L);

    /* When */
    JackpotDividedWay jackpotDivideWay = new JackpotDividedWay();
    List<Money> divideMoneys = jackpotDivideWay.divide(money, pickerCount);

    /* Then */
    BigInteger moneySum = divideMoneys.stream().map(Money::getValue).reduce(BigInteger.ZERO, BigInteger::add);
    Assertions.assertEquals(BigInteger.valueOf(100L), moneySum);
    Assertions.assertEquals(5, divideMoneys.size());
  }

  @Test
  @DisplayName("Jackpot 형식의 분배는 한사람만 당첨이 가능함")
  public void shouldHaveOneJackpotTarget() {
    /* Given */
    Money money = Money.of(100L);
    PickerCount pickerCount = new PickerCount(5L);

    /* When */
    JackpotDividedWay jackpotDivideWay = new JackpotDividedWay();
    List<Money> divideMoneys = jackpotDivideWay.divide(money, pickerCount);

    /* Then */
    long jackpotCount = divideMoneys.stream().map(Money::getValue).filter(BigInteger.valueOf(100L)::equals).count();
    Assertions.assertEquals(1, jackpotCount);
  }

}
