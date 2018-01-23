package cn.appsys.dao.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

/**
 * 数据字典映射接口
 * @author Administrator
 *
 */
public interface DataDictionaryMapper {
	/**
	 * 根据typeCode 查找数据字典信息
	 * @param typeCode
	 * @return
	 */
	List<DataDictionary> getDataDictionaryList(@Param("typeCode") String typeCode) throws Exception;
}
