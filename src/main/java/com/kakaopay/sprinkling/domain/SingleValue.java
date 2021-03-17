package com.kakaopay.sprinkling.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class SingleValue<T> {

  @JsonValue
  protected T value;

  public SingleValue(T value) {
    this.value = value;
  }

}
