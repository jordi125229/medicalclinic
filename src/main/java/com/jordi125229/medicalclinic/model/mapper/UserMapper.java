package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "userId")
    UserDto userToDto (User user);
}
