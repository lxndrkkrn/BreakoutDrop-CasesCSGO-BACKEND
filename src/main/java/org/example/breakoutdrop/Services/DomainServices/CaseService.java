package org.example.breakoutdrop.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.CreateCaseDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.CaseRepository;
import org.example.breakoutdrop.Repositories.SkinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j

public class CaseService {

    private final SkinRepository skinRepository;
    private final CaseRepository caseRepository;

    public CaseService(SkinRepository skinRepository, CaseRepository caseRepository) {
        this.skinRepository = skinRepository;
        this.caseRepository = caseRepository;
    }

    @Transactional
    public void createCase(CreateCaseDTO createCaseDTO) {
        log.info("Попытка создания кейса");
        try {
            Case newCase = new Case();
            List<Skin> skinList = skinRepository.findAllById(createCaseDTO.skinIds());
            if (skinList.isEmpty()) {
                throw new NotFound404("Скины не найдены");
            }

            newCase.setPrice(createCaseDTO.price());
            newCase.setName(createCaseDTO.name());
            newCase.setSkinList(skinList);

            caseRepository.save(newCase);

            log.info("Кейс успешно создан");
        } catch (Exception e) {
            log.error("Ошибка при создании кейса");
            throw e;
        }
    }

    @Transactional
    public void deleteCase(Long id) {
        log.info("Попытка удаления кейса");
        try {
            Case newCase = caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));

            caseRepository.delete(newCase);

            log.info("Кейс успешно удален");
        } catch (Exception e) {
            log.error("Ошибка при удалении кейса");
            throw e;
        }
    }

    @Transactional
    public void setPriceToCase(Long id, BigDecimal newPrice) {
        log.info("Попытка установки цены для кейса");
        try {
            Case newCase = caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));

            newCase.setPrice(newPrice);

            caseRepository.save(newCase);

            log.info("Цена кейса успешно изменена");
        } catch (Exception e) {
            log.error("Ошибка при установки цены на кейс");
            throw e;
        }
    }

    @Transactional
    public void setNameToCase(Long id, String newName) {
        log.info("Попытка установки нового названия кейса");
        try {
            Case newCase = caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));

            newCase.setName(newName);

            caseRepository.save(newCase);

            log.info("Название кейса успешно изменена");
        } catch (Exception e) {
            log.error("Ошибка при установки названия кейса");
            throw e;
        }
    }

}
