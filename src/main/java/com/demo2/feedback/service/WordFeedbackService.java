package com.demo2.feedback.service;

import com.demo2.user.model.User;
import com.demo2.feedback.dao.WordFeedbackDao;
import com.demo2.feedback.model.WordFeedback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

@Service
@Transactional(readOnly = true)
public class WordFeedbackService implements IWordFeedbackService {
    @Resource
    private WordFeedbackDao wordFeedbackDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(WordFeedback wordFeedback) {
        wordFeedback.setCreateDate(new Date());
        wordFeedbackDao.save(wordFeedback);
    }

    @Override
    public WordFeedback find(Integer id) {
        return wordFeedbackDao.findOne(id);
    }

    @Override
    public Page<WordFeedback> list(Pageable pageable,String keyword) {
        Specification specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyword)) {
                predicates.add(cb.like(root.get("creator.nickname"), "%" + keyword + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WordFeedback> feedbackPage = wordFeedbackDao.findAll(specification, pageable);
        List<WordFeedback> wordFeedbackList = feedbackPage.getContent();
        for (WordFeedback wordFeedback : wordFeedbackList) {
            User user = wordFeedback.getCreator();
            String nickName = user.getNickname();
            if (StringUtils.isNotBlank(nickName)) {
                try {
                    nickName = URLDecoder.decode(nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            user.setNickname(nickName);
            wordFeedback.setCreator(user);
        }
        Page<WordFeedback> page = new PageImpl<>(wordFeedbackList,pageable,feedbackPage.getTotalElements());
        return page;
    }
}
