package com.kakaopay.sprinkling.repository;

import com.kakaopay.sprinkling.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SprinkledMoneyRepository extends JpaRepository<SprinkledMoneyEntity, Long> {

  Optional<IdDto> findFirstIdByTokenAndRoomIdOrderByCreatedAtDesc(Token token, RoomId roomId);

  Optional<IdDto> findFirstIdByCreatorAndTokenAndRoomIdOrderByCreatedAtDesc(UserId creator, Token token, RoomId roomId);

  @EntityGraph(attributePaths = {"dividedMoneys"})
  Optional<SprinkledMoneyEntity> findById(Long id);

}
