package cn.appsys.service.backend.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	@Resource
	private BackendUserMapper backendUserMapper; 
	@Override
	public BackendUser login(String userCode, String userPassword)
			throws Exception {
		BackendUser user = null;
		user = backendUserMapper.getLoginUser(userCode);
		if(null != user){//登录成功
			if(!userPassword.equals(user.getUserPassword())){
				user = null;
			}
		}
		return user;
	}

}
