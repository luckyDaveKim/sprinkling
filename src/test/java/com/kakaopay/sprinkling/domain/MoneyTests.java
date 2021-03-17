package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidMoneyException;
import com.kakaopay.sprinkling.common.error.exception.TooLargeMoneyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class MoneyTests {

  @Test
  @DisplayName("돈 생성")
  public void shouldCreateMoney() {
    /* Given */
    long moneyAmount = 100L;

    /* When */
    Money money = Money.of(moneyAmount);

    /* Then */
    Assertions.assertEquals(BigInteger.valueOf(100L), money.getValue());
  }

  @Test
  @DisplayName("동일한 금액의 돈은 가치가 동일함")
  public void shouldEqualMoney() {
    /* Given */
    BigInteger bigIntegerAmount = BigInteger.valueOf(100L);
    Long longAmount = 100L;

    /* When */
    Money money1 = new Money(bigIntegerAmount);
    Money money2 = Money.of(longAmount);

    /* Then */
    Assertions.assertEquals(money1, money2);
  }

  @Test
  @DisplayName("돈은 null일 수 없음")
  public void shouldThrowNullMoney() {
    /* Given */
    BigInteger bigIntegerAmount = null;
    Long longAmount = null;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidMoneyException.class, () -> new Money(bigIntegerAmount));
    Assertions.assertThrows(InvalidMoneyException.class, () -> Money.of(longAmount));
  }

  @Test
  @DisplayName("돈은 음수일 수 없음")
  public void shouldThrowNegativeMoney() {
    /* Given */
    long moneyAmount = -100L;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidMoneyException.class, () -> Money.of(moneyAmount));
  }

  @Test
  @DisplayName("뿌릴 수 있는 돈의 크기는 제한되어 있음")
  public void shouldThrowLargeMoney() {
    /* Given */
    long moneyAmount = 10_000_000_000_000L;

    /* When */
    /* Then */
    Assertions.assertThrows(TooLargeMoneyException.class, () -> Money.of(moneyAmount));
  }

  @Test
  @DisplayName("돈끼리 합산하여 계산할 수 있음")
  public void shouldAddMoney() {
    /* Given */
    Long longAmount1 = 100L;
    Long longAmount2 = 10L;

    /* When */
    Money money1 = Money.of(longAmount1);
    Money money2 = Money.of(longAmount2);

    /* Then */
    Assertions.assertEquals(Money.of(110L), money1.add(money2));
  }

}
