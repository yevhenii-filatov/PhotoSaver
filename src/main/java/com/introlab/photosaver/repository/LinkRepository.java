package com.introlab.photosaver.repository;

import com.introlab.photosaver.model.entity.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yevhenii Filatov
 * @since 6/19/20
 **/

@Repository
public interface LinkRepository extends CrudRepository<Link, Long> {
    Link findByValue(String profileUrl);
}
