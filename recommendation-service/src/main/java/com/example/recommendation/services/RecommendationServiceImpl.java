package com.example.recommendation.services;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.recommendation.persistence.RecommendationEntity;
import com.example.recommendation.persistence.RecommendationRepository;
import com.example.util.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/recommendations")
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final RecommendationMapper mapper = RecommendationMapper.INSTANCE;

    private final RecommendationRepository repository;
    private final ServiceUtil serviceUtil;

    @Override
    public Mono<Recommendation> createRecommendation(Recommendation recommendation) {

        return repository.save(mapper.dtoToEntity(recommendation))
                .log(LOG.getName(), Level.FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + recommendation.getProductId() + ", Recommendation Id:" + recommendation.getRecommendationId()))
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {

        return repository.findByProductId(productId)
                .log(LOG.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .map(this::setServiceAddress);
    }

    private Recommendation setServiceAddress(Recommendation e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }

    @Override
    public Mono<Void> deleteRecommendations(int productId) {

        LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        return repository.deleteAll(repository.findByProductId(productId));
    }
}