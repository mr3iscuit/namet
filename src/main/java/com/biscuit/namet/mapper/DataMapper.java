package com.biscuit.namet.mapper;

import com.biscuit.namet.dto.DataRequest;
import com.biscuit.namet.dto.DataResponse;
import com.biscuit.namet.entity.DataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    DataEntity toEntity(DataRequest dataRequest);

    DataResponse toDataResponse(DataEntity stored);
}
