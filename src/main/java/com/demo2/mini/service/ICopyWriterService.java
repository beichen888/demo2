package com.demo2.mini.service;

import com.demo2.mini.model.CopyWriter;
import com.demo2.user.model.UserLanguage;

import java.util.List;

/**
 * Created by miguo on 2018/6/10.
 */
public interface ICopyWriterService {

    List<CopyWriter> listByLanguage(UserLanguage language);
 }
