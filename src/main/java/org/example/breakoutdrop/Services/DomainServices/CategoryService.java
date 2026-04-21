package org.example.breakoutdrop.Services.DomainServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateCategoryDTO;
import org.example.breakoutdrop.Entities.Category;
import org.example.breakoutdrop.Enums.CategoryType;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.CaseRepository;
import org.example.breakoutdrop.Repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CaseRepository caseRepository;

    @Transactional
    public Category createCategory(CreateCategoryDTO createCategoryDTO) {
        log.info("Попытка созданрия категории кейсов");
        try {
            Category category = new Category();

            category.setTitle(createCategoryDTO.title());
            category.setCategoryType(createCategoryDTO.categoryType());

            categoryRepository.save(category);

            log.info("Категория для кейсов успешно создана");
            return category;
        } catch (Exception e) {
            log.error("Ошибка при создании категории для кейсов");
            throw e;
        }
    }

    @Transactional
    public Category deleteCategory(Long id) {
        log.info("Попытка удалении категории кейсов");
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFound404("Категория не найдена"));

            categoryRepository.delete(category);

            log.info("Категория для кейсов успешно удалена");
            return category;
        } catch (Exception e) {
            log.error("Ошибка при удалении категории для кейсов");
            throw e;
        }
    }

    @Transactional
    public void setTitleToCategory(Long id, String title) {
        log.info("Попытка установки заголовка для категории кейсов");
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFound404("Категория не найдена"));

            category.setTitle(title);

            categoryRepository.save(category);

            log.info("Заголовок для категории кейсов успешно установлен");
        } catch (Exception e) {
            log.error("Ошибка при установке заголовка для категории кейсов");
            throw e;
        }
    }

    @Transactional
    public void setTypeToCategory(Long id, CategoryType categoryType) {
        log.info("Попытка установки Типа для категории кейсов");
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFound404("Категория не найдена"));

            category.setCategoryType(categoryType);

            categoryRepository.save(category);

            log.info("Тип для категории кейсов успешно установлен");
        } catch (Exception e) {
            log.error("Ошибка при установке Типа для категории кейсов");
            throw e;
        }
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
