package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Skin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Long> {
    List<Skin> findAllById(Long id);
}
