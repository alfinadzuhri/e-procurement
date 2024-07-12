package com.enigma.group5.e_procurement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.enigma.group5.e_procurement.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = ConstantTable.TRANSACTION_DETAIL)
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonBackReference
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "vp_id", nullable = false)
    private VendorProduct vendorProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Long price;

//    @OneToMany(mappedBy = "transactionDetail")
//    @JsonManagedReference
//    private List<Report> reports;
}
