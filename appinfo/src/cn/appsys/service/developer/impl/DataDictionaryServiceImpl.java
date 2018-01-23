package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.datadictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.developer.DataDictionaryService;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	@Override
	public List<DataDictionary> getDataDictionaryList(String typeCode) throws Exception {
		return dataDictionaryMapper.getDataDictionaryList(typeCode);
	}

}
