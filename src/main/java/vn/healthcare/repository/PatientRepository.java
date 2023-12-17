package vn.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("select distinct p from Patient as p inner join p.bookSchedules as bs where " +
            "bs.doctor.id = :doctorId and p.name like %:name%")
    List<Patient> findAllPatientBookedByDoctorAndByName(Integer doctorId, String name);
    
    Optional<Patient> findByPhone(String phone);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByResetPasswordCode(String resetPasswordCode);

    @Query("select p from Patient p where p.phone = :phoneOrEmail or p.email = :phoneOrEmail")
    Optional<Patient> findByPhoneOrEmail(String phoneOrEmail);

    @Query("select distinct p from Patient as p inner join p.bookSchedules as bs where " +
            "bs.doctor.id = :doctorId")
    List<Patient> findAllPatientBookedByDoctor(Integer doctorId);
}
