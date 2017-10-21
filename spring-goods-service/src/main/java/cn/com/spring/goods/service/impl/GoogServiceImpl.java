package cn.com.spring.goods.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.spring.goods.api.vo.Goods;
import cn.com.spring.goods.dao.IGoodsDAO;
import cn.com.spring.goods.dao.IItemDAO;
import cn.com.spring.goods.dao.ITagDAO;
import cn.com.spring.goods.service.IGoodsService;
import cn.com.spring.util.service.abs.AbstarctService;
@Service
public class GoogServiceImpl extends AbstarctService implements IGoodsService {
	@Resource	//注入
	private IItemDAO itemDAO;
	@Resource	//注入
	private ITagDAO tagDAO;
	@Resource
	private IGoodsDAO goodsDAO;
	@Override
	public Map<String, Object> preAdd() {
		Map<String, Object> map = new  HashMap<String,Object>();
		map.put("allItems", this.itemDAO.findAll());
		map.put("allTags", this.tagDAO.findAll());
		return map;
	}


	@Override
	public Map<String, Object> listAll(String column, String keyWord, Long currentPage, Integer lineSize) {
		HashMap<String, Object> map = new  HashMap<String,Object>();
		map.put("allItems", this.itemDAO.findAll()) ;
		if(super.isEmpty(column) && super.isEmpty(keyWord)) {	//表示不进行模糊查询
			map.put("allGoods", this.goodsDAO.findAll(currentPage, lineSize));
			map.put("allRecorders", this.goodsDAO.getAllCount()); 
		}else {
			map.put("allGoods",this. goodsDAO.findSplit(column, keyWord, currentPage, lineSize));
			map.put("allRecorders", this.goodsDAO.getSplitCount(column, keyWord)); 
		}
		return map;
	}
	public boolean add(Goods vo,Set<Long> tids) {
		if(tids == null || tids.size()==0) {
			return false;
		}
		vo.setDflag(0);//现在没有删除
		if(this.goodsDAO.doCreate(vo)) {	//goods表中增加数据成功后,在中间表中插入数据
			return this.goodsDAO.doCreateGoodsAndTag(vo.getGid(), tids);
		}
		return false;
	}
	@Override
	public boolean update(Goods vo, Set<Long> tids) {
		if(tids == null || tids.size()==0) {
			return false;
		}
		vo.setDflag(0);//现在没有删除
		if(this.goodsDAO.doEdit(vo)) {	//goods表中增加数据成功后,在中间表中插入数据
			if(this.goodsDAO.doRemoveGoodsTag(vo.getGid())) {//先删除，删除成功之后在进行增加
				return this.goodsDAO.doCreateGoodsAndTag(vo.getGid(), tids);
			}
		}
		return false;
	}
	@Override
	public Map<String, Object> preEdit(Long gid) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("allItems",this.itemDAO.findAll());
		map.put("allTags",this.tagDAO.findAll());
		map.put("goods",this.goodsDAO.findById(gid));
		map.put("goodsTags", this.goodsDAO.findGoodsTag(gid));
		return map;
	}
	
	@Override
	public boolean remove(Set<Long> gid) {
		if(gid==null||"".equals(gid)) {
			return false;
		}

		return this.goodsDAO.doEditDflag(gid, 1);
	}
}
