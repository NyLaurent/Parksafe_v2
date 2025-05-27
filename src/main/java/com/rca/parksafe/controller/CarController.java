package com.rca.parksafe.controller;

import com.rca.parksafe.dto.CarRequest;
import com.rca.parksafe.entity.Car;
import com.rca.parksafe.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/park")
    public ResponseEntity<Car> parkCar(@RequestBody CarRequest request) {
        return ResponseEntity.ok(carService.parkCar(request));
    }

    @PostMapping("/unpark/{carId}")
    public ResponseEntity<Car> unparkCar(@PathVariable Long carId) {
        return ResponseEntity.ok(carService.unparkCar(carId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Car>> getMyCars() {
        return ResponseEntity.ok(carService.getMyCars());
    }
}