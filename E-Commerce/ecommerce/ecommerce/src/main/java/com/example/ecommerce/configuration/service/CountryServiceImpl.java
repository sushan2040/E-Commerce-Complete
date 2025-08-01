package com.example.ecommerce.configuration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.beans.CountryMasterBean;
import com.example.ecommerce.usersrepo.CountryDao;

import jakarta.transaction.Transactional;

@Service
public class CountryServiceImpl implements CountryService {
	
	@Autowired
	CountryDao countryDao;

	@Override
	@Transactional
	public Long saveCountryMaster(CountryMasterBean bean) {
		return countryDao.saveCountryMaster(bean);
	}

	@Override
	public List<CountryMasterBean> fetchAllCountries() {
		return countryDao.fetchAllCountries();
	}

	@Override
	public List<CountryMasterBean> getAllCountriesPagination(int page, int per_page) {
		return countryDao.getAllCountriesPagination(page,per_page);
	}

	@Override
	public CountryMasterBean getCountryById(Integer countryId) {
		return countryDao.getCountryById(countryId);
	}

	@Override
	@Transactional
	public Long deleteCountryId(Integer countryId) {
		return countryDao.deleteCountryId(countryId);
	}

	
}
