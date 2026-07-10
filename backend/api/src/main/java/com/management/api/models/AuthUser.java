package com.management.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.postgis.jdbc.geometry.Geometry;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthUser {

    private Long id_auth;

    private String username;

    private String password;

    private String email;

    private String roleName;

    private Geometry location;
}
