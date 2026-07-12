package com.management.api.controllers;


import com.management.api.dto.LoginDTO;
import com.management.api.dto.RegisterDTO;
import com.management.api.models.AuthUser;
import com.management.api.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthUserController {
    @Autowired
    private AuthUserService authUserService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO register) {
        try {
            String response = authUserService.createUser(register);
            return ResponseEntity.ok(response);
        }catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try {
            String response = authUserService.loginUser(login);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
