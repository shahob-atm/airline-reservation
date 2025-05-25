package com.sh32bit.airline.service.impl;

import com.sh32bit.airline.entity.Flight;
import com.sh32bit.airline.entity.Ticket;
import com.sh32bit.airline.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void sendFlightUpdateNotification(Flight flight, String changeInfo) {
        for (Ticket ticket : flight.getTickets()) {
            String customerEmail = ticket.getCustomer().getEmail();
            String subject = "Your Flight Has Been Updated";
            String text = String.format(
                    "Dear %s,\n\nYour flight %s → %s (%s) has been updated.\n\n%s\n\nNew Details:\nDeparture: %s\nArrival: %s\n\nThank you for choosing us.",
                    ticket.getCustomer().getFirstName(),
                    flight.getFrom().getName(),
                    flight.getTo().getName(),
                    flight.getCompany().getName(),
                    changeInfo,
                    flight.getDepartureTime(),
                    flight.getArrivalTime()
            );
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(customerEmail);
                message.setSubject(subject);
                message.setText(text);
                mailSender.send(message);
                log.info("Notification email sent to {}", customerEmail);
            } catch (MailException e) {
                log.error("Failed to send email to {}: {}", customerEmail, e.getMessage());
            }
        }
    }
}
