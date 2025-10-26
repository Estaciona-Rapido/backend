package org.estaciona.rapido.services;

import java.util.ArrayList;
import java.util.List;

import org.estaciona.rapido.dto.BusinessHour;
import org.estaciona.rapido.persistence.BusinessHourEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BusinessHourService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public List<BusinessHour> getBusinessHours(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Id must be a positive integer.");
        }
        return entityManager.createNamedQuery("BusinessHours.getById", 
        BusinessHour.class)
        .setParameter("id", id)
        .getResultList();
    }

    @Transactional
    public List<BusinessHour> getBusinessHoursAtDefaultScenario()
    {
        return getBusinessHours(1);
    }
}
