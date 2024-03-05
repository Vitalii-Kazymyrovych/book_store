package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.model.Role;
import com.project.bookstore.dto.role.CreateRoleRequestDto;
import java.util.Arrays;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    @Mapping(target = "roleName", source = "roleName", qualifiedByName = "roleNameToString")
    RoleDto toDto(Role role);

    @Mapping(target = "roleName", source = "roleName", qualifiedByName = "createNewRole")
    Role toModel(CreateRoleRequestDto requestDto);

    @Named("createNewRole")
    default Role.RoleName createNewRole(String roleName) {
        return Arrays.stream(Role.RoleName.values())
                .filter(rn -> rn.toString().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid role name: " + roleName));
    }

    @Named("roleNameToString")
    default String roleNameToString(Role.RoleName roleName) {
        return roleName.toString();
    }
}
