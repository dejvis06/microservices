package com.example.review.services;

import java.util.List;

import com.example.api.core.review.Review;
import com.example.review.persistence.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ReviewMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Review entityToDto(ReviewEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ReviewEntity dtoToEntity(Review dto);

    List<Review> entityListToDtoList(List<ReviewEntity> entityList);

    List<ReviewEntity> dtoListToEntityList(List<Review> dtoList);
}