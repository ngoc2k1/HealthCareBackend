package vn.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.constant.StatusBook;
import vn.healthcare.entity.BookSchedule;

import java.util.List;
import java.util.Optional;

public interface BookScheduleRepository extends JpaRepository<BookSchedule, Integer> {
        // tự generate dựa trên tên method
    Page<BookSchedule> findAllByDoctorId(Integer doctorId, Pageable pageable);

    Optional<BookSchedule> findByIdAndDoctorId(Integer id, Integer doctorId);

    Optional<BookSchedule> findByDoctorWorkScheduleId(Integer doctorWorkScheduleId);

    Optional<BookSchedule> findByIdAndPatientId(Integer id, Integer patientId);

    Page<BookSchedule> findAllByPatientId(Integer patientId, Pageable pageable);

    @Query("select b from BookSchedule b where b.doctor.id = :doctorId and " +
            "FUNCTION('TIMESTAMPDIFF', HOUR, FUNCTION('CURRENT_TIMESTAMP'), " +
            "b.updatedAt) between 0 and 8" +
            "and b.statusBook in :status")
    Page<BookSchedule> findAllByDoctorIdAndStatusAndBefore8Hour(Integer doctorId,
                                                                List<StatusBook> status,
                                                                Pageable pageable);


    @Query("select b from BookSchedule b where b.patient.id = :patientId and " +
            "FUNCTION('TIMESTAMPDIFF', HOUR, FUNCTION('CURRENT_TIMESTAMP'), " +
            "b.doctorWorkSchedule.workStartAt) between 0 and 8 and b.statusBook = :status")
    List<BookSchedule> findAllByPatientIdAndStatusAndBefore8Hour(Integer patientId, StatusBook status);
}