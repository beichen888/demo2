package com.demo2.label.service;

import com.demo2.label.dao.LabelDao;
import com.demo2.label.model.Label;
import com.demo2.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/4.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class LabelService implements ILabelService {
    @Resource
    private LabelDao labelDao;

    @Override
    public Label find(Integer id) {
        return labelDao.findOne(id);
    }

    @Override
    public List<Label> list(Sort sort) {
        return labelDao.findAll(sort);
    }

    @Override
    public Page<Label> listByPage(Pageable pageable, String keyword, Date start, Date end) {
        Specification specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyword)) {
                predicates.add(cb.like(root.get("name"), "%" + keyword + "%"));
            }
            if (start != null && end != null) {
                predicates.add(cb.between(root.get("createdDate"), start, end));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Label> labelPage = labelDao.findAll(specification, pageable);
        List<Label> labelList = labelPage.getContent();
        for (Label label : labelList) {
            User user = label.getCreator();
            String nickName = user.getNickname();
            if (StringUtils.isNotBlank(nickName)) {
                try {
                    nickName = URLDecoder.decode(nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            user.setNickname(nickName);
            label.setCreator(user);
        }
        Page<Label> page = new PageImpl<>(labelList,pageable,labelPage.getTotalElements());
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Label create(Label label) {
        label.setCreatedDate(new Date());
        return labelDao.save(label);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Label update(Label label) {
        if (label.getId() != null) {
            Label db = labelDao.findOne(label.getId());
            label.setCreatedDate(db.getCreatedDate());
            label.setCreator(db.getCreator());
            return labelDao.save(label);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        labelDao.delete(id);
    }
}
