package com.example.ecom.controllers;

import com.example.ecom.dtos.DeliveryEstimateRequestDto;
import com.example.ecom.dtos.DeliveryEstimateResponseDto;
import com.example.ecom.dtos.ResponseStatus;
import com.example.ecom.exceptions.AddressNotFoundException;
import com.example.ecom.exceptions.ProductNotFoundException;
import com.example.ecom.services.ProductService;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public DeliveryEstimateResponseDto estimateDeliveryTime(DeliveryEstimateRequestDto requestDto){
        DeliveryEstimateResponseDto responseDto = new DeliveryEstimateResponseDto();
        Date expectedDeliveryDate = null;
        try {
            expectedDeliveryDate = productService.estimateDeliveryDate(requestDto.getProductId(), requestDto.getAddressId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }catch (AddressNotFoundException e) {
            System.out.println(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        responseDto.setExpectedDeliveryDate(expectedDeliveryDate);
        return responseDto;
    }
}
