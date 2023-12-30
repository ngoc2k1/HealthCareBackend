package vn.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.entity.DoctorWorkSchedule;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorWorkScheduleRepository extends JpaRepository<DoctorWorkSchedule, Integer> {
        //sql thuần(khi jpql không hỗ trợ) dựa theo bảng trong databse: nativeQuery=true
    @Query(value = "select distinct DATE(d.work_start_at) from doctor_work_schedule as d where d.doctor_id = :doctorId " +
            "and d.work_start_at > :current",
            nativeQuery = true)
    List<String> findAllDateByDoctor(Integer doctorId, LocalDateTime current);

    @Query("select d from DoctorWorkSchedule as d where d.doctor.id = :doctorId and " +
            "d.workStartAt between :start and :end")
    List<DoctorWorkSchedule> findAllByDoctor(Integer doctorId, LocalDateTime start, LocalDateTime end);
}
