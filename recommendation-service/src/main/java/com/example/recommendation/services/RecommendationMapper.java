package com.example.recommendation.services;

import java.util.List;

import com.example.api.core.recommendation.Recommendation;
import com.example.recommendation.persistence.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecommendationMapper {

    RecommendationMapper INSTANCE = Mappers.getMapper(RecommendationMapper.class);

    @Mappings({
            @Mapping(target = "rate", source = "entity.rating"),
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Recommendation entityToDto(RecommendationEntity entity);

    @Mappings({
            @Mapping(target = "rating", source = "dto.rate"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    RecommendationEntity dtoToEntity(Recommendation dto);

    List<Recommendation> entityListToDtoList(List<RecommendationEntity> entityList);

    List<RecommendationEntity> dtoListToEntityList(List<Recommendation> dtoList);
}
