package cn.appsys.service.developer.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoMapper appInfoMapper;
	@Resource
	private AppVersionMapper appVersionMapper;
	
	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId, Integer devId, Integer currentPageNo,
			Integer pageSize) throws Exception {
		return appInfoMapper.getAppInfoList(querySoftwareName, queryStatus,
					queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				queryFlatformId, devId, (currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId)
			throws Exception {
		return appInfoMapper.getAppInfoCount(querySoftwareName, queryStatus, 
					queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, 
					queryFlatformId, devId);
	}

	@Override
	public AppInfo getAppInfo(Integer id, String APKName) throws Exception{
		return appInfoMapper.getAppInfo(id, APKName);
	}

	@Override
	public boolean add(AppInfo appInfo) throws Exception{
		return appInfoMapper.add(appInfo) > 0 ? true : false;
	}

	@Override
	public boolean deleteAppLogo(int id) throws Exception{
		return appInfoMapper.deleteAppLogo(id) > 0 ? true : false;
	}

	@Override
	public boolean modify(AppInfo appInfo) throws Exception {
		return appInfoMapper.modify(appInfo) > 0 ? true : false;
	}
	/**
	 * 根据app的id删除app信息 
	 * 1.通过appId 查询是否有appVersion信息,查询app_version表中是否有关联的数据
	 * 2.若有关联的app_version 信息 ,则先删除版本信息,然后删除app信息
	 * 3.若没有关联的app_version 信息,则直接删除app信息
	 */
	@Override
	public boolean deleteAppInfoAndAppVersionById(int id) throws Exception {
		//根据app id 查询app_version 中的是否有记录
		int versionCount = appVersionMapper.getVersionCountByAppId(id);
		List<AppVersion> appVersionList = null;
		//若有记录, 先删版本信息
		if(versionCount > 0){
			appVersionList = appVersionMapper.getAppversionListByAppId(id);
			for(AppVersion appVersion : appVersionList){
				//1 先删除用户上传的apk文件
				if(null != appVersion.getApkLocPath() && !"".equals(appVersion.getApkLocPath())){
					File file = new File(appVersion.getApkLocPath());
					if(file.exists()){
						if(!file.delete()){
							throw new RuntimeException("删除版本信息中的apk文件失败!");
						}
					}
				}
			}
			//<2> 再删除app_version表数据
			appVersionMapper.deleteAppVersionByAppId(id);
		}
		//再删除app信息
		AppInfo appInfo = appInfoMapper.getAppInfo(id, null);
		if(null != appInfo.getLogoLocPath() && !"".equals(appInfo.getLogoLocPath())){
			File file = new File(appInfo.getLogoLocPath());
			if(file.exists()){
				if(!file.delete()){
					throw new RuntimeException("删除app信息中的logo图片失败!");
				}
			}
		}
		//删除app数据信息
		return appInfoMapper.deleteAppInfoById(id) > 0 ? true : false;
	}
	
	/*
	 * 上架： 
		1 更改status由【2 or 5】 to 4 ， 上架时间
		2 根据versionid 更新 publishStauts 为 2 
		
		下架：
		更改status 由4给为5
	 */
	@Override
	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj)
			throws Exception {
		Integer operator = appInfoObj.getModifyBy();
		if(operator < 0 || appInfoObj.getId() <0){
			throw new Exception();
		}
		AppInfo appInfo = appInfoMapper.getAppInfo(appInfoObj.getId(), null);
		if(null == appInfo){
			return false;
		}else{
			switch (appInfo.getStatus()) {
			case 2://当状态为审核通过时，可以进行上架操作
				onSale(appInfo,operator,4,2);
				break;
			case 5://当状态为下架时，可以进行上架操作
				onSale(appInfo,operator,4,2);
				break;
			case 4://当状态为上架时，可以进行下架操作
				offSale(appInfo,operator,5);
				break;
			default:
				return false;
			}
		}
		return true;
	}
	/**
	 * 修改状态为上架状态
	 * @param appInfo
	 * @param operator
	 * @param appInfoStatus
	 * @param appVersionStatus
	 * @throws Exception
	 */
	public void onSale(AppInfo appInfo, Integer operator, 
			Integer appInfoStatus, Integer appVersionStatus) throws Exception{
		offSale(appInfo,operator,appInfoStatus);
		setSaleSwitchToAppVersion(appInfo,operator,appVersionStatus);
	}
	/**
	 * 根据app id 修改app_info的状态,修改时间 以及下架时间
	 * @param appInfo
	 * @param operator
	 * @param appInfoStatus
	 * @return
	 * @throws Exception
	 */
	public boolean offSale(AppInfo appInfo, Integer operator, 
				int appInfoStatus) throws Exception {
		AppInfo app = new AppInfo();
		app.setId(appInfo.getId());
		app.setStatus(appInfoStatus);
		app.setModifyBy(operator);
		app.setOffSaleDate(new Date(System.currentTimeMillis()));
		appInfoMapper.modify(app);
		return true;
	}
	/**
	 * 根据id修改app_version的版本状态 以及发布状态id,修改者以及修改时间
	 * @param appInfo
	 * @param operator
	 * @param appVersionStatus
	 * @return
	 * @throws Exception
	 */
	public boolean setSaleSwitchToAppVersion(AppInfo appInfo, Integer operator,
			Integer appVersionStatus) throws Exception{
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(appVersionStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionMapper.modify(appVersion);
		return false;
	}
	

}
