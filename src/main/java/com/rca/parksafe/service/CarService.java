package com.rca.parksafe.service;

import com.rca.parksafe.dto.CarRequest;
import com.rca.parksafe.entity.Car;
import com.rca.parksafe.entity.User;
import com.rca.parksafe.repository.CarRepository;
import com.rca.parksafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Car parkCar(CarRequest request) {
        User driver = getCurrentUser();
        Car car = new Car();
        car.setPlateNumber(request.getPlateNumber());
        car.setDriver(driver);
        car.setParked(true);
        car.setParkedAt(LocalDateTime.now());
        return carRepository.save(car);
    }

    public Car unparkCar(Long carId) {
        User driver = getCurrentUser();
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        if (!car.getDriver().getId().equals(driver.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        car.setParked(false);
        carRepository.save(car);
        return car;
    }

    public List<Car> getMyCars() {
        User driver = getCurrentUser();
        return carRepository.findByDriver(driver);
    }
}