package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface SkinRepository extends JpaRepository<Skin, Long> {

    @Query(value = "SELECT * FROM skins s ORDER BY ABS(s.price - :targetPrice) ASC LIMIT 1", nativeQuery = true)
    Optional<Skin> findClosestByPrice(@Param("targetPrice") BigDecimal targetPrice);


}
