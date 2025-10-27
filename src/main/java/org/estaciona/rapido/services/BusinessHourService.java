package org.estaciona.rapido.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.estaciona.rapido.dto.BusinessHour;
import org.estaciona.rapido.dto.BusinessHourProposal;
import org.estaciona.rapido.exceptions.NoBusinessHoursException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.persistence.BusinessHourEntity;
import org.estaciona.rapido.persistence.ScenarioEntity;

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

    @Transactional
    public void createBusinessHour(BusinessHourProposal businessHourProposal, long relatedScenarioId) throws NoScenariosException
    {
        BusinessHourEntity entity = new BusinessHourEntity();
        entity.isActivated = businessHourProposal.isActivated();
        entity.startWeekDay = businessHourProposal.startWeekDay();
        entity.endWeekDay = businessHourProposal.endWeekDay();
        entity.startTime = businessHourProposal.startTime();
        entity.endTime = businessHourProposal.endTime();
        entity.scenario = entityManager.find(ScenarioEntity.class, relatedScenarioId);
        if (entity.scenario == null) {
            throw new NoScenariosException("There are no scenarios with this id.");
        } else {
            entityManager.persist(entity);
        }
    }

    @Transactional
    public void createBusinessHourAtDefaultScenario(BusinessHourProposal businessHourProposal) throws NoScenariosException
    {
        createBusinessHour(businessHourProposal, 1);
    }

    @Transactional
    public void setBusinessHour(long id, BusinessHourProposal businessHour, long relatedScenarioId) throws NoBusinessHoursException
    {
        ScenarioEntity scenarioEntity = entityManager.find(ScenarioEntity.class, relatedScenarioId);
        Optional<BusinessHourEntity> optionalBusinessHourEntity = scenarioEntity.businessHours.stream().filter(iteratorBusinessHourEntity -> iteratorBusinessHourEntity.id == id).findFirst();
        if (optionalBusinessHourEntity.isPresent() == false) {
            throw new NoBusinessHoursException(String.format("There are no business hours with id %d in the scenario with id %d.", id, relatedScenarioId));
        } else {
            optionalBusinessHourEntity.get().isActivated = businessHour.isActivated();
            optionalBusinessHourEntity.get().startWeekDay = businessHour.startWeekDay();
            optionalBusinessHourEntity.get().endWeekDay = businessHour.endWeekDay();
            optionalBusinessHourEntity.get().startTime = businessHour.startTime();
            optionalBusinessHourEntity.get().endTime = businessHour.endTime();
        }
    }

    @Transactional
    public void setBusinessHourAtDefaultScenario(long id, BusinessHourProposal businessHour) throws NoBusinessHoursException
    {
        setBusinessHour(id, businessHour, 1);
    }

    @Transactional
    public void deleteBusinessHour(long id, long relatedScenarioId) throws NoBusinessHoursException
    {
        ScenarioEntity scenarioEntity = entityManager.find(ScenarioEntity.class, relatedScenarioId);
        Optional<BusinessHourEntity> optionalBusinessHourEntity = scenarioEntity.businessHours.stream().filter(iteratorBusinessHourEntity -> iteratorBusinessHourEntity.id == id).findFirst();
        if (optionalBusinessHourEntity.isPresent() == false) {
            throw new NoBusinessHoursException(String.format("There are no business hours with id %d in the scenario with id %d.", id, relatedScenarioId));
        } else {
            entityManager.remove(optionalBusinessHourEntity.get());
        }
    }

    @Transactional
    public void deleteBusinessHourAtDefaultScenario(long id) throws NoBusinessHoursException
    {
        deleteBusinessHour(id, 1);
    }
    
}
