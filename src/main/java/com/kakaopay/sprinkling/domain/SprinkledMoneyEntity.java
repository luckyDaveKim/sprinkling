package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.AllPickedMoneyException;
import com.kakaopay.sprinkling.common.error.exception.AlreadyParticipatedPickMoneyException;
import com.kakaopay.sprinkling.common.error.exception.CreatorCannotPickMoneyException;
import com.kakaopay.sprinkling.common.error.exception.ExpiredPickMoneyException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "sprinkled_money",
        indexes = @Index(
                name = "i_sprinkled_money",
                columnList = "token, room_id"
        )
)
public class SprinkledMoneyEntity {

  private static final long SURVIVAL_MINUTES = 10L;
  private static final long CHECKABLE_DAYS = 7L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "token")
  @Convert(converter = Token.TokenConverter.class)
  private Token token;

  @Column(name = "room_id")
  @Convert(converter = RoomId.RoomIdConverter.class)
  private RoomId roomId;

  @Column(name = "money")
  @Convert(converter = Money.MoneyConverter.class)
  private Money money;

  @Column(name = "picker_count")
  @Convert(converter = PickerCount.PickerCountConverter.class)
  private PickerCount pickerCount;

  @Column(name = "creator")
  @Convert(converter = UserId.UserIdConverter.class)
  private UserId creator;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToMany(
          mappedBy = "sprinkledMoney",
          fetch = FetchType.LAZY,
          cascade = CascadeType.ALL
  )
  private List<DividedMoneyEntity> dividedMoneys = new ArrayList<>();

  public SprinkledMoneyEntity(Token token, RoomId roomId, UserId creator, Money money, PickerCount pickerCount) {
    this.token = token;
    this.roomId = roomId;
    this.money = money;
    this.pickerCount = pickerCount;
    this.creator = creator;
    this.createdAt = LocalDateTime.now();
    this.dividedMoneys = this.generateDividedMoneys(new JackpotDividedWay());
  }

  private List<DividedMoneyEntity> generateDividedMoneys(DividedWay dividedWay) {
    List<Money> dividedMoneys = dividedWay.divide(this.money, this.pickerCount);
    return dividedMoneys.stream()
            .map(curMoney -> DividedMoneyEntity.of(curMoney, this))
            .collect(Collectors.toList());
  }

  public DividedMoneyEntity pick(UserId picker) {
    if (this.isCreator(picker)) {
      throw new CreatorCannotPickMoneyException();
    }

    if (this.isExpired()) {
      throw new ExpiredPickMoneyException();
    }

    if (this.isParticipated(picker)) {
      throw new AlreadyParticipatedPickMoneyException();
    }

    DividedMoneyEntity dividedMoney = this.dividedMoneys.stream()
            .filter(not(DividedMoneyEntity::isPicked))
            .findFirst()
            .orElseThrow(AllPickedMoneyException::new);

    return dividedMoney.pick(picker);
  }

  public boolean isParticipated(UserId picker) {
    return this.dividedMoneys.stream()
            .anyMatch(dividedMoney -> dividedMoney.isPicker(picker));
  }

  public boolean isCreator(UserId creator) {
    return this.creator.equals(creator);
  }

  public boolean isExpired() {
    LocalDateTime expiredDateTime = this.createdAt.plusMinutes(SURVIVAL_MINUTES);
    return LocalDateTime.now().isAfter(expiredDateTime);
  }

  public boolean isCheckable() {
    LocalDateTime expiredDateTime = this.createdAt.plusDays(CHECKABLE_DAYS);
    return !LocalDateTime.now().isAfter(expiredDateTime);

  }

  public Money getPickedMoneySum() {
    return this.getPickedDividedMoneys().stream()
            .map(DividedMoneyEntity::getMoney)
            .reduce(Money.of(0L), Money::add);
  }

  public List<DividedMoneyEntity> getPickedDividedMoneys() {
    return this.dividedMoneys.stream()
            .filter(DividedMoneyEntity::isPicked)
            .collect(Collectors.toList());
  }

}
