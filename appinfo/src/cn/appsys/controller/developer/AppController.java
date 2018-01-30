package cn.appsys.controller.developer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.AppCategoryService;
import cn.appsys.service.developer.AppInfoService;
import cn.appsys.service.developer.AppVersionService;
import cn.appsys.service.developer.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;
/**
 * app维护
 * @author Administrator
 *delfile
 */
@Controller
@RequestMapping(value="/dev/flatform/app")
public class AppController {
	private Logger logger = Logger.getLogger(AppController.class);
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	/**
	 *  根据条件查询出app列表
	 * @param model:保存数据
	 * @param session:获取当前用户id
	 * @param querySoftwareName:软件名称
	 * @param queryStatus:app状态
	 * @param queryCategoryLevel1:一级分类
	 * @param queryCategoryLevel2:二级分类
	 * @param queryCategoryLevel3:三级分类
	 * @param queryFlatformId:app所属平台id
	 * @param pageIndex:当前页码
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getAppInfoList(Model model,HttpSession session,
				@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
				@RequestParam(value="queryStatus",required=false) String _queryStatus,
				@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
				@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
				@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
				@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
				@RequestParam(value="pageIndex",required=false) String pageIndex){
		logger.info("getAppInfoList-->querySoftwareName:"+querySoftwareName);
		logger.info("getAppInfoList-->_queryStatus:"+_queryStatus);
		logger.info("getAppInfoList-->_queryCategoryLevel1:"+_queryCategoryLevel1);
		logger.info("getAppInfoList-->_queryCategoryLevel2:"+_queryCategoryLevel2);
		logger.info("getAppInfoList-->_queryCategoryLevel3:"+_queryCategoryLevel3);
		logger.info("getAppInfoList-->_queryFlatformId:"+_queryFlatformId);
		logger.info("getAppInfoList-->pageIndex:"+pageIndex);
		//app信息列表
		List<AppInfo> appInfoList = null;
		//数据字典中的状态列表
		List<DataDictionary> statusList = null;
		//数据字典中的平台列表
		List<DataDictionary> flatFormList = null;
		//一级分类
		List<AppCategory> categoryLevel1List = null;
		//二级分类 注意二级分类与三级分类是通过ajax异步请求获取
		List<AppCategory> categoryLevel2List = null;
		//三级分类
		List<AppCategory> categoryLevel3List = null;
		//页面显示的记录数
		int pageSize = Constants.pageSize;
		//当前页码
		Integer currentPageNo = 1;
		if(null != pageIndex){
			currentPageNo = Integer.valueOf(pageIndex);
		}
		Integer queryStatus = null;
		if(null != _queryStatus && !"".equals(_queryStatus)){
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if(null != _queryCategoryLevel1 && !"".equals(_queryCategoryLevel1)){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if(null != _queryCategoryLevel2 && !"".equals(_queryCategoryLevel2)){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if(null != _queryCategoryLevel3 && !"".equals(_queryCategoryLevel3)){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if(null != _queryFlatformId && !"".equals(_queryFlatformId)){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		//获取开发者id
		Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		//获取总记录数
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, 
						queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, 
						queryFlatformId, devId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//控制首页和尾页
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);;
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appInfoService.getAppInfoList(querySoftwareName,
						queryStatus, queryCategoryLevel1, queryCategoryLevel2,
						queryCategoryLevel3, queryFlatformId, devId, currentPageNo, pageSize);
			statusList = dataDictionaryService.getDataDictionaryList("APP_STATUS");
			flatFormList = dataDictionaryService.getDataDictionaryList("APP_FLATFORM");
			categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
		} catch (Exception e) {
			e.printStackTrace();
		}//appInfoList
		for(AppInfo appInfo : appInfoList){
			logger.info("appInfoList :appInfo apkName"+appInfo.getSoftwareName());
		}
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages",pages);
		model.addAttribute("queryStatus",queryStatus);//querySoftwareName
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId",queryFlatformId);
		//二级分类列表和三级分类列表 回显  后期优化
		if(null != queryCategoryLevel2 && !"".equals(queryCategoryLevel2)){
			categoryLevel2List = getCategoryList(queryCategoryLevel1);
			model.addAttribute("categoryLevel2List",categoryLevel2List);
		}
		if(null != queryCategoryLevel3 && !"".equals(queryCategoryLevel3)){
			categoryLevel3List = getCategoryList(queryCategoryLevel2);
			model.addAttribute("categoryLevel3List",categoryLevel3List);
		}
		return "developer/appinfolist";
	}
	
	
	/**
	 * 根据父节点id 获取子节点信息 
	 * @param pid
	 * @return
	 */
	public List<AppCategory> getCategoryList (Integer pid){
		List<AppCategory> categoryLevelList = null;
		try {
			categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryLevelList;
	}
	/**
	 * 异步请求:根据父节点id 获取子节点信息 
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCategory> getAppCategoryList(@RequestParam Integer pid){
		logger.info(" getAppCategoryList==:pid"+pid);
		return getCategoryList(pid);
	}
	/**
	 * APP基础信息添加页面
	 * @return
	 */
	@RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
	public String appinfoAdd(/*@ModelAttribute("appInfo") AppInfo appInfo*/){
		logger.info("appinfoAdd===>");
		return "developer/appinfoadd";
	}
	/**
	 * 保存app添加信息 appinfoaddsave
	 * @param appInfo
	 * @param request
	 * @param session
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String appinfoAddSave(AppInfo appInfo,HttpServletRequest request,HttpSession session,
				@RequestParam(value="a_logoPicPath",required=false) MultipartFile attach){
		//LOGO图片URL路径
		String logoPicPath = null;
		//LOGO图片的服务器存储路径
		String logoLocPath = null;
		if(!attach.isEmpty()){
			//定义上传目标路径path(statics+File.separator：自适应的路径分隔符+uploadfiles)
			String path = request.getSession().getServletContext().
						getRealPath("statics"+File.separator+"uploadfiles");
			//获取原文件名oldName
			String oldName = attach.getOriginalFilename();
			//通过文件名  获取原文件后缀suffix、文件大小，并进行相应的判断比较
			String suffix = FilenameUtils.getExtension(oldName);
			int fileSize = 500000;
			if(attach.getSize() > fileSize){//文件大小不超过50k
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
			}else if(suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("pneg")
					|| suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg") ){
				//若满足以上规定（文件大小、文件后缀），则可以进行文件上传操作
				//定义上传文件名 fileName：当前系统时间+随机数+“_Personal.jpg”
				String fileName = appInfo.getAPKName()+".jpg";
				//根据完整文件名 构建新文件targetFile
				File targetFile = new File(path, fileName);
				//若不存在 则创建
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//将文件写入硬盘中(调用attach.transferTo(目标文件) )
				try {
					attach.transferTo(targetFile);
				} catch (IOException e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				}
				//获取文件完整路径名(path/fileName) 用于保存到数据库
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path + File.separator +fileName;
			}else{
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setStatus(1);
		try {
			if(appInfoService.add(appInfo)){
				//重定向到list页面
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}
	
	
	/**
	 * 异步验证用户输入的APKName 是否存在
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkNameExist(@RequestParam String APKName ){
		Map<String,String> resultMap = new HashMap<String,String>();
		if(StringUtils.isNullOrEmpty(APKName)){
			resultMap.put("APKName", "empty");
		}else{
			AppInfo appInfo = null;
			try {
				appInfo = appInfoService.getAppInfo(null,APKName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null != appInfo){
				resultMap.put("APKName", "exist");
			}else{
				resultMap.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * 根据typeCode动态加载所属平台列表
	 * @param tcode
	 * @return
	 */
	@RequestMapping(value="/datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<DataDictionary> getDataDiclist(@RequestParam String tcode){
		return this.getDataDictionarylist(tcode);
	}
	/**
	 * 根据typeCode查询出相应的数据字典列表
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> getDataDictionarylist(String typeCode){
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataDictionaryList;
	}
	/**
	 * 根据id显示修改App信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appinfomodify",method=RequestMethod.GET)
	public String appInfoModify(@RequestParam("id")String id,
				@RequestParam(value="error",required=false) String fileUploadError,
				Model model){
		AppInfo appInfo = null;
		//文件上传错误信息提示 回显至页面
		if(null != fileUploadError && "error2".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && "error3".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}else if(null != fileUploadError && "error4".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_4;
		}
		try {
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("fileUploadError",fileUploadError);
		model.addAttribute("appInfo", appInfo);
		return "developer/appinfomodify";
	}
	/**
	 * 修改操作时,删除apk文件/logo图片,根据id 异步删除图片
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delFile(@RequestParam(value="flag",required=false) String flag,
			@RequestParam(value="id",required=false) String id){
		logger.info("flag==>"+flag);
		logger.info("id===>"+id);
		Map<String,String> resultMap = new HashMap<String,String>();
		String fileLocPath = null;
		if(null == flag || "".equals(flag) || null == id || "".equals(id)){
			resultMap.put("result", "failed");
		}else if("logo".equals(flag)){//删除logo 图片failed
			try {
				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
				File file = new File(fileLocPath);
				if(file.exists()){
					if(file.delete()){//删除服务器存储的物理文件
						if(appInfoService.deleteAppLogo(Integer.parseInt(id))){
							resultMap.put("result","success");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("apk".equals(flag)){//删除apk文件（操作app_version）
			try {
				fileLocPath = appVersionService.getAppVersionById(Integer.parseInt(id)).getApkLocPath();
				File file = new File(fileLocPath);
				if(file.exists()){
					if(file.delete()){//删除服务器存储的物理文件
						if(appVersionService.deleteApkFile(Integer.parseInt(id))){//更新表
							resultMap.put("result", "success");
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String appinfoModifySave(AppInfo appInfo,HttpServletRequest request,HttpSession session,
								@RequestParam(value="attach",required=false) MultipartFile attach){
		String logoPicPath = null;
		String logoLocPath = null;//attach
		String APKName = appInfo.getAPKName();
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();
			String suffix = FilenameUtils.getExtension(oldFileName);//获取后缀
			int fileSize = 500000;//上传文件不得超过50k
			if(attach.getSize() > fileSize){
				return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error4";
			}else if(suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("pneg")
					|| suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")){
				//文件格式正确
				String fileName = APKName + ".jpg";//上传LOGO图片命名:apk名称.apk
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
							 +"&error=error2";
				}
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			}else{
				return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error3";
			}
		}
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		try {
			if(appInfoService.modify(appInfo)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	/**
	 * 增加app版本信息（跳转到新增app版本页面）对于上传的错误信息做出提示
	 * @param appId
	 * @param appVersion
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
	public String appversionAdd(@RequestParam("id")String appId,
				@RequestParam(value="error",required=false)String fileUploadError,
				AppVersion appVersion,Model model){
		//判断错误信息
		if(null != fileUploadError && "error1".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && "error2".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && "error3".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		
		appVersion.setAppId(Integer.parseInt(appId));
		List<AppVersion> appVersionList = null;
		try {
			appVersionList = appVersionService.getAppVersionListByAppId(Integer.parseInt(appId));
			appVersion.setAppName(appInfoService.getAppInfo(Integer.parseInt(appId), null).getSoftwareName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("fileUploadError", fileUploadError);
		return "developer/appversionadd";
	}
	/**
	 * 保存新增appversion数据（子表）-上传该版本的apk包
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/addversionsave",method=RequestMethod.POST)
	public String addversionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
								@RequestParam(value="a_downloadLink",required=false) MultipartFile attach){
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();
			String suffix = FilenameUtils.getExtension(oldFileName);
			if(suffix.equalsIgnoreCase("apk")){//apk文件
				String apkName = null;
				try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null == apkName || "".equals(apkName)){
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error1";
				}
				apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
				File targetFile = new File(path,apkFileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error2";
				}
				//得到下载链接的地址
				downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator +apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
				 +"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setDownloadLink(downloadLink);
		try {
			if(appVersionService.appsysAdd(appVersion)){
				//添加成功 重定向至list页面
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
	}
	
	/**
	 * 修改最新的appVersion版本信息,并且跳转到appVersion修改页面
	 * @param versionId
	 * @param appId
	 * @param model
	 * @param fileUploadError
	 * @return
	 */
	@RequestMapping(value="/appversionmodify",method=RequestMethod.GET)
	public String appversionModify(@RequestParam("vid") String versionId,
						@RequestParam("aid") String appId,Model model,
						@RequestParam(value="error",required=false) String fileUploadError){
		AppVersion appVersion = null;
		List<AppVersion> appVersionList = null;
		if(null != fileUploadError && "error1".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && "error2".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && "error3".equals(fileUploadError)){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		try {
			appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
			appVersionList = appVersionService.getAppVersionListByAppId(Integer.parseInt(appId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionmodify";
	}
	/**
	 * 保存修改后的appVersion版本信息 包扩下载链接,apk位置,apk文件
	 * @param appVersion
	 * @param request
	 * @param session
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
	public String appversionModifySave(AppVersion appVersion,HttpServletRequest request,
			HttpSession session,@RequestParam(value="attach",required=false) MultipartFile attach){
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();
			String suffix = FilenameUtils.getExtension(oldFileName);
			if(suffix.equalsIgnoreCase("apk")){
				String apkName = null;
				try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null == apkName || "".equals(apkName)){
					return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
						+"&aid="+appVersion.getAppId()+"&error=error1";	
				}
				apkFileName = apkName + "-" + appVersion.getVersionNo() + ".apk";
				File targetFile = new File(path,apkFileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionmodify?vid="
							+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error2";
				}
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
						 +"&aid="+appVersion.getAppId()
						 +"&error=error3";
			}
			appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
			appVersion.setModifyDate(new Date());
			appVersion.setApkFileName(apkFileName);
			appVersion.setDownloadLink(downloadLink);
			appVersion.setApkLocPath(apkLocPath);
			try {
				if(appVersionService.modify(appVersion)){
					return "redirect:/dev/flatform/app/list";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "developer/appversionmodify";
	}
	/**
	 * 使用rest风格查看app基础信息 以及对应的版本信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
	public String appView(@PathVariable String id,Model model){
		AppInfo appInfo = null;
		List<AppVersion> appVersionList = null;
		try {
			//根据id查询出app基础信息
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
			//根据id查询出app基础信息所对应的版本信息
			appVersionList = appVersionService.getAppVersionListByAppId(Integer.parseInt(id));
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appInfo", appInfo);
		return "developer/appinfoview";
	}
	/**
	 * 根据id异步删除app基础信息 以及app版本信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delapp.json",method=RequestMethod.GET)
	@ResponseBody
	public Object deleteAppInfoAndAppVersionById(@RequestParam("id") String id){
		Map<String,String> resultMap = new HashMap<String,String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("delResult", "notexist");
		}else{
			try {
				if(appInfoService.deleteAppInfoAndAppVersionById(Integer.parseInt(id))){
					resultMap.put("delResult", "true");
				}else{
					resultMap.put("delResult", "false");
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * 根据appId 修改App状态 可以有上架以及下架两种状态
	 * @param appId
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/{appId}/sale.json",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appId,HttpSession session){
		Integer appIdInteger = 0;
		Map<String,String> resultMap = new HashMap<String,String>();
		logger.info("appId==>"+appId);
		try {
			appIdInteger = Integer.parseInt(appId);
		} catch (Exception e) {
			appIdInteger = 0;
		}
		//放置默认值 errorCode
		resultMap.put("errorCode", "0");
		resultMap.put("appId", appId);
		if(appIdInteger > 0){
			try {
				DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(devUser.getId());
				if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
					resultMap.put("resultMsg", "success");
				}else{
					resultMap.put("resultMsg", "failed");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else{
			resultMap.put("errorCode","param000001");
		}
		return resultMap;
	}
	
}
