package com.example.ecommerce.configuration.service;

import java.util.List;

import com.example.ecommerce.configuration.beans.CountryCurrencyBean;

public interface CountryCurrencyService {

	Long saveCountryCurrencyMaster(CountryCurrencyBean bean);

	List<CountryCurrencyBean> getAllCountryCurrencyPagination(int page, int per_page);

	Long deleteCountryCurrencyId(Integer countryCurrencyMasterId);

	CountryCurrencyBean getCountryCurrencyById(Integer countryCurrencyMasterId);

	Long checkCombination(Integer countryId);

}
