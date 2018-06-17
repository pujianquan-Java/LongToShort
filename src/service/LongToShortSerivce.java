package service;

import java.util.List;

import domain.LongToShort;

public interface LongToShortSerivce {
	
	/**
	 * 将长链接、短链接、自定义存入数据库
	 * @param lts
	 */
	public void insertLongAndShort(LongToShort lts);
	
	/**
	 * 更新短链接
	 * @param short_url
	 */
	public void updateShortURL(LongToShort lts);
	
	/**
	 * 根据长链接查询所有
	 * @param long_url
	 * @return
	 */
	public List<LongToShort> findAllByLong(String long_url);
	
	/**
	 * 根据短链接查询长链接
	 * @param short_url
	 * @return
	 */
	public List<LongToShort> findLongByShort(String short_url);
	
}
