package com.example.composite.product.services;

import com.example.api.composite.product.*;
import com.example.api.core.product.Product;
import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.review.Review;
import com.example.util.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.logging.Level.FINE;

@RestController
@RequestMapping(value = "/products-composite")
@AllArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;

    @Override
    public Mono<ProductAggregate> getProduct(int productId) {
        LOG.info("Will get composite product info for product.id={}", productId);

        return Mono.zip(
                values -> createProductAggregate(
                        (Product) values[0],
                        (List<Recommendation>) values[1],
                        (List<Review>) values[2],
                        serviceUtil.getServiceAddress()),
                integration.getProduct(productId),
                integration.getRecommendations(productId).collectList(),
                integration.getReviews(productId).collectList())
                .doOnError(ex -> LOG.warn("getCompositeProduct failed: {}", ex.toString()))
                .log(LOG.getName(), FINE);
    }

    @Override
    public Mono<Void> createProduct(ProductAggregate productAggregate) {
        /*try {
            Product product = new Product(productAggregate.getProductId(), productAggregate.getName(), productAggregate.getWeight(), null);
            integration.createProduct(product);
            if (productAggregate.getRecommendations() != null) {
                productAggregate.getRecommendations().forEach(r -> {
                    Recommendation recommendation =
                            new Recommendation(productAggregate.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent(), null);
                    integration.createRecommendation(recommendation);
                });
            }
            if (productAggregate.getReviews() != null) {
                productAggregate.getReviews().forEach(r -> {
                    Review review = new Review(productAggregate.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
                    integration.createReview(review);
                });
            }
        } catch (RuntimeException re) {
            LOG.warn("createCompositeProduct failed", re);
            throw re;
        }*/
        return null;
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        /*integration.deleteProduct(productId);
        integration.deleteRecommendations(productId);
        integration.deleteReviews(productId);*/
        return null;
    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries =
                (recommendations == null) ? null : recommendations.stream()
                        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
                        .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries =
                (reviews == null) ? null : reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
                        .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices addresses
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
    }
}
