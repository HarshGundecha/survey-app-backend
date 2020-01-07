package com.happysolutions.surveyappbackend.repository;

import com.happysolutions.surveyappbackend.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository  extends JpaRepository<Survey, Long> {
}
