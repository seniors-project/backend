package com.side.common.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<D, E> {

	D toDto(E entity);

	E toEntity(D dto);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntity(D dto, @MappingTarget E entity);

	default List<D> toDtoList(final Collection<E> entities) {
		return entities.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	default List<E> toEntityList(final Collection<D> dtos) {
		return dtos.stream()
				.map(this::toEntity)
				.collect(Collectors.toList());
	}
}