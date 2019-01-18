package com.demo2.label.service;

import com.demo2.label.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/4.
 */
public interface ILabelService {

    Label find(Integer id);

    List<Label> list(Sort sort);

    Page<Label> listByPage(Pageable pageable, String keyword, Date startDate, Date endDate);

    Label create(Label label);

    Label update(Label label);

    void delete(Integer id);
}
