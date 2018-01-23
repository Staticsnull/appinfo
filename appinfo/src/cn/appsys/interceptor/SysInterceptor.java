package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;
/**
 * 系统拦截器 拦截开发者用户登录 以及 后台管理用户登录
 * @author Administrator
 *
 */
public class SysInterceptor extends HandlerInterceptorAdapter{
	private Logger logger = Logger.getLogger(SysInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("preHandle====>");
		HttpSession session = request.getSession();
		BackendUser backendUser = (BackendUser) session.getAttribute(Constants.USER_SESSION);
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER_SESSION);
		if(null != devUser){//登录成功
			return true;
		}else if(null != backendUser){//登录成功
			return true;
		}else {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
	}

}
