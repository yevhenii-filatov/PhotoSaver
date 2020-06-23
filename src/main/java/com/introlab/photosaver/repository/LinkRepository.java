package com.introlab.photosaver.repository;

import com.introlab.photosaver.model.entity.Link;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yevhenii Filatov
 * @since 6/19/20
 **/

@Repository
public interface LinkRepository extends CrudRepository<Link, Long> {
    Link findByValue(String profileUrl);

    @Transactional
    @Modifying
    @Query("update Link l set l.profilePhotoName = :name where l.value = :profileUrl")
    int setImageNameForUrl(@Param("profileUrl") String profileUrl, @Param("name") String name);
}
