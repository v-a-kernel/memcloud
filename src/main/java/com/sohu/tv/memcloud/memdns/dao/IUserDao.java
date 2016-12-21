package com.sohu.tv.memcloud.memdns.dao;

import com.sohu.tv.memcloud.memdns.dao.entry.User;

public interface IUserDao {

	public User get(Long id);
	
	public Long save(User user);
	
	public void update(User user);
	
	public void delete(Long id);
	
	public User getByName(String name);
}
