package cn.com.spring.goods.service.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.spring.goods.service.IGoodsService;
import junit.framework.TestCase;

@ContextConfiguration(locations= {"classpath:spring/spring-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)	//	设置要使用的测试工具类
public class GoodsServiceTest extends TestCase {
	@Resource
	private IGoodsService goodsService;
	@Test
	public void testPreAdd() throws Exception{
		System.out.println(this.goodsService.preAdd());
	}
}
