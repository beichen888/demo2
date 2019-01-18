package com.demo2.word.service;


import com.demo2.common.exception.AppException;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IWordService {

    Word find(Integer id);

    List<Word> list(Sort sort);

    Page<Word> listByPage(Pageable pageable, ChineseLevel level, String keyword, Date start, Date end);

    Word create(Word label);

    Word update(Word label);

    void delete(Integer id);

    List<String> importExcel(String folder,String excelName) throws AppException, IOException;

    long countByLevel(ChineseLevel level);

    List<Word> listByLevel(ChineseLevel level);
}
