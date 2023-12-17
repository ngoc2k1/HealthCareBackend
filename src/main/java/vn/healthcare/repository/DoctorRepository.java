package vn.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.entity.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Page<Doctor> findAllBySpecialtyId(Integer specialtyId, Pageable pageable);

    Optional<Doctor> findByPhone(String phone);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByResetPasswordCode(String resetPasswordCode);

    @Query("select d from Doctor d where d.phone = :phoneOrEmail or d.email = :phoneOrEmail")
    Optional<Doctor> findByPhoneOrEmail(String phoneOrEmail);
}
