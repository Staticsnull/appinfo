package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppInfo;

/**
 * APP信息维护业务层
 * @author Administrator
 *
 */
public interface AppInfoService {
	/**
	 * 根据条件查询出app列表
	 * @param querySoftwareName:软件名称
	 * @param queryStatus:app状态
	 * @param queryCategoryLevel1:一级分类
	 * @param queryCategoryLevel2:二级分类
	 * @param queryCategoryLevel3:三级分类
	 * @param queryFlatformId:app所属平台id
	 * @param devId:开发者id
	 * @param currentPageNo:当前页
	 * @param pageSize:页面显示的记录数
	 * @return
	 * @throws Exception
	 */
	List<AppInfo> getAppInfoList(String querySoftwareName,Integer queryStatus,
								Integer queryCategoryLevel1,Integer queryCategoryLevel2,
								Integer queryCategoryLevel3,Integer queryFlatformId,
								Integer devId,Integer currentPageNo,Integer pageSize)throws Exception;
	
	/**
	 * 根据条件查询appInfo表记录数
	 * @param querySoftwareName:软件名称
	 * @param queryStatus:APP 状态
	 * @param queryCategoryLevel1:一级分类
	 * @param queryCategoryLevel2:二级分类
	 * @param queryCategoryLevel3:三级分类
	 * @param queryFlatformId:所属平台id
	 * @param devId:开发者id
	 * @return
	 * @throws Exception
	 */
	int getAppInfoCount(String querySoftwareName,Integer queryStatus,
							Integer queryCategoryLevel1,Integer queryCategoryLevel2,
							Integer queryCategoryLevel3,Integer queryFlatformId,Integer devId)throws Exception;
	
	
	AppInfo getAppInfo(Integer id,String APKName)throws Exception;
	
	/**
	 * 保存App信息
	 * @param appInfo
	 * @return
	 */
	boolean add(AppInfo appInfo) throws Exception;
	/**
	 * 根据id删除appInfo的logo 图片以及路径
	 * @param id
	 * @return
	 */
	boolean deleteAppLogo(int id)throws Exception;
	/**
	 * 修改用户信息
	 * @param appInfo
	 * @return
	 */
	boolean modify(AppInfo appInfo) throws Exception;
	/**
	 * 根据id删除app基础信息 以及app版本信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteAppInfoAndAppVersionById(int id) throws Exception;
	/**
	 * 根据app id修改app状态 为上架或者下架状态
	 * @param appInfo
	 * @return
	 */
	boolean appsysUpdateSaleStatusByAppId(AppInfo appInfo) throws Exception;


}
