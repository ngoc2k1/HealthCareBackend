package vn.healthcare;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.healthcare.constant.BloodGroup;
import vn.healthcare.constant.Gender;
import vn.healthcare.entity.*;
import vn.healthcare.repository.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class HealthcareApplication implements CommandLineRunner {
    private final BookScheduleRepository bookScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final PatientMedicalHistoryRepository patientMedicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public static void main(String[] args) {
        SpringApplication.run(HealthcareApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initSpecialty();
        initPatient();
        initDoctor();
        initDoctorWorkSchedule();
        initAdmin();
    }

    private void initAdmin() {
        if(adminRepository.count() == 0) {
            Admin admin = Admin.builder()
                    .email("admin@gmail.com")
                    .phone("0123456789")
                    .password(passwordEncoder.encode("12345"))
                    .build();
            adminRepository.save(admin);
        }
    }

    private void createSchedule(List<DoctorWorkSchedule> doctorWorkSchedules,
                                Integer doctorId, Integer startPrice, Integer priceDistance,
                                LocalDateTime startTime, Integer loop, Integer distanceMinute,
                                Integer freeTimeMinus) {

        LocalDateTime temp = startTime;
        for (int i = 0; i < loop; i++) {
            DoctorWorkSchedule doctorWorkSchedule = new DoctorWorkSchedule();

            doctorWorkSchedule.setPrice(startPrice + i * priceDistance);
            doctorWorkSchedule.setDoctor(Doctor.builder().id(doctorId).build());

            doctorWorkSchedule.setWorkStartAt(temp);

            doctorWorkSchedule.setWorkEndAt(temp.plusMinutes(distanceMinute));

            temp = temp.plusMinutes(distanceMinute + freeTimeMinus);

            doctorWorkSchedules.add(doctorWorkSchedule);
        }
    }

    private void initDoctorWorkSchedule() {
        if (doctorWorkScheduleRepository.count() == 0) {
            List<DoctorWorkSchedule> doctorWorkSchedules = new ArrayList<>();


            LocalDateTime start_1 = LocalDateTime.now()
                    .withMinute(0).plusHours(6);
            createSchedule(doctorWorkSchedules, 1, 100000, 10000,
                    start_1, 6, 50, 30);


            LocalDateTime start_2 = LocalDateTime.now().withHour(9)
                    .withMinute(0).plusMonths(1).plusDays(1);
            createSchedule(doctorWorkSchedules, 1, 150000, 10000,
                    start_2, 10, 80, 20);

            LocalDateTime start_3 = LocalDateTime.now().withHour(10)
                    .withMinute(0).plusMonths(1).plusDays(2);
            createSchedule(doctorWorkSchedules, 1, 120000, 11000,
                    start_3, 8, 60, 30);

            LocalDateTime start_4 = LocalDateTime.now().withHour(8)
                    .withMinute(0).plusMonths(1).plusDays(3);
            createSchedule(doctorWorkSchedules, 1, 160000, 15000,
                    start_4, 5, 80, 30);


            LocalDateTime start_5 = LocalDateTime.now().withHour(9)
                    .withMinute(0).plusMonths(1).plusDays(4);
            createSchedule(doctorWorkSchedules, 1, 200000, 20000,
                    start_5, 9, 50, 20);

            LocalDateTime start_6 = LocalDateTime.now().withHour(8)
                    .withMinute(0).plusMonths(1);
            createSchedule(doctorWorkSchedules, 2, 180000, 15000,
                    start_6, 6, 60, 30);

            LocalDateTime start_7 = LocalDateTime.now().withHour(9)
                    .withMinute(0).plusMonths(1).plusDays(1);
            createSchedule(doctorWorkSchedules, 2, 100000, 10000,
                    start_7, 8, 100, 40);

            doctorWorkScheduleRepository.saveAll(doctorWorkSchedules);
        }
    }


    private void initDoctor() {
        if (doctorRepository.count() == 0) {
            Doctor doctor_1 = Doctor.builder()
                    .id(1)
                    .addressTest("Address Test 1")
                    .avatar("1.png")
                    .birthday("01/01/2001")
                    .gender(Gender.MALE)
                    .name("Doctor 1")
                    .email("email_10@gmail.com")
                    .specialty(Specialty.builder().id(1).build())
                    .password(passwordEncoder.encode("12345"))
                    .phone("0123456789")
                    .build();

            Doctor doctor_2 = Doctor.builder()
                    .id(2)
                    .addressTest("Address Test 2")
                    .avatar("2.png")
                    .birthday("02/02/2002")
                    .gender(Gender.MALE)
                    .name("Doctor 2")
                    .email("email_2@gmail.com")
                    .specialty(Specialty.builder().id(2).build())
                    .password(passwordEncoder.encode("12345"))
                    .phone("0222222222")
                    .build();

            Doctor doctor_3 = Doctor.builder()
                    .id(3)
                    .addressTest("Address Test 3")
                    .avatar("3.png")
                    .birthday("01/01/2001")
                    .gender(Gender.MALE)
                    .name("Doctor 3")
                    .email("email_3@gmail.com")
                    .specialty(Specialty.builder().id(3).build())
                    .password(passwordEncoder.encode("12345"))
                    .phone("0333333333")
                    .build();

            Doctor doctor_4 = Doctor.builder()
                    .id(4)
                    .addressTest("Address Test 4")
                    .avatar("4.png")
                    .birthday("04/04/2004")
                    .gender(Gender.MALE)
                    .name("Doctor 4")
                    .email("email_4@gmail.com")
                    .specialty(Specialty.builder().id(4).build())
                    .password(passwordEncoder.encode("12345"))
                    .phone("0444444444")
                    .build();

            Doctor doctor_5 = Doctor.builder()
                    .id(5)
                    .addressTest("Address Test 5")
                    .avatar("5.png")
                    .birthday("05/05/2005")
                    .gender(Gender.MALE)
                    .name("Doctor 5")
                    .email("email_5@gmail.com")
                    .specialty(Specialty.builder().id(5).build())
                    .password(passwordEncoder.encode("12345"))
                    .phone("0987654321")
                    .build();
            doctorRepository.saveAll(List.of(doctor_1, doctor_2, doctor_3,
                    doctor_4, doctor_5));
        }
    }

    private void initPatient() {
        if (patientRepository.count() == 0) {
            Patient patient_1 = Patient.builder()
                    .id(1)
                    .address("Address 1")
                    .avatar("1.png")
                    .birthday("01/01/2001")
                    .bloodGroup(BloodGroup.A)
                    .gender(Gender.MALE)
                    .name("Patient 1")
                    .email("email_100@gmail.com")
                    .password(passwordEncoder.encode("12345"))
                    .phone("0123456789")
                    .build();

            Patient patient_2 = Patient.builder()
                    .id(2)
                    .address("Address 2")
                    .avatar("2.png")
                    .birthday("02/02/2002")
                    .bloodGroup(BloodGroup.A)
                    .gender(Gender.MALE)
                    .name("Patient 2")
                    .email("email_2@gmail.com")
                    .password(passwordEncoder.encode("12345"))
                    .phone("0222222222")
                    .build();

            Patient patient_3 = Patient.builder()
                    .id(3)
                    .address("Address 3")
                    .avatar("3.png")
                    .birthday("01/01/2001")
                    .bloodGroup(BloodGroup.A)
                    .gender(Gender.MALE)
                    .name("Patient 3")
                    .email("email_3@gmail.com")
                    .password(passwordEncoder.encode("12345"))
                    .phone("0333333333")
                    .build();

            Patient patient_4 = Patient.builder()
                    .id(4)
                    .address("Address 4")
                    .avatar("4.png")
                    .birthday("04/04/2004")
                    .bloodGroup(BloodGroup.A)
                    .gender(Gender.MALE)
                    .name("Patient 4")
                    .email("email_4@gmail.com")
                    .password(passwordEncoder.encode("12345"))
                    .phone("0444444444")
                    .build();

            Patient patient_5 = Patient.builder()
                    .id(5)
                    .address("Address 5")
                    .avatar("5.png")
                    .birthday("05/05/2005")
                    .bloodGroup(BloodGroup.A)
                    .gender(Gender.MALE)
                    .name("Patient 5")
                    .email("email_5@gmail.com")
                    .password(passwordEncoder.encode("12345"))
                    .phone("0987654321")
                    .build();
            patientRepository.saveAll(List.of(patient_1, patient_2, patient_3,
                    patient_4, patient_5));
        }
    }

    private void initSpecialty() {
        if (specialtyRepository.count() == 0) {
            Specialty specialty_1 = Specialty.builder()
                    .id(1)
                    .name("111111")
                    .image("1.png")
                    .build();

            Specialty specialty_2 = Specialty.builder()
                    .id(2)
                    .name("222222")
                    .image("2.png")
                    .build();

            Specialty specialty_3 = Specialty.builder()
                    .id(3)
                    .name("333333")
                    .image("3.png")
                    .build();

            Specialty specialty_4 = Specialty.builder()
                    .id(4)
                    .name("444444")
                    .image("4.png")
                    .build();

            Specialty specialty_5 = Specialty.builder()
                    .id(5)
                    .name("555555")
                    .image("5.png")
                    .build();
            specialtyRepository.saveAll(List.of(specialty_1, specialty_2,
                    specialty_3, specialty_4, specialty_5));
        }
    }
}
