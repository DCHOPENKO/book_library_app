package com.book.library.service.repositoryService;

import com.book.library.dto.DtoClass;
import com.book.library.model.EntityClass;
import java.util.List;

public interface CRUDService<T extends EntityClass, DTO extends DtoClass> {
        DTO save(T t);
        DTO update(T t);
        boolean deleteById(long id);
        DTO getDtoById(long id);
        T getEntityById(long id);
        void deleteAll();
        List<T> findAllEntities();
        List<DTO> findAllDtos();
}

