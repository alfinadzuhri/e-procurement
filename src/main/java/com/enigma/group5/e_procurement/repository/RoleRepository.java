package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.constant.UserRole;
import com.enigma.group5.e_procurement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
