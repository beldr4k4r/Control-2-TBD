package com.management.api.services;

import com.management.api.config.JwtUtils;
import com.management.api.dto.LoginDTO;
import com.management.api.dto.RegisterDTO;
import com.management.api.models.AuthUser;
import com.management.api.repositories.AuthUserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public String createUser(RegisterDTO user) {
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas deben ser iguales");
        }
        if(authUserRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Este username ya esta registrado");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        AuthUser newUser = new AuthUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(hashedPassword);
        GeometryFactory geometryFactory = new GeometryFactory();
        Point locationPoint = geometryFactory.createPoint(new Coordinate(user.getLongitud(), user.getLatitud()));
        locationPoint.setSRID(4326);
        newUser.setLocation(locationPoint);
        authUserRepository.save(newUser);
        return "Usuario registrado con exito";
    }
    public String loginUser(LoginDTO login) {
        AuthUser user = authUserRepository.findByUsername(login.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user);

        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }
}
