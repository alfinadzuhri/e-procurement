package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.constant.UserRole;
import com.enigma.group5.e_procurement.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
