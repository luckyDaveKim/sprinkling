package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.FailedPickException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "divided_money",
        indexes = @Index(
                name = "i_divided_money",
                columnList = "sprinkled_money_id"
        )
)
public class DividedMoneyEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "money")
  @Convert(converter = Money.MoneyConverter.class)
  private Money money;

  @Column(name = "picker")
  @Convert(converter = UserId.UserIdConverter.class)
  private UserId picker;

  @Column(name = "picked_at")
  private LocalDateTime pickedAt;

  @ManyToOne
  @JoinColumn(name = "sprinkled_money_id")
  private SprinkledMoneyEntity sprinkledMoney;

  public DividedMoneyEntity(Money money, SprinkledMoneyEntity sprinkledMoney) {
    this.money = money;
    this.setSprinkledMoney(sprinkledMoney);
  }

  private void setSprinkledMoney(SprinkledMoneyEntity sprinkledMoney) {
    this.sprinkledMoney = sprinkledMoney;

    if (!sprinkledMoney.getDividedMoneys().contains(this)) {
      sprinkledMoney.getDividedMoneys().add(this);
    }
  }

  public static DividedMoneyEntity of(Money money, SprinkledMoneyEntity sprinkledMoney) {
    return new DividedMoneyEntity(money, sprinkledMoney);
  }

  public DividedMoneyEntity pick(UserId picker) {
    if (this.isPicked()) {
      throw new FailedPickException();
    }

    this.picker = picker;
    this.pickedAt = LocalDateTime.now();

    return this;
  }

  public boolean isPicked() {
    return this.picker != null;
  }

  public boolean isPicker(UserId picker) {
    return this.isPicked() && this.picker.equals(picker);
  }

  public UserId getPicker() {
    return this.picker;
  }

}
