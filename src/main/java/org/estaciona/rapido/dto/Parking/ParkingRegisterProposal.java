package org.estaciona.rapido.dto.Parking;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkingRegisterProposal {
    private String plate;
    public long price_model_id;
    // TODO: create unique Exception class for plate errors.
    public ParkingRegisterProposal(String plate) throws IOException
    {
        this.setPlate(plate);
    }

    public String getPlate()
    {
        return this.plate;
    }
    public void setPlate(String plate) throws IOException
    {   
        if (Pattern.matches("[A-Z]{3}[0-9]{4}", plate) || Pattern.matches("[A-Z]{3}[0-9][A-Z][0-9]{2}", plate))
            this.plate = plate;
        else
            throw new IOException("Plate inserted is not from a supported pattern.");
    }
}
