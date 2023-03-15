package com.nagarro.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.nagarro.model.Product;

public interface ProductService {
	Product saveProduct(Product product);
	Boolean existsByProductCode(String productCode);
	Set<String> findBrands();
	Set<String> findCategories();
	List<Product> findAllProducts();
	List <Product> findAllByAddByUserId(Long addByUserId);
	Optional <Product> findByProductCode(String productCode);
	Set<Object> generateAutoCompleteData ();

}
