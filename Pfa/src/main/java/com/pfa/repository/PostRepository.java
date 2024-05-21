package com.pfa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pfa.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	@Query("Select p from Post p where p.user.id=:userId")
	List<Post> findPostByUserId(Integer userId);
	
	

}
