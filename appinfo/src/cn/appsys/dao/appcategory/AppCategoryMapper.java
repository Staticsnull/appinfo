package cn.appsys.dao.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

/**
 * 分类信息表的映射接口
 * @author Administrator 
 *
 */
public interface AppCategoryMapper {
	/**
	 * 根据分类id获取 子类信息
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<AppCategory> getAppCategoryListByParentId(@Param("parentId") Integer parentId) throws Exception;
	

}
