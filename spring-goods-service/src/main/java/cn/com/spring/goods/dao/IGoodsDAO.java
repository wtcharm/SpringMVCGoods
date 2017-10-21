package cn.com.spring.goods.dao;

import java.util.List;
import java.util.Set;

import cn.com.spring.goods.api.vo.Goods;
import cn.com.spring.util.dao.IBaseDAO;

public interface IGoodsDAO  extends IBaseDAO<Long, Goods>{
	/**
	 * 实现商品标签关联关系的保存
	 * @param gid 商品编号
	 * @param tids  商品标签
	 * @return 保存成功返回true否则返回false
	 */
	public boolean doCreateGoodsAndTag(Long gid,Set<Long> tids);
	/**
	 * 根据商品编号查询出商品的标签信息
	 * @param gid 商品编号
	 * @return
	 */
	public List<Long> findGoodsTag(Long gid);
	/**
	 * 根据商品编号 删除商品的标签
	 * @param gid 要删除的商品标签
	 * @return
	 */
	public boolean doRemoveGoodsTag(Long gid);
	/**
	 * 批量删除数据，这里采用的是物理删除，将dflag的值改为1就可以了
	 * @param gid 要删除的商品编号
	 * @param dflag 0为未删除的，1为删除的
	 * @return
	 */
	public boolean doEditDflag(Set<Long> gid, Integer dflag);
	
}
