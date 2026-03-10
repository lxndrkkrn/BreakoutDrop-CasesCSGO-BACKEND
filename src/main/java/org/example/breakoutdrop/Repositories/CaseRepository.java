package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.Case;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, Long> {
}
