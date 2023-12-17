package vn.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.healthcare.entity.Specialty;

import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    Optional<Specialty> findByName(String name);
}
