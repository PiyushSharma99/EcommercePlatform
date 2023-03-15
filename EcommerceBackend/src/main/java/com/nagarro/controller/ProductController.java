package com.nagarro.controller;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.nagarro.constants.ResponseMessageConstants;
import com.nagarro.model.Product;
import com.nagarro.model.User;
import com.nagarro.request.ProductRequest;
import com.nagarro.response.ResponseHandler;
import com.nagarro.service.ProductService;
import com.nagarro.service.UserService;
import com.nagarro.utils.ImageUtil;

@CrossOrigin
@RestController
public class ProductController {

	@Autowired 
	ProductService productService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ImageUtil imageUtil;
	
	@PostMapping("/ecommerce/product/add/{username}")
	public ResponseEntity<Object> addProduct(@RequestPart("productData") ProductRequest productRequest, @RequestPart("imageFile") MultipartFile file,
			@PathVariable String username) {
		try {
			// finding userId for given username 
			Optional <User> user = userService.findByUsername(username);
			if(!user.isPresent()) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
			}
			
			// to avoid duplicate Product Code
			if (productService.existsByProductCode(productRequest.getProductCode())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_ALREADY_PRESENT, HttpStatus.OK, null);
			}
			
			//to avoid negative effective price
			if (Double.parseDouble(productRequest.getPrice()) <= Double.parseDouble(productRequest.getDiscount())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.DISCOUNT_LESS_THAN_PRICE, HttpStatus.OK, null);
			}
			
			
			// Create new product
		    Product product = new Product();
		    
		    product.setProductName(productRequest.getProductName());
		    product.setProductCode(productRequest.getProductCode());
		    product.setPrice(Double.parseDouble(productRequest.getPrice()));
		    product.setDiscount(Double.parseDouble(productRequest.getDiscount()));
		    product.setBrand(productRequest.getBrand());
		    product.setCategory(productRequest.getCategory());
		    product.setProductDescription(productRequest.getProductDescription());
		    product.setProductImage(imageUtil.compressBytes(file.getBytes()));
		    product.setAddByUserId(user.get().getUserId());
		    productService.saveProduct(product);
		    
			//System.out.println(product);
			return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_ADDED_SUCCESSFULLY, HttpStatus.CREATED, product);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(ResponseMessageConstants.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

	  }
	
	@GetMapping("/ecommerce/product/brands")
	public ResponseEntity<Object> getBrands() {
		try {
			if(productService.findBrands().size() != 0) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.BRANDS_SUCCESS, HttpStatus.OK, productService.findBrands());
			}
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
	}
	
	@GetMapping("/ecommerce/product/categories")
	public ResponseEntity<Object> getCategories() {
		try {
			if(productService.findCategories().size() != 0) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.CATEGORY_SUCCESS, HttpStatus.OK, productService.findCategories());
			}
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
	}
	
	@GetMapping("/ecommerce/products")
	public ResponseEntity<Object> getProducts() {
		try {
			List <Product> products = productService.findAllProducts();
			if(products.size() != 0) {
				List <Product> productsAfterDecompression = new ArrayList <>();
				for (Product product : products) {
					product.setProductImage(imageUtil.decompressBytes(product.getProductImage()));
					productsAfterDecompression.add(product);
				}
				return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_RETRIEVED, HttpStatus.OK, productsAfterDecompression);
			}
			return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			System.out.print(e);
			e.printStackTrace();
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/ecommerce/products/{username}")
	public ResponseEntity<Object> getProductsForVendor(@PathVariable String username) {
		try {
			Optional<User> user = userService.findByUsername(username);
			if(!user.isPresent()) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
			}
			List <Product> products = productService.findAllByAddByUserId(user.get().getUserId());
			
			if(products.size() != 0) {
				List <Product> productsAfterDecompression = new ArrayList <>();
				for (Product product : products) {
					product.setProductImage(imageUtil.decompressBytes(product.getProductImage()));
					productsAfterDecompression.add(product);
				}
				return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_RETRIEVED, HttpStatus.OK, productsAfterDecompression);
			}
			return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	
	@PostMapping("/ecommerce/product/edit")
	public ResponseEntity<Object> editProduct(@RequestPart("productData") ProductRequest productRequest, 
											  @RequestPart("imageFile") MultipartFile file) {
		try {
			
			if (Double.parseDouble(productRequest.getPrice()) <= Double.parseDouble(productRequest.getDiscount())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.DISCOUNT_LESS_THAN_PRICE, HttpStatus.OK, null);
				
			}
			
			// if product with code doesn't exist
			if( !productService.existsByProductCode(productRequest.getProductCode())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
			}
			
			Optional <Product> productInDb = productService.findByProductCode(productRequest.getProductCode());
			
			Product product = productInDb.get();
		    
		    product.setProductName(productRequest.getProductName());
		    product.setProductCode(productRequest.getProductCode());
		    product.setPrice(Double.parseDouble(productRequest.getPrice()));
		    product.setDiscount(Double.parseDouble(productRequest.getDiscount()));
		    product.setBrand(productRequest.getBrand());
		    product.setCategory(productRequest.getCategory());
		    product.setProductDescription(productRequest.getProductDescription());
		    product.setProductImage(imageUtil.compressBytes(file.getBytes()));
		    
		    productService.saveProduct(product);
		    
			return ResponseHandler.generateResponse(ResponseMessageConstants.PRODUCT_EDITED, HttpStatus.OK, product);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(ResponseMessageConstants.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

	  }
	
	@GetMapping(value = "ecommerce/autocompletelist")
	public ResponseEntity<Object> getAutoCompleteList() {
	try {
			Set<Object> autocompletelist = productService.generateAutoCompleteData();
			return ResponseHandler.generateResponse(null, HttpStatus.OK, autocompletelist);
	}
	catch(Exception e) {
		return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
	}
	}
}
