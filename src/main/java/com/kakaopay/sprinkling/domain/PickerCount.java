package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidPickerCountException;
import com.kakaopay.sprinkling.common.error.exception.TooLargePickerCountException;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Getter
public class PickerCount extends SingleValue<Long> {

  private static final long MAX_COUNT = 9999L;

  public PickerCount(Long pickerCount) {
    super(pickerCount);
    this.validate(pickerCount);
  }

  private void validate(Long pickerCount) {
    if (pickerCount == null) {
      throw new InvalidPickerCountException();
    }

    boolean isNegative = pickerCount < 0;
    if (isNegative) {
      throw new InvalidPickerCountException(String.format("뿌릴 인원의 값은 음수일 수 없습니다. '%d'", pickerCount));
    }

    boolean isTooLarge = pickerCount > MAX_COUNT;
    if (isTooLarge) {
      throw new TooLargePickerCountException(pickerCount);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Converter
  public static class PickerCountConverter implements AttributeConverter<PickerCount, Long> {

    @Override
    public Long convertToDatabaseColumn(PickerCount attribute) {
      return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PickerCount convertToEntityAttribute(Long dbData) {
      return dbData == null ? null : new PickerCount(dbData);
    }

  }

}
