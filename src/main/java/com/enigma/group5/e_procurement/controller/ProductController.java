package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.constant.ResponseMessage;
import com.enigma.group5.e_procurement.dto.request.NewProductRequest;
import com.enigma.group5.e_procurement.dto.request.SearchProductRequest;
import com.enigma.group5.e_procurement.dto.response.CommonResponse;
import com.enigma.group5.e_procurement.dto.response.PagingResponse;
import com.enigma.group5.e_procurement.dto.response.ProductResponse;
import com.enigma.group5.e_procurement.entity.Product;
import com.enigma.group5.e_procurement.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    private final ObjectMapper objectMapper;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> createNewProductWithImage(
            @RequestPart(name = "product") String jsonProductRequest,
            @RequestPart(name = "image") MultipartFile productImage
    ){

        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();

        try {
            NewProductRequest productRequest = objectMapper.readValue(jsonProductRequest, new TypeReference<>() {
            });
            productRequest.setImage(productImage);
            ProductResponse newProduct = productService.create(productRequest);

            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message("successfully save data");
            responseBuilder.data(newProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());

        }catch (Exception e){
            responseBuilder.message("internal server error");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }

    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<Product>> getById(@PathVariable String id) {
        Product product = productService.getById(id);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Product>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();

        Page<Product> productAll = productService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(productAll.getTotalPages())
                .totalElements(productAll.getTotalElements())
                .page(productAll.getPageable().getPageNumber())
                .size(productAll.getPageable().getPageSize())
                .hasNext(productAll.hasNext())
                .hasPrevious(productAll.hasPrevious())
                .build();

        CommonResponse<List<Product>> response = CommonResponse.<List<Product>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(productAll.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Product>> updateProduct(@RequestBody Product product) {
        Product update = productService.update(product);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(update)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<?>> deleteById(@PathVariable String id) {
        productService.deleteById(id);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK, Success Delete Product with id " + id)
                .build();
        return ResponseEntity.ok(response);
    }

}
