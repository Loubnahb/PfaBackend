package com.pfa.service;

import java.util.List;

import com.pfa.models.Reels;
import com.pfa.models.User;

public interface ReelsService {
	
	public Reels createReel(Reels reel, User user);

	public List<Reels> findAllReels();
	public List<Reels> findUsersReel(Integer userId) throws Exception;
	
}
