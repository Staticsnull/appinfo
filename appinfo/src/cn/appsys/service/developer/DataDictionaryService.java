package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

/**
 * 数据字典业务层
 * @author Administrator
 *
 */
public interface DataDictionaryService {
	List<DataDictionary> getDataDictionaryList(String typeCode) throws Exception;

}
