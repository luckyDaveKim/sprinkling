package com.kakaopay.sprinkling.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class JackpotDividedWay implements DividedWay {

  @Override
  public List<Money> divide(Money money, PickerCount size) {
    List<Money> dividedMoneys = new ArrayList<>();

    long jackpotIndex = ThreadLocalRandom.current().nextLong(size.getValue());

    for (long i = 0; i < size.getValue(); i++) {
      boolean isJackpot = i == jackpotIndex;
      Money curMoney = isJackpot ? money : Money.of(0L);
      dividedMoneys.add(curMoney);
    }

    return dividedMoneys;
  }

}
