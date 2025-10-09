package com.busbooking.model;

import javax.persistence.Transient;

public class TripDetail extends TripTransport {

    @Transient
    public String getCompanyName() {
        return getTransportCompany() != null ? getTransportCompany().getName() : null;
    }

    @Transient
    public String getVehicleLicensePlate() {
        return getVehicleTransport() != null ? getVehicleTransport().getLicensePlate() : null;
    }

    @Transient
    public String getDriverName() {
        return getDriverTransport() != null ? getDriverTransport().getName() : null;
    }
}
