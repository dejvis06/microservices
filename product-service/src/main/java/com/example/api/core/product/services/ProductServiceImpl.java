package com.example.api.core.product.services;

import com.example.api.core.product.Product;
import com.example.api.core.product.ProductService;
import com.example.api.core.product.persistence.ProductRepository;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.util.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;

@RestController
@RequestMapping(value = "/products")
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductMapper mapper = ProductMapper.INSTANCE;

    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;

    @Override
    public Mono<Product> getProduct(int productId) {
        LOG.info("Will get product info for id={}", productId);
        return repository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
                .log(LOG.getName(), FINE)
                .map(mapper::entityToDto)
                .map(this::setServiceAddress);
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return repository.save(mapper.dtoToEntity(product))
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + product.getProductId()))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        return repository.findByProductId(productId)
                .log(LOG.getName(), FINE)
                .map(repository::delete)
                .flatMap(e -> e);
    }

    private Product setServiceAddress(Product product) {
        product.setServiceAddress(serviceUtil.getServiceAddress());
        return product;
    }
}
