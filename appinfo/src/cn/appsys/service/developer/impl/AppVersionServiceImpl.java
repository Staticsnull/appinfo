package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppVersionService;
@Service
public class AppVersionServiceImpl implements AppVersionService {
	
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private AppInfoMapper appInfoMapper;
	@Override
	public List<AppVersion> getAppVersionListByAppId(Integer appId)
			throws Exception {
		return appVersionMapper.getAppversionListByAppId(appId);
	}
	/**1、app_verion表插入数据
	 * 2、更新app_info表对应app的versionId字段（记录最新版本id）
	 * 注意：事务控制
	 */
	@Override
	public boolean appsysAdd(AppVersion appVersion) throws Exception {
		boolean flag = false;
		Integer versionId = null;
		if(appVersionMapper.add(appVersion) > 0){
			versionId = appVersion.getId();
			flag = true;
		}
		if(appInfoMapper.updateVersionId(versionId,appVersion.getAppId()) > 0 && flag){
			flag = true;
		}
//		return appInfoMapper.updateVersionId(versionId,appVersion.getAppId()) > 0 && flag ? true : false;
		return flag;
	}

	@Override
	public AppVersion getAppVersionById(int appVersionId) throws Exception {
		return appVersionMapper.getAppVersionById(appVersionId);
	}
	@Override
	public boolean modify(AppVersion appVersion) throws Exception {
		return appVersionMapper.modify(appVersion) > 0 ? true : false;
	}
	@Override
	public boolean deleteApkFile(int id) throws Exception {
		return appVersionMapper.deleteApkFile(id) > 0 ? true : false;
	}

}
