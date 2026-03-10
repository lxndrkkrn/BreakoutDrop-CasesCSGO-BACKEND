package org.example.breakoutdrop.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.CreateSkinDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.CaseRepository;
import org.example.breakoutdrop.Repositories.SkinRepository;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j

public class SkinService {

    private final UserRepository userRepository;
    private final SkinRepository skinRepository;
    private final CaseRepository caseRepository;

    public SkinService(UserRepository userRepository, SkinRepository skinRepository, CaseRepository caseRepository) {
        this.userRepository = userRepository;
        this.skinRepository = skinRepository;
        this.caseRepository = caseRepository;
    }

    @Transactional
    public void createSkin(CreateSkinDTO createSkinDTO) {
        log.info("Попытка созданрия скина");
        try {
            Skin skin = new Skin();
            List<Case> parentCase = caseRepository.findAllById(createSkinDTO.caseIds());

            skin.setType(createSkinDTO.weaponType());
            skin.setName(createSkinDTO.name());
            skin.setRarity(createSkinDTO.rarity());
            skin.setPrice(createSkinDTO.price());
            skin.setCases(parentCase);

            skinRepository.save(skin);

            log.info("Скин успешно создан");
        } catch (Exception e) {
            log.error("Ошибка при создании скина");
            throw e;
        }
    }

    @Transactional
    public void deleteSkin(Long id) {
        log.info("Попытка удаления скина");
        try {
            Skin skin = skinRepository.findById(id).orElseThrow(() -> new NotFound404("Скин не найден"));

            skinRepository.delete(skin);

            log.info("Скин успешно удалён");
        } catch (Exception e) {
            log.error("Ошибка при удалении скина");
            throw e;
        }
    }

    @Transactional
    public void setPrice(Long id, BigDecimal newPrice) {
        log.info("Попытка изменения цены скина");
        try {
            Skin skin = skinRepository.findById(id).orElseThrow(() -> new NotFound404("Скин не найден"));

            skin.setPrice(newPrice);

            skinRepository.save(skin);

            log.info("Цена скина успешно изменена");
        } catch (Exception e) {
            log.error("Ошибка при изменении цены скина");
            throw e;
        }
    }

    @Transactional
    public void addSkinInCase(Long id, List<Long> caseList) {
        log.info("Попытка добавления скина в кейс");
        try {
            Skin skin = skinRepository.findById(id).orElseThrow(() -> new NotFound404("Скин не найден"));
            List<Case> targetCases = caseRepository.findAllById(caseList);

            if (targetCases.isEmpty()) {
                throw new NotFound404("Кейсы не найдены");
            }

            skin.getCases().addAll(targetCases);

            skinRepository.save(skin);

            log.info("Скин успешно добавлен в кенйсы");
        } catch (Exception e) {
            log.error("Ошибка при добавлении скина в кейсы");
            throw e;
        }
    }

    @Transactional
    public void removeSkinInCase(Long id, List<Long> caseList) {
        log.info("Попытка удаления скина из кейса");
        try {
            Skin skin = skinRepository.findById(id).orElseThrow(() -> new NotFound404("Скин не найден"));
            List<Case> targetCases = caseRepository.findAllById(caseList);

            if (targetCases.isEmpty()) {
                throw new NotFound404("Кейсы не найдены");
            }

            skin.getCases().removeAll(targetCases);

            skinRepository.save(skin);

            log.info("Скин успешно удален из кейса");
        } catch (Exception e) {
            log.error("Ошибка при удалении скина из кейса");
            throw e;
        }
    }

}
