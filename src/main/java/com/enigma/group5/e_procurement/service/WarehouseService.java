package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.response.WarehouseResponse;
import com.enigma.group5.e_procurement.entity.Transaction;
import com.enigma.group5.e_procurement.dto.request.SearchWarehouseRequest;
import com.enigma.group5.e_procurement.entity.Warehouse;
import org.springframework.data.domain.Page;

public interface WarehouseService {

    Warehouse findByVendorProductId(String vpId);
    void create(Transaction transaction);
    Warehouse getById(String id);
    Page<WarehouseResponse> getAll(SearchWarehouseRequest WarehouseRequest);
    Warehouse update(Warehouse warehouse);
    void deleteById(String id);
}
