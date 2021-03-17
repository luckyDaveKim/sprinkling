package com.kakaopay.sprinkling.repository;

import com.kakaopay.sprinkling.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class DividedMoneyRepositoryTests {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private DividedMoneyRepository dividedMoneyRepository;

  @Test
  @DisplayName("돈 주운 정보를 저장하고 조회 가능")
  public void shouldSaveAndFindDividedMoney() {
    /* Given */
    Token token = Token.generate();
    RoomId roomId = new RoomId("dave-1");
    UserId creator = new UserId(1L);
    Money money = Money.of(10000L);
    PickerCount pickerCount = new PickerCount(10L);

    SprinkledMoneyEntity sprinkledMoney = new SprinkledMoneyEntity(token, roomId, creator, money, pickerCount);
    DividedMoneyEntity dividedMoney = sprinkledMoney.getDividedMoneys().get(0);

    /* When */
    testEntityManager.persist(dividedMoney);

    /* Then */
    Assertions.assertEquals(dividedMoney, dividedMoneyRepository.findById(dividedMoney.getId()).get());
  }

}
