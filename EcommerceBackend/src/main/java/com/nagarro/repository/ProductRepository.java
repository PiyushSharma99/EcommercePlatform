package com.nagarro.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nagarro.constants.SqlQueryConstants;
import com.nagarro.model.Order;
import com.nagarro.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	@Query(value = SqlQueryConstants.FIND_BRANDS, nativeQuery = true)
	Set<String> findBrands();
	
	@Query(value = SqlQueryConstants.FIND_CATEGORIES, nativeQuery = true)
	Set<String> findCategory();
	
	List <Product> findAll();

	List <Product> findAllByAddByUserId(Long addByUserId);
	
	Boolean existsByProductCode(String productCode);
	
	Optional <Product> findByProductCode(String productCode);
	
	@Query(value = SqlQueryConstants.GENERATE_AUTOCOMPLETE_LIST, nativeQuery = true)
	Set<Object> generateAutoCompleteData ();
}
