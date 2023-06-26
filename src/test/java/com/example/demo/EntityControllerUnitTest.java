package com.example.demo;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    private final static List<Doctor> doctors = new ArrayList<>();
    @MockBean
    private DoctorRepository doctorRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        final Doctor d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        d1.setId(1);
        final Doctor d2 = new Doctor("Macarena", "García", 24, "m.garcia@hospital.accwe");
        d2.setId(2);
        doctors.add(d1);
        doctors.add(d2);
    }

    @Test
    void should_get_all_doctors() throws Exception {
        when(doctorRepository.findAll()).thenReturn(doctors);
        mockMvc.perform(get("/api/doctors")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_get_no_content_when_get_all_doctors_size_is_0() throws Exception {
        doctors.clear();
        when(doctorRepository.findAll()).thenReturn(doctors);
        mockMvc.perform(get("/api/doctors")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNoContent());
    }

    @Test
    void should_not_found_a_doctor_by_id() throws Exception {
        final long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(String.format("/api/doctors/%s", id)))
               .andExpect(status().isNotFound());
    }

    @Test
    void should_get_a_doctor_by_id() throws Exception {
        final long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctors.get(1)));
        mockMvc.perform(get(String.format("/api/doctors/%s", id)))
               .andExpect(status().isOk());
    }

    @Test
    void should_create_a_doctor() throws Exception {
        final Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        mockMvc.perform(post("/api/doctor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(doctor))
               )
               .andExpect(status().isCreated());
    }

    @Test
    void should_delete_a_doctor_by_id() throws Exception {
        final long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctors.get(1)));
        doNothing().when(doctorRepository)
                   .deleteById(id);
        mockMvc.perform(delete(String.format("/api/doctors/%s", id)))
               .andExpect(status().isOk());
    }

    @Test
    void should_return_not_found_when_delete_a_doctor_by_id() throws Exception {
        final long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(delete(String.format("/api/doctors/%s", id)))
               .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_all_doctors() throws Exception {
        doNothing().when(doctorRepository)
                   .deleteAll();
        mockMvc.perform(delete("/api/doctors"))
               .andExpect(status().isOk());
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    private final static List<Patient> patients = new ArrayList<>();

    @MockBean
    private PatientRepository patientRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        final Patient p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        p1.setId(1);
        final Patient p2 = new Patient("Cornelio", "Andrea", 59, "c.andrea@hospital.accwe");
        p2.setId(2);
        patients.add(p1);
        patients.add(p2);
    }

    @Test
    void should_return_all_patients() throws Exception {
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_return_no_content_when_get_all_patients() throws Exception {
        patients.clear();
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNoContent());
    }

    @Test
    void should_return_not_found_when_get_patient_by_id() throws Exception {
        final long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(String.format("/api/patients/%s", id))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNotFound());
    }

    @Test
    void should_return_patient_when_get_patient_by_id() throws Exception {
        final long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.of(patients.get(0)));
        mockMvc.perform(get(String.format("/api/patients/%s", id))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_create_patient_when_get_patient_by_id() throws Exception {
        when(patientRepository.save(patients.get(0))).thenReturn(patients.get(0));
        mockMvc.perform(post("/api/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patients.get(0)))
               )
               .andExpect(status().isCreated());
    }

    @Test
    void should_delete_patient_when_get_patient_by_id_is_ok() throws Exception {
        final long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.ofNullable(patients.get(0)));
        doNothing().when(patientRepository)
                   .deleteById(id);
        mockMvc.perform(delete(String.format("/api/patients/%s", id))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_not_delete_patient_when_get_patient_by_id_is_ko() throws Exception {
        final long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(delete(String.format("/api/patients/%s", id))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_all_patients() throws Exception {
        doNothing().when(patientRepository)
                   .deleteAll();
        mockMvc.perform(delete("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    private final static List<Room> rooms = new ArrayList<>();

    @MockBean
    private RoomRepository roomRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        final Room r1 = new Room("Dermatology");
        final Room r2 = new Room("Nursing");
        rooms.add(r1);
        rooms.add(r2);
    }

    @Test
    void should_return_all_rooms_when_get_all_rooms() throws Exception {
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_return_not_found_when_get_all_rooms() throws Exception {
        rooms.clear();
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNoContent());
    }

    @Test
    void should_return_room_by_name_when_get_room_by_name() throws Exception {
        when(roomRepository.findByRoomName(rooms.get(0)
                                                .getRoomName())).thenReturn(Optional.of(rooms.get(0)));
        mockMvc.perform(get(String.format(
                       "/api/rooms/%s", rooms.get(0)
                                             .getRoomName()))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_return_not_found_when_get_room_by_name() throws Exception {
        when(roomRepository.findByRoomName(rooms.get(0)
                                                .getRoomName())).thenReturn(Optional.empty());
        mockMvc.perform(get(String.format(
                       "/api/rooms/%s", rooms.get(0)
                                             .getRoomName()))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNotFound());
    }

    @Test
    void should_create_room_when_create_room() throws Exception {
        when(roomRepository.save(rooms.get(0))).thenReturn(rooms.get(0));
        mockMvc.perform(post("/api/room")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rooms.get(0)))
               )
               .andExpect(status().isCreated());
    }

    @Test
    void should_delete_room_when_exists() throws Exception {
        when(roomRepository.findByRoomName(rooms.get(0)
                                                .getRoomName())).thenReturn(Optional.of(rooms.get(0)));
        doNothing().when(roomRepository)
                   .deleteByRoomName(rooms.get(0)
                                          .getRoomName());
        mockMvc.perform(delete(String.format(
                       "/api/rooms/%s", rooms.get(0)
                                             .getRoomName()))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    @Test
    void should_return_not_found_when_room_not_exists() throws Exception {
        when(roomRepository.findByRoomName(rooms.get(0)
                                                .getRoomName())).thenReturn(Optional.empty());
        doNothing().when(roomRepository)
                   .deleteByRoomName(rooms.get(0)
                                          .getRoomName());
        mockMvc.perform(delete(String.format(
                       "/api/rooms/%s", rooms.get(0)
                                             .getRoomName()))
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_all_rooms() throws Exception {
        doNothing().when(roomRepository)
                   .deleteAll();
        mockMvc.perform(delete("/api/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

}
