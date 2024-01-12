package vn.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.entity.PatientMedicalHistory;

public interface PatientMedicalHistoryRepository extends JpaRepository<PatientMedicalHistory, Integer> {
        @Query("select p from PatientMedicalHistory  p where p.patient.id = :patientId " +
                        "and p.bookSchedule.statusBook = 'DA_KHAM'")
        Page<PatientMedicalHistory> findAllByPatientId(Integer patientId, Pageable pageable);

        @Query("select p from PatientMedicalHistory p where p.patient.id = :patientId " +
                        "and p.bookSchedule.doctor.id = :doctorId and p.bookSchedule.statusBook = 'DA_KHAM'")
        Page<PatientMedicalHistory> findAllByPatientIdAndDoctorId(Integer patientId, Integer doctorId,
                        Pageable pageable);

        @Modifying
        @Query(value = "delete from PatientMedicalHistory as p where p.bookSchedule.id = :bookScheduleId")
        void deleteByBookScheduleId(Integer bookScheduleId);
}
