package com.example.review.services;

import com.example.api.core.review.Review;
import com.example.api.core.review.ReviewService;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.review.persistence.ReviewRepository;
import com.example.util.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;


@RestController
@RequestMapping(value = "/reviews")
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewMapper mapper = ReviewMapper.INSTANCE;

    private final ReviewRepository repository;
    private final ServiceUtil serviceUtil;

    @Override
    public Mono<Review> createReview(Review body) {

        return repository.save(mapper.dtoToEntity(body))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
                .log(LOG.getName(), FINE)
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<Review> getReviews(int productId) {

        return repository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No reviews found for productId: " + productId)))
                .map(mapper::entityToDto)
                .map(this::setServiceAddress);
    }

    private Review setServiceAddress(Review review) {
        review.setServiceAddress(serviceUtil.getServiceAddress());
        return review;
    }

    @Override
    public Mono<Void> deleteReviews(int productId) {
        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        return repository.deleteAll(repository.findByProductId(productId));
    }
}
