package com.kakaopay.sprinkling.repository;

import com.kakaopay.sprinkling.domain.DividedMoneyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividedMoneyRepository extends JpaRepository<DividedMoneyEntity, Long> {

}
