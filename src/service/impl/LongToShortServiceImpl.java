package service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import dao.LongToShortDao;
import domain.LongToShort;
import service.LongToShortSerivce;

@Transactional
public class LongToShortServiceImpl implements LongToShortSerivce{

	private LongToShortDao ltsDao;

	public LongToShortDao getLtsDao() {
		return ltsDao;
	}

	public void setLtsDao(LongToShortDao ltsDao) {
		this.ltsDao = ltsDao;
	}

	@Override
	public void insertLongAndShort(LongToShort lts) {
		ltsDao.insertLongAndShort(lts);
	}

	@Override
	public void updateShortURL(LongToShort lts) {
		ltsDao.updateShortURL(lts);
	}
	
	@Override
	public List<LongToShort> findAllByLong(String long_url) {
		return ltsDao.findAllByLong(long_url);
	}

	@Override
	public List<LongToShort> findLongByShort(String short_url) {
		return ltsDao.findLongByShort(short_url);
	}

}
