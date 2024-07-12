package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.constant.ResponseMessage;
import com.enigma.group5.e_procurement.dto.request.SearchWarehouseRequest;
import com.enigma.group5.e_procurement.dto.response.CommonResponse;
import com.enigma.group5.e_procurement.dto.response.PagingResponse;
import com.enigma.group5.e_procurement.dto.response.WarehouseResponse;
import com.enigma.group5.e_procurement.entity.Warehouse;
import com.enigma.group5.e_procurement.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.WAREHOUSE_API)
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<WarehouseResponse>>> getAllWarehouse(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "vpId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "vpId", required = false) String vendorId,
            @RequestParam(name = "transDetail", required = false) String transDetail,
            @RequestParam(name = "stock", required = false) Integer stock
    ){
        SearchWarehouseRequest request = SearchWarehouseRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .vpId(vendorId)
                .transDetail(transDetail)
                .stock(stock)
                .build();

        Page<WarehouseResponse> warehouseList = warehouseService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(warehouseList.getTotalPages())
                .totalElements(warehouseList.getTotalElements())
                .page(warehouseList.getPageable().getPageNumber())
                .size(warehouseList.getPageable().getPageSize())
                .hasNext(warehouseList.hasNext())
                .hasPrevious(warehouseList.hasPrevious())
                .build();

        CommonResponse<List<WarehouseResponse>> response = CommonResponse.<List<WarehouseResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(warehouseList.getContent())
                .paging(pagingResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<Warehouse>> getWarehouseById(@PathVariable String id) {
        Warehouse byId = warehouseService.getById(id);

        CommonResponse<Warehouse> response = CommonResponse.<Warehouse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Warehouse>> updatePrice(@RequestBody Warehouse warehouse) {
        Warehouse update = warehouseService.update(warehouse);

        CommonResponse<Warehouse> response = CommonResponse.<Warehouse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(update)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id) {
        warehouseService.deleteById(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response) ;
    }

}
