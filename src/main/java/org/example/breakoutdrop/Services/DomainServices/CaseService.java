package org.example.breakoutdrop.Services.DomainServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateCaseDTO;
import org.example.breakoutdrop.DTOs.Response.GetCaseFrontResponseDTO;
import org.example.breakoutdrop.DTOs.Response.GetCaseResponseDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Category;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.CaseRepository;
import org.example.breakoutdrop.Repositories.CategoryRepository;
import org.example.breakoutdrop.Repositories.SkinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor

public class CaseService {

    private final SkinRepository skinRepository;
    private final CaseRepository caseRepository;
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Case createCase(CreateCaseDTO createCaseDTO) {
        log.info("Попытка создания кейса");
        try {
            Case newCase = new Case();
            List<Skin> skinList = skinRepository.findAllById(createCaseDTO.skinIds());
            if (skinList.isEmpty()) {
                throw new NotFound404("Скины не найдены");
            }

            List<Category> categoryList = categoryRepository.findAllById(createCaseDTO.categoryIds());

            newCase.setPrice(createCaseDTO.price());
            newCase.setPictureCase(createCaseDTO.pictureCase());
            newCase.setName(createCaseDTO.name());
            newCase.setSkinList(skinList);
            newCase.setCategoryList(categoryList);

            caseRepository.save(newCase);

            log.info("Кейс успешно создан");
            return newCase;
        } catch (Exception e) {
            log.error("Ошибка при создании кейса");
            throw e;
        }
    }

    @Transactional
    public Case deleteCase(Long id) {
        log.info("Попытка удаления кейса");
        try {
            Case newCase = caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));

            caseRepository.delete(newCase);

            log.info("Кейс успешно удален");
            return newCase;
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

    @Transactional
    public void setCategoryToCase(Long id, List<Long> categoryIds) {
        log.info("Попытка установки категорий для кейса");
        try {
            Case caseEntity = caseRepository.findById(id)
                    .orElseThrow(() -> new NotFound404("Кейс не найден"));

            List<Category> incomingCategories = categoryRepository.findAllById(categoryIds);

            caseEntity.getCategoryList().forEach(cat -> cat.setCaseEntity(null));
            caseEntity.getCategoryList().clear();

            for (Category cat : incomingCategories) {
                cat.setCaseEntity(caseEntity);
                caseEntity.getCategoryList().add(cat);
            }

            caseRepository.save(caseEntity);

            entityManager.flush();
            entityManager.clear();

            log.info("Категории кейса успешно изменены");
        } catch (Exception e) {
            log.error("Ошибка при установки категорий на кейс");
            throw e;
        }
    }

    @Transactional
    public void setPictureToCase(Long id, String url) {
        log.info("Попытка установки картинки для кейса");
        try {
            Case newCase = caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));

            newCase.setPictureCase(url);

            caseRepository.save(newCase);

            log.info("Картинка кейса успешно изменена");
        } catch (Exception e) {
            log.error("Ошибка при установки картинки на кейс");
            throw e;
        }
    }

    public Case findCaseById(Long id) {
        return caseRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));
    }

    public List<GetCaseFrontResponseDTO> findAllCaseForFrontend() {
        List<Case> allCase = caseRepository.findAll();

        return allCase.stream()
                .map(c -> new GetCaseFrontResponseDTO(
                        c.getCategoryList().stream().map(Category::getTitle).collect(Collectors.toList()),
                        c.getId(),
                        c.getName(),
                        c.getPictureCase(),
                        c.getPrice()
                )).collect(Collectors.toList());
    }

    public GetCaseResponseDTO findCaseByIdForFrontend(Long id) {
        Case newCase = findCaseById(id);

        return new GetCaseResponseDTO(
                newCase.getCategoryList().stream().map(Category::getTitle).collect(Collectors.toList()),
                newCase.getId(),
                newCase.getName(),
                newCase.getPictureCase(),
                newCase.getPrice(),
                newCase.getSkinList().stream().map(Skin::getId).collect(Collectors.toList())
        );
    }

    public List<Skin> findAllSkinsInCase(Long id) {
        return caseRepository.findAllById(id);
    }

}
