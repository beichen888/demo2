package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.CopyWriter;
import com.demo2.user.model.UserLanguage;

import java.util.List;

public interface CopyWriterDao extends BaseJpaRepository<CopyWriter>{

    List<CopyWriter> findCopyWritersByLanguageEquals(UserLanguage language);
}
