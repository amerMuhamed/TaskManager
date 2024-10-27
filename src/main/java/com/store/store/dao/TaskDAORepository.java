package com.store.store.dao;

import com.store.store.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDAORepository extends JpaRepository<Task,Integer> {
}
