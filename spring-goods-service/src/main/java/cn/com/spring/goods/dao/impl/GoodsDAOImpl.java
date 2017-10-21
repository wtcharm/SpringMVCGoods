package cn.com.spring.goods.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.RowSetWriter;

import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.com.spring.goods.api.vo.Goods;
import cn.com.spring.goods.dao.IGoodsDAO;
import cn.com.spring.util.dao.IBaseDAO;
import cn.com.spring.util.dao.abs.AbstractDAO;
@Repository	//DAO层注入
public class GoodsDAOImpl extends AbstractDAO implements IGoodsDAO ,RowMapper<Goods>{

	@Override
	public boolean doCreate(Goods vo) {
		KeyHolder keyHolder = new GeneratedKeyHolder();	//获取增长后的ID数据
		String sql = "INSERT INTO goods(name,price,photo,dflag,iid) VALUES(?,?,?,?,?)";
	   int len = this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, vo.getName());
				 ps.setDouble(2, vo.getPrice());
				 ps.setString(3, vo.getPhoto());
				 ps.setInt(4, vo.getDflag());
				 ps.setLong(5, vo.getIid());
				return ps;
			}
		},keyHolder);//实现更新处理，随后获取对象内容
	   vo.setGid(keyHolder.getKey().longValue());//将增长后的id保存在VO类中
		return len>0;
	}
	@Override
	public boolean doCreateGoodsAndTag(Long gid, Set<Long> tids) {
		String sql ="INSERT INTO goods_tag(gid,tid) VALUES(?,?)";
		 int[][] len = this.jdbcTemplate.batchUpdate(sql, tids, tids.size(), new ParameterizedPreparedStatementSetter<Long>() {

			@Override
			public void setValues(PreparedStatement ps, Long argument) throws SQLException {
				ps.setLong(1, gid);
				ps.setLong(2, argument);

			}
		});
		for(int x =0;x<len.length;x++) {
			for (int i = 0; i < len.length; i++) {
				if(len[x][i]<0) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public boolean doEdit(Goods vo) {
		String sql = "UPDATE goods SET name=?,price=?,photo=?,iid=? WHERE  gid=? ";
		return super.jdbcTemplate.update(sql,vo.getName(),vo.getPrice(),vo.getPhoto(),vo.getIid(),vo.getGid())>0;
	}
	@Override
	public boolean doRemoveGoodsTag(Long gid) {
		String sql ="DELETE FROM goods_tag WHERE gid=?";
		
		return super.jdbcTemplate.update(sql,gid)>0;
	}
	
	@Override
	public boolean doEditDflag(Set<Long> gid, Integer dflag) {
		String sql = "UPDATE goods SET dflag=? WHERE gid=?";
		 int[][] len = super.jdbcTemplate.batchUpdate(sql,gid,gid.size(),new ParameterizedPreparedStatementSetter<Long>() {
			@Override
			public void setValues(PreparedStatement ps, Long argument) throws SQLException {
				ps.setInt(1, dflag);
				ps.setLong(2, argument);
			}
		});
		for (int i = 0; i < len.length; i++) {
			for (int j = 0; j < len[i].length; j++) {
				if(len[i][j]<=0) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public boolean doRemove(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean doRemove(Set<Long> ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Goods findById(Long id) {
		String sql= "SELECT gid,name,price,photo,dflag,iid FROM goods WHERE dflag=0 AND gid=?";
		return super.jdbcTemplate.queryForObject(sql, new Object[] {id},this);
	}
	@Override
	public List<Long> findGoodsTag(Long gid) {
		String sql = "SELECT tid FROM goods_tag WHERE gid=?";
		return super.jdbcTemplate.queryForList(sql, new Object[] {gid},Long.class);
	}

	@Override
	public List<Goods> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Goods> findAll(Long currentPage, Integer lineSize) {
		String sql = "SELECT gid,name,price,photo,dflag,iid FROM goods WHERE dflag=0  LIMIT ?,?";
		return super.jdbcTemplate.query(sql, new Object[] {(currentPage-1)*lineSize, lineSize},this);
	}

	@Override
	public List<Goods> findSplit(String column, String keyWord, Long currentPage, Integer lineSize) {
		String sql = "SELECT gid,name,price,photo,dflag,iid FROM goods WHERE dflag=0 AND  "+column+" LIKE ? LIMIT ?,? ";
		return super.jdbcTemplate.query(sql, new Object[]{"%"+keyWord+"%",(currentPage-1)*lineSize,lineSize},this);
	}

	@Override
	public Long getAllCount() {
		String sql = "SELECT COUNT(*) FROM goods WHERE dflag=0";
		return super.jdbcTemplate.queryForObject(sql, Long.class);
	}

	@Override
	public Long getSplitCount(String column, String keyWord) {
		String sql = "SELECT COUNT(*) FROM goods WHERE  dflag=0 AND "+column+" LIKE ?"; 
		return super.jdbcTemplate.queryForObject(sql, new Object[] {"%"+keyWord+"%"},Long.class);
	}

	@Override
	public Goods mapRow(ResultSet rs, int rowNum) throws SQLException {
		Goods vo = new Goods();
		vo.setGid(rs.getLong(1));
		vo.setName(rs.getString(2));
		vo.setPrice(rs.getDouble(3));
		vo.setPhoto(rs.getString(4));
		vo.setDflag(rs.getInt(5));
		vo.setIid(rs.getLong(6));
		return vo;
	}
	
}
