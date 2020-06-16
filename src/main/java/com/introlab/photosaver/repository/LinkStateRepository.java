package com.introlab.photosaver.repository;

import com.introlab.photosaver.model.entity.LinkState;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Yevhenii Filatov
 * @since 6/16/20
 **/

public interface LinkStateRepository extends CrudRepository<LinkState, Long> {
    LinkState findByLink(String link);

    @Modifying
    @Query("update LinkState ls set ls.imageName = :imageUrl where ls.link = :profileUrl")
    void setImageNameForLink(@Param("profileUrl") String profileUrl, @Param("imageUrl") String imageUrl);
}
