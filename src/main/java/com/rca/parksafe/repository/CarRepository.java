package com.rca.parksafe.repository;

import com.rca.parksafe.entity.Car;
import com.rca.parksafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByDriver(User driver);
} 