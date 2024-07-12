package com.enigma.group5.e_procurement.specification;

import com.enigma.group5.e_procurement.dto.request.SearchWarehouseRequest;
import com.enigma.group5.e_procurement.entity.Warehouse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WarehouseSpecification {
    public static Specification<Warehouse> getSpecification(SearchWarehouseRequest searchWarehouseRequest){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchWarehouseRequest.getVpId() != null) {
                Predicate warehousePredicate = criteriaBuilder.equal(root.get("vpId"),
                        searchWarehouseRequest.getVpId());
                predicates.add(warehousePredicate);
            }
            if (searchWarehouseRequest.getTransDetail() != null) {
                Predicate warehousePredicate = criteriaBuilder.equal(root.get("transDetail"),
                        searchWarehouseRequest.getTransDetail());
                predicates.add(warehousePredicate);
            }
            if (searchWarehouseRequest.getStock() != null) {
                Predicate warehousePredicate = criteriaBuilder.equal(root.get("stock"),
                        searchWarehouseRequest.getStock());
                predicates.add(warehousePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
