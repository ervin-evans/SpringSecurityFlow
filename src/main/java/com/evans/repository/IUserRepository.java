package com.evans.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evans.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByEmal(String email);

}
