package com.liyu.server.service;

import com.liyu.server.tables.pojos.Grade;

import java.util.List;

public interface GradeService {

    Grade create(Grade newGrade);

    List<Grade> list();

    Grade update(String gradeId, Grade newGrade);

    void delete(String gradeId);
}
