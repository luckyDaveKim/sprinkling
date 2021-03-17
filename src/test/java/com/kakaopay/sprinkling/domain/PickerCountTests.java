package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidPickerCountException;
import com.kakaopay.sprinkling.common.error.exception.TooLargePickerCountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PickerCountTests {

  @Test
  @DisplayName("받기 인원 생성")
  public void shouldCreatePickerCount() {
    /* Given */
    long pickerSize = 100L;

    /* When */
    PickerCount pickerCount = new PickerCount(pickerSize);

    /* Then */
    Assertions.assertEquals(100L, pickerCount.getValue());
  }

  @Test
  @DisplayName("받기 인원수는 null일 수 없음")
  public void shouldThrowNullPickerCount() {
    /* Given */
    Long pickerSize = null;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidPickerCountException.class, () -> new PickerCount(pickerSize));
  }

  @Test
  @DisplayName("받기 인원수는 음수일 수 없음")
  public void shouldThrowNegativePickerCount() {
    /* Given */
    long pickerSize = -100L;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidPickerCountException.class, () -> new PickerCount(pickerSize));
  }

  @Test
  @DisplayName("받기 인원수는 제한이 있음")
  public void shouldThrowLargePickerCount() {
    /* Given */
    long pickerSize = 10000L;

    /* When */
    /* Then */
    Assertions.assertThrows(TooLargePickerCountException.class, () -> new PickerCount(pickerSize));
  }

}
