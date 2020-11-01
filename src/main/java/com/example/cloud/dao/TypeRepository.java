package com.example.cloud.dao;

import com.example.cloud.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, String> {
}
