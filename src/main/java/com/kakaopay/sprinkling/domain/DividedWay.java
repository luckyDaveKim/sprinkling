package com.kakaopay.sprinkling.domain;

import java.util.List;

public interface DividedWay {

  List<Money> divide(Money money, PickerCount size);

}
