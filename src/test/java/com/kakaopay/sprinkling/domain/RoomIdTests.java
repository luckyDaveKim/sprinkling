package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidRoomIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomIdTests {

  @Test
  @DisplayName("대화방은 문자형태로 이루어질 수 있음")
  public void shouldCreateNumberRoomId() {
    /* Given */
    String roomIdText1 = "123";
    String roomIdText2 = "a123";
    String roomIdText3 = "abc";

    /* When */
    RoomId roomId1 = new RoomId(roomIdText1);
    RoomId roomId2 = new RoomId(roomIdText2);
    RoomId roomId3 = new RoomId(roomIdText3);

    /* Then */
    Assertions.assertEquals(roomIdText1, roomId1.getValue());
    Assertions.assertEquals(roomIdText2, roomId2.getValue());
    Assertions.assertEquals(roomIdText3, roomId3.getValue());
  }

  @Test
  @DisplayName("대화방은 공백이 포함될 수 있음")
  public void shouldCreateRoomIdWithSpace() {
    /* Given */
    String roomIdText = "1 23";

    /* When */
    RoomId roomId = new RoomId(roomIdText);

    /* Then */
    Assertions.assertEquals(roomIdText, roomId.getValue());
  }

  @Test
  @DisplayName("대화방은 빈문자일 수 없음")
  public void shouldThrowInvalidEmptyRoomId() {
    /* Given */
    String roomIdText = "";

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidRoomIdException.class, () -> new RoomId(roomIdText));
  }

  @Test
  @DisplayName("대화방은 null일 수 없음")
  public void shouldThrowInvalidNullRoomId() {
    /* Given */
    String roomIdText = null;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidRoomIdException.class, () -> new RoomId(roomIdText));
  }

}
