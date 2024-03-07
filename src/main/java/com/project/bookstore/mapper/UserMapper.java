package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import com.project.bookstore.model.Role;
import com.project.bookstore.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(
            target = "roleIds",
            source = "roles",
            qualifiedByName = "createRoleIdSet")
    UserWithRolesDto toUserWithRolesDto(User user);

    UserDto toDto(User user);

    User toModel(CreateUserRequestDto requestDto);

    @Named("createRoleIdSet")
    default Set<Long> createRoleIdSet(Set<Role> roles) {
        return roles
                .stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}
