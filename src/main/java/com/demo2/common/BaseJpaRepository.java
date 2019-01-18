package com.demo2.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 避免SpringDataJPA自动实例化
 */
@NoRepositoryBean
public interface BaseJpaRepository<T> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {
}
