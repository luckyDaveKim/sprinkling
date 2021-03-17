package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidMoneyException;
import com.kakaopay.sprinkling.common.error.exception.TooLargeMoneyException;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigInteger;

@Getter
public class Money extends SingleValue<BigInteger> {

  private static final BigInteger MAX_AMOUNT = BigInteger.valueOf(1_000_000_000_000L);

  public Money(BigInteger money) {
    super(money);
    this.validate(money);
  }

  public Money(Long money) {
    super(BigInteger.valueOf(money));
  }

  private void validate(BigInteger money) {
    if (money == null) {
      throw new InvalidMoneyException();
    }

    boolean isNegative = BigInteger.ZERO.compareTo(money) > 0;
    if (isNegative) {
      throw new InvalidMoneyException(String.format("돈의 값은 음수일 수 없습니다. '%d'", money));
    }

    boolean isTooLarge = MAX_AMOUNT.compareTo(money) < 0;
    if (isTooLarge) {
      throw new TooLargeMoneyException(money);
    }
  }

  public static Money of(Long money) {
    validate(money);
    return new Money(BigInteger.valueOf(money));
  }

  private static void validate(Long money) {
    if (money == null) {
      throw new InvalidMoneyException();
    }
  }

  public Money add(Money money) {
    BigInteger amount = this.value.add(money.getValue());
    return new Money(amount);
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Converter
  public static class MoneyConverter implements AttributeConverter<Money, BigInteger> {

    @Override
    public BigInteger convertToDatabaseColumn(Money attribute) {
      return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Money convertToEntityAttribute(BigInteger dbData) {
      return dbData == null ? null : new Money(dbData);
    }

  }

}
