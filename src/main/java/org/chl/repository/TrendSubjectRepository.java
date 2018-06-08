package org.chl.repository;

import org.chl.model.Support;
import org.chl.model.TrendSubject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 06/08/2018.
 */
public interface TrendSubjectRepository extends MongoRepository<TrendSubject, String> {
    // List<TrendSubject> findTrendSubjects(Sort sort);
}
