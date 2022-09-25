package com.example.review.services;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.example.api.core.review.Review;
import com.example.api.core.review.ReviewService;
import com.example.api.exceptions.InvalidInputException;
import com.example.review.persistence.ReviewEntity;
import com.example.review.persistence.ReviewRepository;
import com.example.util.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RestController
@RequestMapping(value = "/reviews")
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewMapper mapper = ReviewMapper.INSTANCE;

    private final ReviewRepository repository;
    private final ServiceUtil serviceUtil;
    private final Scheduler jdbcScheduler;

    @Override
    public Mono<Review> createReview(Review body) {

        return Mono.fromCallable(() -> internalCreateReview(body))
                .subscribeOn(jdbcScheduler);
    }

    private Review internalCreateReview(Review body) {
        try {

            ReviewEntity newEntity = repository.save(mapper.dtoToEntity(body));

            LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
            return mapper.entityToDto(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
        }
    }

    @Override
    public Flux<Review> getReviews(int productId) {

        LOG.info("Will get reviews for product with id={}", productId);

        return Mono.fromCallable(() -> internalGetReviews(productId))
                .flatMapMany(Flux::fromIterable)
                .log(LOG.getName(), Level.FINE)
                .subscribeOn(jdbcScheduler);
    }

    private List<Review> internalGetReviews(int productId) {

        List<Review> reviews = repository.findByProductId(productId)
                .stream().map(mapper::entityToDto)
                .map(this::setServiceAddress)
                .collect(Collectors.toList());

        LOG.debug("Response size: {}", reviews.size());

        return reviews;
    }

    private Review setServiceAddress(Review review) {
        review.setServiceAddress(serviceUtil.getServiceAddress());
        return review;
    }

    @Override
    public Mono<Void> deleteReviews(int productId) {

        return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
    }

    private void internalDeleteReviews(int productId) {

        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }
}
