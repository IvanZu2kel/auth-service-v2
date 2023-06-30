package com.example.authservice.controller;

import com.example.authservice.api.Person;
import com.example.authservice.config.JwtAuthentication;
import com.example.authservice.config.Role;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class Controller {
    private final AuthService authService;

    private final PersonService personService;


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        log.info("{}", authInfo);
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Person crate(@RequestBody Person request) {
        return personService.create(request);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public List<Person> findAll() {
        return personService.getAll();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public Person getBuId(@PathVariable long id) {
        return personService.getUserById(id);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/all")
    public List<Person> findAllUser() {
        return personService.getAll();
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{id}")
    public Person getBuIdUser(@PathVariable long id) {
        return personService.getUserById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        personService.removeById(String.valueOf(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public Person update(@PathVariable long id, @RequestBody Person request) {
        return personService.update(id, request);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}/role")
    public Person updateRole(@PathVariable long id, @RequestParam(name = "text") String description) {
        return personService.updateRole(id, Role.valueOf(description));
    }
}