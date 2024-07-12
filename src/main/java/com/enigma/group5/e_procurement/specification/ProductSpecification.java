package com.enigma.group5.e_procurement.specification;

import com.enigma.group5.e_procurement.dto.request.SearchProductRequest;
import com.enigma.group5.e_procurement.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(SearchProductRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + request.getName().toLowerCase() + "%"
                        )
                );
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
