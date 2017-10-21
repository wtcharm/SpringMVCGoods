package cn.com.spring.goods.action;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.com.spring.goods.api.vo.Goods;
import cn.com.spring.goods.api.vo.Item;
import cn.com.spring.goods.service.IGoodsService;
import cn.com.spring.goods.util.action.abc.AbstractAction;
import cn.com.util.web.SplitPageUtil;
@Controller
@RequestMapping("/pages/back/admin/goods/*")
public class GoodsAction extends AbstractAction{
	private static final String  TIELTE = "商品";
	@Resource
	private IGoodsService goodsService;
	@RequestMapping("goods_add_pre")
	public ModelAndView addPre() {	//添加页面的显示操作
		ModelAndView mav = new ModelAndView(super.getPage("goods.add.page"));
		mav.addAllObjects(goodsService.preAdd());
		return mav;
	}
	@RequestMapping("goods_add") 
	public ModelAndView add(Goods goods,String tid[],MultipartFile pic) throws Exception {
		ModelAndView mav = new ModelAndView(super.getPage("forward.page"));
		if(pic==null&& pic.isEmpty()) {
			goods.setPhoto("nophoto.jpg");//使用默认的图片
		}else {//创建新图片的名称
			goods.setPhoto(super.getNewFileName(pic));
		}
		if(goodsService.add(goods, super.stringToLong(tid))) {	//增加数据
			if(!pic.isEmpty()) {//现在有上传的图片
				String filePath = ContextLoader.getCurrentWebApplicationContext().getServletContext()
						.getRealPath("/WEB-INF/upload/")+goods.getPhoto();
				System.out.println("图片地址+" + filePath);
				pic.transferTo(new File(filePath)); 
			}
			super.setMsgAndUrl(mav,"goods.list.action", "vo.add.success", TIELTE);
		}else {
			super.setMsgAndUrl(mav,"goods.add.action", "vo.add.failure", TIELTE);
		}
		return mav;
	}
	@RequestMapping("goods_list")
	public @ResponseBody ModelAndView list() {
		ModelAndView mav = new  ModelAndView(super.getPage("goods.list.page"));
		SplitPageUtil spu = new SplitPageUtil("商品名称:name", super.getPage("goods.list.action"));
		Map<String, Object> map = this.goodsService.listAll(spu.getColumn(), spu.getKeyWord(), spu.getCurrentPage(), spu.getLineSize());
		mav.addAllObjects(map);
		Map<Long, String> iterMap = new HashMap<Long,String>();
		//建立级联关系
		Iterator<Item> iter = ((List<Item>)map.get("allItems")).iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			iterMap.put(item.getIid(), item.getTitle());
		}
		mav.addObject("allItems", iterMap);
		return mav;
	}
	@RequestMapping("goods_edit_pre")
	public ModelAndView editPre(long gid) {
	 ModelAndView mav = new ModelAndView(super.getPage("goods.edit.page"));
	 mav.addAllObjects(this.goodsService.preEdit(gid));
		return mav;
	}
	@RequestMapping("goods_edit")
	public ModelAndView edit(Goods goods,String tid[],MultipartFile pic) throws Exception {
		 ModelAndView mav = new ModelAndView(super.getPage("forward.page"));
		 if(!pic.isEmpty()) {	//，有新文件上传
			 if("nophoto,jpg".equals(goods.getPhoto())) {
				 goods.setPhoto(super.getNewFileName(pic));
			 }
		 }
		 if(this.goodsService.update(goods, super.stringToLong(tid))) {
			 if(!pic.isEmpty()) {//现在有文件上传
				 String filePath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/WEB-INF/upload")+goods.getPhoto();
				 pic.transferTo(new File(filePath));
			 }
			 super.setMsgAndUrl(mav, "goods.list.action", "vo.edit.success", TIELTE);
		 }else {
			 super.setMsgAndUrl(mav, "goods.list.action", "vo.edit.failure", TIELTE);
		 }
		return mav;
		
	}
	@RequestMapping("goods_delete")
	public ModelAndView delete(String ids) {

		 ModelAndView mav = new ModelAndView(super.getPage("forward.page"));
		 if(this.goodsService.remove(super.stringToLong(ids))) {
			 super.setMsgAndUrl(mav, "goods.list.action", "vo.delete.success",TIELTE);
		 }else {
			 super.setMsgAndUrl(mav, "goods.list.action", "vo.delete.failure",TIELTE);
		 }
		 
		 return mav;
	}
}	