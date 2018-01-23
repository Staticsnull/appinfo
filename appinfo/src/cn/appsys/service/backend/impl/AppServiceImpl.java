package cn.appsys.service.backend.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.service.backend.AppService;

@Service
public class AppServiceImpl implements AppService {
	@Resource
	private AppInfoMapper appInfoMapper;
	@Override
	public int getAppInfoCount(String querySoftwareName,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId) throws Exception{
		return appInfoMapper.getAppInfoCount(querySoftwareName, 1, 
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				queryFlatformId, null);
	}

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId,
			Integer currentPageNo, int pageSize) throws Exception{
		return appInfoMapper.getAppInfoList(querySoftwareName, 1, queryCategoryLevel1, 
				queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, null,
				(currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public AppInfo getAppInfoById(int appId) throws Exception {
		return appInfoMapper.getAppInfo(appId, null);
	}

	@Override
	public boolean updateStatus(Integer status, Integer id) throws Exception {
		return appInfoMapper.updateStatus(status,id) > 0 ? true : false;
	}

}
