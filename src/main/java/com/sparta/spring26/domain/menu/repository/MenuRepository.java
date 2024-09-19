package com.sparta.spring26.domain.menu.repository;

import com.sparta.spring26.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
