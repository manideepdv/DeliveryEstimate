package com.example.ecom.services;

import com.example.ecom.exceptions.AddressNotFoundException;
import com.example.ecom.exceptions.ProductNotFoundException;
import com.example.ecom.libraries.GoogleMapAPIAdapter;

import com.example.ecom.libraries.MapApiAdapter;
import com.example.ecom.libraries.models.GLocation;
import com.example.ecom.models.*;
import com.example.ecom.repositories.AddressRepository;
import com.example.ecom.repositories.DeliveryHubRepository;
import com.example.ecom.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private AddressRepository addressRepository;
    private MapApiAdapter mapApiAdapter;
    private DeliveryHubRepository deliveryHubRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              AddressRepository addressRepository,
                              DeliveryHubRepository deliveryHubRepository) {
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.mapApiAdapter = new GoogleMapAPIAdapter();
        this.deliveryHubRepository = deliveryHubRepository;
    }

    public Date estimateDeliveryDate(int productId, int addressId) throws ProductNotFoundException, AddressNotFoundException {

        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product not found!!");
        }

        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if(addressOptional.isEmpty()){
            throw new AddressNotFoundException("Address not found!!");
        }

        //Gathering lattitude & longitude from address of destination and
        // creating a Glocation object for destion with that
        Address dest = addressOptional.get();
        GLocation destination = new GLocation();
        destination.setLatitude(dest.getLatitude());
        destination.setLongitude(dest.getLongitude());

        //As one product belongs to only one seller, we can get seller from that product
        // And from that seller, we can know about its address and lat&long of address
        //After getting lat&long of seller/ source address, we can create object of Glocation and create object
        Seller seller = productOptional.get().getSeller();
        Address src = seller.getAddress();
        GLocation source = new GLocation();
        source.setLatitude(src.getLatitude());
        source.setLongitude(src.getLongitude());


        //Finding address of Delivery hub location

        Optional<DeliveryHub> hubAddressOptional = deliveryHubRepository.findByAddress_ZipCode(src.getZipCode());
        if(hubAddressOptional.isEmpty()){
            throw new AddressNotFoundException("Delivery hub not found!");
        }

        DeliveryHub deliveryHub = hubAddressOptional.get();
        Address hubAddress = deliveryHub.getAddress();
        GLocation hub = new GLocation();
        hub.setLatitude(hubAddress.getLatitude());
        hub.setLongitude(hubAddress.getLongitude());


        //With help of Glocation source & Glocation destination, we can get time of estimate with GoogleMapApi

        //First time from Source to Delivery hub
        int srcToHubTime = mapApiAdapter.estimateTravelTime(source,hub);
        //Second time from  Delivery hub to destination
        int hubToDestTime = mapApiAdapter.estimateTravelTime(hub,destination);

        long sorceToDeliveryHubTime = (long)srcToHubTime*1000;
        long deliveryHubToDestinationTime = (long)hubToDestTime*1000;

        long totalRequiredTime = sorceToDeliveryHubTime+deliveryHubToDestinationTime+System.currentTimeMillis();

        //Parsing the given String to Date object
        Date date = new Date(totalRequiredTime);


        return date;

    }
}