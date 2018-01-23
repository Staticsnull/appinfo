package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;
import cn.appsys.tools.Constants;

/**
 * 后台管理系统用户登录控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/manager")
public class UserLoginController {
	private Logger logger = Logger.getLogger(UserLoginController.class);
	@Resource
	private BackendUserService backendUserService;
	/**
	 * 后台管理系统用户登录页面
	 * @return
	 */
	@RequestMapping("/login")
	public String login(){
		logger.info("UserLoginController==login==>");
		return "backendlogin";
	}
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,
				HttpServletRequest request,HttpSession session){
		BackendUser user = null;
		try {
			user = backendUserService.login(userCode,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != user){//登录成功
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/manager/backend/main";
		}else{
			request.setAttribute("error", "用户名或者密码有误!!");
			return "backendlogin";
		}
	}
	/**
	 * 进入主页面
	 * @param session
	 * @return
	 */
	@RequestMapping("/backend/main")
	public String main(HttpSession session){
		if(null == session.getAttribute(Constants.USER_SESSION)){
			return "redirect:/manager/login";
		}
		return "backend/main";
	}
	/**
	 * 用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
	

}
