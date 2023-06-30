package com.example.authservice.FeignClient;

import com.example.authservice.api.Person;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "FeignUserService", url = "localhost:8081/api")
public interface FeignUserService {
    @GetMapping("/{id}")
    Optional<Person> getPersonById(@PathVariable("id") Long id);

    @GetMapping("email/{email}")
    Optional<Person> getPersonByEmail(@PathVariable("email") String email);
}
