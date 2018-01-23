package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppVersion;

/**
 * app版本信息业务层
 * @author Administrator
 *
 */
public interface AppVersionService {
	/**
	 * 根据appId查询相应的app版本列表
	 * @param appId
	 * @return
	 */
	List<AppVersion> getAppVersionListByAppId(Integer appId) throws Exception;
	
	/**
	 * 增加app版本信息
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	boolean appsysAdd (AppVersion appVersion) throws Exception;
	/**
	 * 根据版本id获取appVersion信息
	 * @param appVersionId
	 * @return
	 */
	AppVersion getAppVersionById(int appVersionId) throws Exception;
	/**
	 * 修改appVersion版本信息
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	boolean modify(AppVersion appVersion) throws Exception;
	/**
	 *根据id 删除apkFile  
	 * @param parseInt
	 * @return
	 */
	boolean deleteApkFile(int id) throws Exception;

}
