package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Pattern;

import org.estaciona.rapido.dpo.Parking.ParkingRegisterProposal;
import org.estaciona.rapido.persistence.OperationEntity;
import org.estaciona.rapido.persistence.PriceModelEntity;

@ApplicationScoped
public class ParkingService {
    @Inject
    EntityManager em;

    @Transactional
    public void register(ParkingRegisterProposal proposal) throws IndexOutOfBoundsException
    {
        OperationEntity parking_operation = new OperationEntity();
        parking_operation.plate = proposal.getPlate();
        parking_operation.entry = OffsetDateTime.now();
        parking_operation.price_model = em.find(PriceModelEntity.class, proposal.price_model_id);
        if (parking_operation.price_model == null) {
            throw new IndexOutOfBoundsException(proposal.price_model_id);
        } else {
            em.persist(parking_operation);
        }
    }
}
