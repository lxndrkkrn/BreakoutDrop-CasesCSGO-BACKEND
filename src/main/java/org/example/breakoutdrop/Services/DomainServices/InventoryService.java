package org.example.breakoutdrop.Services.DomainServices;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.InventoryRepository;
import org.example.breakoutdrop.Repositories.SkinRepository;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j

public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SkinRepository skinRepository;
    private final UserRepository userRepository;

    public InventoryService(InventoryRepository inventoryRepository, SkinRepository skinRepository, UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.skinRepository = skinRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createInventory(CreateInventoryDTO createInventoryDTO) {
        log.info("Попытка создания инвентаря");
        try {
            Inventory inventory = new Inventory();
            User user = userRepository.findById(createInventoryDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
            Skin skin = skinRepository.findById(createInventoryDTO.skinId()).orElseThrow(() -> new NotFound404("Скин не найден"));

            inventory.setUser(user);
            inventory.setSkin(skin);

            inventoryRepository.save(inventory);

            log.info("Инвентарь успешно создан");
        } catch (Exception e) {
            log.error("Ошибка при создании инвентаря");
            throw e;
        }
    }

    @Transactional
    public void deleteInventory(Inventory inventory) {
        log.info("Попытка удаления инвентаря");
        try {
            inventoryRepository.delete(inventory);

            log.info("Инвентарь успешно удален");
        } catch (Exception e) {
            log.error("Ошибка при удалении инвентаря");
            throw e;
        }
    }

    @Transactional
    public void deleteInventoryById(Long id) {
        log.info("Попытка удаления инвентаря по ID");
        try {
            Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFound404("Инвентарь не найден"));

            inventoryRepository.delete(inventory);

            log.info("Инвентарь успешно удален по ID");
        } catch (Exception e) {
            log.error("Ошибка при удалении инвентаря по ID");
            throw e;
        }
    }

    public Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new NotFound404("Инвентарь не найден"));
    }

    public Inventory findInventoryByUserAndSkin(User user, Skin skin) {
        return inventoryRepository.findFirstByUserAndSkin(user, skin).orElseThrow(() -> new NotFound404("Инвентаря, связующего user: " + user + " и skin: " + skin + " не найден"));
    }
}