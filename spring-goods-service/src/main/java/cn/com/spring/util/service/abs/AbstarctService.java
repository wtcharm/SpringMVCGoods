package cn.com.spring.util.service.abs;

public abstract class AbstarctService {
	/**
	 * 如何返回true 表示不进行模糊查询
	 * @param str 要模糊查询的字段名称
	 * @return	如何返回true 表示不进行模糊查询
	 */
	public boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true ;
		}
		return false ;
	}
}
