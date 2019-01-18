package com.demo2.label.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.label.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface LabelDao extends BaseJpaRepository<Label> {

    Page<Label> findByNameLikeAndCreatedDateBetween(Pageable pageable, String keyword, Date startDate, Date endDate);
}
