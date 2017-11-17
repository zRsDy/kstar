package com.ibm.kstar.api.system.permission;

import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.api.system.permission.entity.VerificationCode;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

public interface IUserService {

	void save(User user)throws AnneException;
	
	void update(User user)throws AnneException;
	
	IPage query(PageCondition condition) throws AnneException;
	
	IPage queryWithPosition(PageCondition condition) throws AnneException;
	
	void delete(String userId) throws AnneException;
	
	User get(String userId) throws AnneException;
	
	void changePosition(String userId,String positionId);

	void modifyPassword(User user) throws AnneException;

	void resetPassword(String userId, String password) throws AnneException;
	
	void resetPasswordById(String id) throws AnneException;
	
	void sendMail(final String email, final String title,final String content) ;

	void findPassword(String email);

	VerificationCode getLastVerificationCode(String email, String verificationCode);
}
