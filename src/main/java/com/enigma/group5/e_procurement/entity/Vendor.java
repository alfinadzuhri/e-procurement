package com.enigma.group5.e_procurement.entity;

import com.enigma.group5.e_procurement.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = ConstantTable.VENDOR)
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
}
