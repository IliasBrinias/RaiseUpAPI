package com.unipi.msc.riseupapi.Repository;

import com.unipi.msc.riseupapi.Model.Image;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}