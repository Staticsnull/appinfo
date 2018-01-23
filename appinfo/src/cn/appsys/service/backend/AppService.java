package cn.appsys.service.backend;

import java.util.List;

import cn.appsys.pojo.AppInfo;

/**
 * 后台管理用户信息的业务层
 * @author Administrator
 *
 */
public interface AppService {
	
	/**
	 * 根据条件查询appInfo表记录数
	 * @param querySoftwareName
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @return
	 */
	int getAppInfoCount(String querySoftwareName, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId) throws Exception;
	/**
	 * 根据条件查询出appInfo列表信息
	 * @param querySoftwareName
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 */
	List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId,
			Integer currentPageNo, int pageSize) throws Exception;
	/**
	 * 根据app id获取appInfo 信息
	 * @param appId
	 * @return
	 */
	AppInfo getAppInfoById(int appId) throws Exception;
	/**
	 * 根据id修改app审核信息
	 * @param status
	 * @param id
	 * @return
	 */
	boolean updateStatus(Integer status, Integer id) throws Exception;
	

}
