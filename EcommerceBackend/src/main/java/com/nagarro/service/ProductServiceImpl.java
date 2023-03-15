package com.nagarro.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.Product;
import com.nagarro.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public Boolean existsByProductCode(String productCode) {
		return productRepository.existsByProductCode(productCode);
	}

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Set<String> findBrands() {
		return productRepository.findBrands();
	}

	@Override
	public Set<String> findCategories() {
		return productRepository.findCategory();
	}

	@Override
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Optional<Product> findByProductCode(String productCode) {
		return productRepository.findByProductCode(productCode);
	}

	@Override
	public List<Product> findAllByAddByUserId(Long addByUserId) {
		return productRepository.findAllByAddByUserId(addByUserId);
	}

	@Override
	public Set<Object> generateAutoCompleteData() {
		return productRepository.generateAutoCompleteData();
	}

	

}
