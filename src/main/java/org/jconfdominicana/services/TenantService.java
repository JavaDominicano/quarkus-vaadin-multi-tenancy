package org.jconfdominicana.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.repositories.common.TenantRepository;

import java.util.Optional;

/**
 * @author me@fredpena.dev
 * @created 08/07/2024  - 10:24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class TenantService {

//    private final EntityManager entityManager;

    private final TenantRepository repository;


    @Transactional
    public void insert(Tenant element) {
        repository.insert(element);
//        entityManager.persist(element);
    }

    public Tenant findByTenantName(String tenantId) {
        Tenant tenant = repository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException());

        return tenant;
    }
}
