package vn.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.healthcare.entity.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
//jpql theo chuẩn của thư viện dựa theo Object Entity trong code

    @Query("select a from Admin a where a.phone = :phoneOrEmail or a.email = :phoneOrEmail")
    Optional<Admin> findByPhoneOrEmail(String phoneOrEmail);
}
