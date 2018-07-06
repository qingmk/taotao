package com.taotao.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

/**
 * 通用service
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class BaseService<T extends BasePojo> {

	/**
	 * 1.queryByid 2queryall 3queryListBywhere 4queryPageListbywhere 5save 6update
	 * 7deleteByid 8deleteByids 9deletebywhere
	 * 
	 */
	
	/**
	 * 定义抽象类获取marrper<T>
	 * @return
	 */
	/*public abstract Mapper<T> getMapper();*/
	@Autowired
	private Mapper<T> mapper;
	
	/**
	 * 根绝id查询数据
	 * 
	 * @param id
	 * @return
	 */
	public T queryByid(Long id) {

		return mapper.selectByPrimaryKey(id);

	}

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	public List<T> queryAll() {

		return mapper.select(null);

	}

	/**
	 * 根据条件查询一条数据
	 * 
	 * @param record
	 * @return
	 */

	public T queryOne(T record) {

		return mapper.selectOne(record);

	}

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param record
	 * @return
	 */

	public List<T> queryListBYWhere(T record) {

		return mapper.select(record);

	}

	/**
	 * 根据条件查询分页列表
	 * 
	 * @param record
	 * @return
	 */

	public PageInfo<T> querPageyListBYWhere(T record, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<T> list = mapper.select(record);
		return new PageInfo<T>(list);

	}

	/**
	 * 保存数据
	 * 
	 * @param t
	 * @return
	 */

	public Integer save(T t) {
		t.setCreated(new Date());
		t.setUpdated(t.getCreated());
		return mapper.insert(t);

	}

	/**
	 * 选择部位NULL数据保存
	 * 
	 * @param t
	 * @return
	 */

	public Integer saveSelect(T t) {
		t.setCreated(new Date());
		t.setUpdated(t.getCreated());
		return mapper.insertSelective(t);

	}

	/**
	 * 更新数据
	 * 
	 * @param t
	 * @return
	 */

	public Integer update(T t) {
		t.setUpdated(new Date());
		return mapper.updateByPrimaryKey(t);

	}

	/**
	 * 选择不为NULL数据更新
	 * 
	 * @param t
	 * @return
	 */

	public Integer updateSelect(T t) {

		t.setUpdated(new Date());
		return mapper.updateByPrimaryKeySelective(t);

	}

	/**
	 * 根绝id删除数据
	 * 
	 * @param id
	 * @return
	 */
	public Integer deleteByid(Long id) {

		return mapper.deleteByPrimaryKey(id);

	}

	/**
	 * 根据id批量删除数据
	 * 
	 * @param clazz
	 * @param property
	 * @param values
	 * @return
	 */
	public Integer deleteByids(Class<T> clazz, String property, List<Object> values) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, values);
		return mapper.deleteByExample(example);
	}

	/**
	 * 根据条件删除数据
	 * 
	 * @param record
	 * @return
	 */

	public Integer deleteBYWhere(T record) {

		return mapper.delete(record);

	}

}
