package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.Comment;
import com.unipi.msc.raiseupapi.Model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}