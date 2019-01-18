package com.demo2.mini.service;

import com.demo2.mini.dao.CopyWriterDao;
import com.demo2.mini.model.CopyWriter;
import com.demo2.user.model.UserLanguage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by miguo on 2018/6/10.
 */

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CopyWriterService implements ICopyWriterService {
    @Resource
    private CopyWriterDao copyWriterDao;
    @Override
    public List<CopyWriter> listByLanguage(UserLanguage language) {
        return copyWriterDao.findCopyWritersByLanguageEquals(language);
    }
}
