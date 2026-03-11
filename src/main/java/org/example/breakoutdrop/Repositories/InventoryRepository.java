package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findFirstByUserAndSkin(User user, Skin skin);

    List<Inventory> findAllByUserAndSkinIn(User user, List<Skin> skins);


}
