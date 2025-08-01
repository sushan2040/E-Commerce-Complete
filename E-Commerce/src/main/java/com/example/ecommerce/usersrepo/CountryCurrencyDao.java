package com.example.ecommerce.usersrepo;

import java.util.List;

import com.example.ecommerce.configuration.beans.CountryCurrencyBean;

public interface CountryCurrencyDao {

	Long saveCountryCurrencyMaster(CountryCurrencyBean bean);

	List<CountryCurrencyBean> getAllCountryCurrencyPagination(int page, int per_page);

	Long deleteCountryCurrencyId(Integer countryCurrencyMasterId);

	CountryCurrencyBean getCountryCurrencyById(Integer countryCurrencyMasterId);

	Long checkCombination(Integer countryId);

}
