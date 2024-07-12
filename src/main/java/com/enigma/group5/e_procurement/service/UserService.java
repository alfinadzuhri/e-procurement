package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);

    UserAccount getByContext();
}
