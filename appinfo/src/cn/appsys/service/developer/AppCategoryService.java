package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppCategory;

/**
 * 分类信息业务层
 * @author Administrator
 *
 */
public interface AppCategoryService {
	/**
	 * 根据父节点parentId获取相应的分类列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<AppCategory> getAppCategoryListByParentId(Integer parentId) throws Exception;
	
 
}
