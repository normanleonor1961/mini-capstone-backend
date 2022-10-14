package norman.backendminicapstone.service;

import norman.backendminicapstone.dto.ProductDTO;
import norman.backendminicapstone.entity.ProductEntity;
import norman.backendminicapstone.exception.UserAlreadyExist;
import norman.backendminicapstone.model.ProductRequest;
import norman.backendminicapstone.repository.ProductRepository;
import norman.backendminicapstone.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;

    public List<ProductDTO> getAllProducts() {

        // Get all data from database
        List<ProductEntity> allProducts = productRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<ProductDTO> allProductsDTO = new ArrayList<>();

        allProducts.forEach(product -> {
            allProductsDTO.add(modelMapper.map(product, ProductDTO.class));
        });

        return allProductsDTO;
    }

    public ProductDTO getProduct(UUID productId) {

        // Get product from database
        ProductEntity product = productRepository.findByProductId(productId);

        if (product == null) throw new UserAlreadyExist("Product doesn't exist");

        return modelMapper.map(product, ProductDTO.class);
    }
    public List<ProductDTO> addProduct(ProductRequest newProduct) {

        // Save new product to database
        productRepository.save(ProductEntity
                .builder()
                .productId(UUID.randomUUID())
                .productName(newProduct.getProductName())
                .imageLink(null)
                .price(newProduct.getPrice())
                .ratings(newProduct.getRatings())
                .type(newProduct.getType())
                .filter(newProduct.getFilter())
                .description(newProduct.getDescription())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllProducts();
    }

    public List<ProductDTO> deleteProduct(UUID productId) {

        // Get product
        ProductEntity product = productRepository.findByProductId(productId);

        // Check if product exist
        if(product == null) throw new UserAlreadyExist("Product doesn't exist");

        // Delete product
        productRepository.deleteByProductId(productId);

        return getAllProducts();
    }
}
