package org.jconfdominicana.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.model.common.Tenant;

/**
 * @author me@fredpena.dev
 * @created 08/07/2024  - 10:24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class TenantService {

    private final EntityManager entityManager;

//    private final TenantRepository repository;


    @Transactional
    public void insert(Tenant element) {
        entityManager.persist(element);
    }
}
