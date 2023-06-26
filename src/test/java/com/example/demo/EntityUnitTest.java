package com.example.demo;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private final static LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now()
                                                                                   .format(formatter), formatter);
    @Autowired
    private TestEntityManager entityManager;
    private Doctor d1;
    private Patient p1;
    private Room r1;
    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeEach
    void setUp() {
        d1 = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        p1 = new Patient("Marcos", "Gonzalez", 48, "m.gonzalez@hospital.accwe");
        r1 = new Room("Dermatology");
        a1 = new Appointment(p1, d1, r1, dateTime, dateTime.plusHours(1));
        a2 = new Appointment(p1, d1, r1, dateTime.plusHours(2), dateTime);
        a3 = new Appointment(p1, d1, r1, dateTime, dateTime);
    }

    // Creation, getters and setters

    @Test
    void should_create_a_valid_doctor() {
        d1.setId(1);
        assertThat(d1.getId()).isEqualTo(1);
        assertThat(d1.getFirstName()).isEqualTo("Juan");
        assertThat(d1.getLastName()).isEqualTo("Carlos");
        assertThat(d1.getAge()).isEqualTo(34);
        assertThat(d1.getEmail()).isEqualTo("j.carlos@hospital.accwe");
        assertThat(d1.getClass()).isEqualTo(Doctor.class);
    }

    @Test
    void should_Edit_person_details_of_doctor() {
        d1.setAge(54);
        d1.setFirstName("Manolo");
        d1.setLastName("Castro");
        d1.setEmail("m.castro@hospital.accwe");
        assertThat(d1).hasFieldOrPropertyWithValue("age", 54)
                      .hasFieldOrPropertyWithValue("firstName", "Manolo")
                      .hasFieldOrPropertyWithValue("lastName", "Castro")
                      .hasFieldOrPropertyWithValue("email", "m.castro@hospital.accwe");
        assertThat(d1.getClass()).isEqualTo(Doctor.class);
    }

    @Test
    void should_create_a_valid_patient() {
        p1.setId(1);
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getFirstName()).isEqualTo("Marcos");
        assertThat(p1.getLastName()).isEqualTo("Gonzalez");
        assertThat(p1.getAge()).isEqualTo(48);
        assertThat(p1.getEmail()).isEqualTo("m.gonzalez@hospital.accwe");
        assertThat(p1.getClass()).isEqualTo(Patient.class);
    }

    @Test
    void should_create_a_valid_room() {
        assertThat(r1.getRoomName()).isEqualTo("Dermatology");
        assertThat(r1.getClass()).isEqualTo(Room.class);
    }

    @Test
    void should_create_a_not_valid_room() {
        final Room room = new Room();
        assertThat(room).hasFieldOrPropertyWithValue("roomName", null);
        assertThat(room.getClass()).isEqualTo(Room.class);
    }

    @Test
    void should_create_a_valid_appointment() {
        final Appointment appointment = new Appointment(p1, d1, r1, dateTime, dateTime.plusHours(1));
        appointment.setId(4);
        appointment.setDoctor(d1);
        appointment.setPatient(p1);
        assertThat(appointment.getId()).isEqualTo(4);
        assertThat(appointment.getDoctor()).isEqualTo(d1);
        assertThat(appointment.getPatient()).isEqualTo(p1);
        assertThat(appointment.getClass()).isEqualTo(Appointment.class);
    }

    // Overlapping

    @Test
    void should_not_have_overlaps_between_a1_and_a2_given_same_room() {
        assertThat(a1.overlaps(a2)).isFalse();
    }

    @Test
    void should_not_have_overlaps_between_a1_and_a2_given_different_room() {
        a1.setRoom(new Room("Consulting"));
        assertThat(a1.overlaps(a2)).isFalse();
    }

    @Test
    void should_have_overlaps_between_a1_and_a2_cases_1_and_2() {
        assertThat(a1.overlaps(a3)).isTrue();
    }

    @Test
    void should_have_overlaps_between_a1_and_a2_case_3() {
        a1.setStartsAt(dateTime.minusHours(3));
        assertThat(a1.overlaps(a2)).isTrue();
    }

    @Test
    void should_have_overlaps_between_a1_and_a2_case_4() {
        a1.setStartsAt(dateTime.plusHours(1));
        a1.setFinishesAt(dateTime.plusHours(3));
        assertThat(a1.overlaps(a2)).isTrue();
    }
}