package dao.impl;

import java.util.List;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import dao.LongToShortDao;
import domain.LongToShort;

public class LongToShortDaoImpl extends HibernateDaoSupport implements LongToShortDao{

	@Override
	public void insertLongAndShort(LongToShort lts) {
		this.getHibernateTemplate().save(lts);
	}

	@Override
	public void updateShortURL(LongToShort lts) {
		this.getHibernateTemplate().update(lts);
	}
	
	@Override
	public List<LongToShort> findAllByLong(String long_url) {
		String hql = "from LongToShort where long_url = ?";
		
		return (List<LongToShort>) this.getHibernateTemplate().find(hql, long_url);
	}

	@Override
	public List<LongToShort> findLongByShort(String short_url) {
		String hql = "from LongToShort where short_url = ?";
		
		return (List<LongToShort>) this.getHibernateTemplate().find(hql, short_url);
	}

}
