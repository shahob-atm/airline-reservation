package com.sh32bit.airline.service;

import com.sh32bit.airline.entity.Flight;

public interface EmailNotificationService {
    void sendFlightUpdateNotification(Flight flight, String s);
}
