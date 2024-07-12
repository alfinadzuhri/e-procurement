package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String > {
    Optional<UserAccount> findByUsername(String username);
}
