package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.Image;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findById(@NonNull Long id);
}