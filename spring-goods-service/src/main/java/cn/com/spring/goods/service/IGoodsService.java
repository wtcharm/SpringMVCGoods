package cn.com.spring.goods.service;

import java.util.Map;
import java.util.Set;

import cn.com.spring.goods.api.vo.Goods;

public interface IGoodsService {
	/**
	 * 实现数据增加时页面的显示
	 * @return
	 */
	public Map<String, Object> preAdd();
	/**
	 * 实现数据的增加
	 * @param vo 要增加的数据
	 * @param tids 增加数据的标签
	 * @return 增加成功返回true否则返回false
	 */
	public boolean  add(Goods vo,Set<Long> tids);
	/**
	 * 批量的删除商品数据
	 * @param gid 要删除的商品的编号
	 * @return
	 */
	public boolean remove(Set<Long>gid);
	/**
	 * 实现数据的更新操作
	 * @param vo 要更新的对象
	 * @param tids 更新对象的标签
	 * @return
	 */
	public boolean update(Goods vo,Set<Long> tids);
	/**
	 * 实现分页查询和模糊查询
	 * @param column  模糊字段名称
	 * @param keyWord 
	 * @param currentPage  当前页
	 * @param lineSize 每页显示的数据
	 * @return
	 */
	public Map<String,Object> listAll(String column, String keyWord, Long currentPage, Integer lineSize);
	/**
	 * 数据修改回显操作
	 * @param gid 要回显的编号
	 * @return
	 */
	public Map<String, Object> preEdit(Long gid);

}
