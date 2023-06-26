package com.example.demo.controllers;

import com.example.demo.entities.Appointment;
import com.example.demo.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        final List<Appointment> appointments = new ArrayList<>(appointmentRepository.findAll());

        if (appointments.isEmpty()) {
            return ResponseEntity.noContent()
                                 .build();
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable("id") final long id
    ) {
        final Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound()
                                                         .build());
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(
            @RequestBody final Appointment appointment
    ) {
        final boolean hasInvalidDates = hasInvalidDates(appointment);

        if (hasInvalidDates) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final boolean existsOverlap = appointmentRepository.findAll()
                                                           .stream()
                                                           .anyMatch(appointment::overlaps);
        if (existsOverlap) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        appointmentRepository.save(appointment);

        final List<Appointment> appointmentList = appointmentRepository.findAll();

        return ResponseEntity.ok(appointmentList);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(
            @PathVariable("id") final long id
    ) {
        final Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return ResponseEntity.notFound()
                                 .build();
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean hasInvalidDates(final Appointment appointment) {
        /// True when:
        // Case 1: A.starts > A.finishes
        // Case 2: A.finishes < A.starts
        // Case 3: A.starts == A.finishes
        return appointment.getStartsAt()
                          .isAfter(appointment.getFinishesAt()) ||
                appointment.getFinishesAt()
                           .isBefore(
                                   appointment.getStartsAt()) ||
                appointment.getStartsAt()
                           .equals(appointment.getFinishesAt());
    }
}
