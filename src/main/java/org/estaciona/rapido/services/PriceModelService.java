package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.estaciona.rapido.dto.ParkingRecord;
import org.estaciona.rapido.dto.ParkingRegisterProposal;
import org.estaciona.rapido.dto.PriceModel;
import org.estaciona.rapido.dto.PriceOption;
import org.estaciona.rapido.dto.Scenario;
import org.estaciona.rapido.dto.ScenarioBrief;
import org.estaciona.rapido.exceptions.ClosedException;
import org.estaciona.rapido.exceptions.HasAlreadyPaid;
import org.estaciona.rapido.exceptions.NoCheckoutException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.exceptions.TooOldCheckout;
import org.estaciona.rapido.persistence.BusinessHourEntity;
import org.estaciona.rapido.persistence.FrequencyEnum;
import org.estaciona.rapido.persistence.OperationEntity;
import org.estaciona.rapido.persistence.PriceModelEntity;
import org.estaciona.rapido.persistence.ScenarioEntity;

@ApplicationScoped
public class PriceModelService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public List<PriceModel> getPriceModels(long scenarioId)
    {
        List<PriceModelEntity> priceModelEntities = entityManager.find(ScenarioEntity.class, scenarioId).prices;
        List<PriceModel> result = new ArrayList<PriceModel>(priceModelEntities.size());
        for (PriceModelEntity priceModelEntity : priceModelEntities) {
            result.add(new PriceModel(priceModelEntity.id, priceModelEntity.name, priceModelEntity.isActivated, priceModelEntity.value, priceModelEntity.frequencyValue, priceModelEntity.frequencyType.toString()));
        }
        return result;
    }

    @Transactional
    public List<PriceModel> getDefaultScenarioPriceModels()
    {
        return getPriceModels(1);
    }

}
