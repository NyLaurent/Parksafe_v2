package com.rca.parksafe.controller;

import com.rca.parksafe.dto.CarResponse;
import com.rca.parksafe.entity.Car;
import com.rca.parksafe.entity.User;
import com.rca.parksafe.repository.CarRepository;
import com.rca.parksafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final CarRepository carRepository;

    private CarResponse mapToCarResponse(Car car) {
        CarResponse dto = new CarResponse();
        dto.setId(car.getId());
        dto.setPlateNumber(car.getPlateNumber());
        dto.setParked(car.isParked());
        dto.setParkedAt(car.getParkedAt() != null ? car.getParkedAt().toString() : null);
        dto.setDriverUsername(car.getDriver().getUsername());
        dto.setDriverEmail(car.getDriver().getEmail());
        return dto;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CarResponse>> getAllCars() {
        List<CarResponse> cars = carRepository.findAll().stream()
                .map(this::mapToCarResponse)
                .toList();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/cars/parked")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CarResponse>> getAllParkedCars() {
        List<CarResponse> cars = carRepository.findAll().stream()
                .filter(Car::isParked)
                .map(this::mapToCarResponse)
                .toList();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/cars/unparked")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CarResponse>> getAllUnparkedCars() {
        List<CarResponse> cars = carRepository.findAll().stream()
                .filter(car -> !car.isParked())
                .map(this::mapToCarResponse)
                .toList();
        return ResponseEntity.ok(cars);
    }

    @PostMapping("/cars/unpark/{carId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> unparkAnyCar(@PathVariable Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        car.setParked(false);
        carRepository.save(car);
        return ResponseEntity.ok(mapToCarResponse(car));
    }
}