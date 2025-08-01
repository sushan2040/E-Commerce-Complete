package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CountryCurrencyBean;
import com.example.ecommerce.usersrepo.CountryCurrencyDao;

import jakarta.transaction.Transactional;

@Service
public class CountryCurrencyServiceImpl implements CountryCurrencyService{

	@Autowired
	CountryCurrencyDao countryCurrencyDao;

	@Override
	@Transactional
	public Long saveCountryCurrencyMaster(CountryCurrencyBean bean) {
		return countryCurrencyDao.saveCountryCurrencyMaster(bean);
	}

	@Override
	public List<CountryCurrencyBean> getAllCountryCurrencyPagination(int page, int per_page) {
		return countryCurrencyDao.getAllCountryCurrencyPagination(page,per_page);
	}

	@Override
	@Transactional
	public Long deleteCountryCurrencyId(Integer countryCurrencyMasterId) {
		return countryCurrencyDao.deleteCountryCurrencyId(countryCurrencyMasterId);
	}

	@Override
	public CountryCurrencyBean getCountryCurrencyById(Integer countryCurrencyMasterId) {
		return countryCurrencyDao.getCountryCurrencyById(countryCurrencyMasterId);
	}

	@Override
	public Long checkCombination(Integer countryId) {
		return countryCurrencyDao.checkCombination(countryId);
	}
}
