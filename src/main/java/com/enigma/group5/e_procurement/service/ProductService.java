package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.request.NewProductRequest;
import com.enigma.group5.e_procurement.dto.request.SearchProductRequest;
import com.enigma.group5.e_procurement.dto.response.ProductResponse;
import com.enigma.group5.e_procurement.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(NewProductRequest productRequest);
    Product getById(String id);
    Page<Product> getAll(SearchProductRequest productRequest);
    Product update(Product product);
    void deleteById(String id);
    Product findByName(String name);
}
