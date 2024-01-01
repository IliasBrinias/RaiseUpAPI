package com.unipi.msc.riseupapi.Repository;

import com.unipi.msc.riseupapi.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<List<Tag>> findByIdIn(List<Long> id);
    List<Tag> findAllByNameContaining(String keyword);
}