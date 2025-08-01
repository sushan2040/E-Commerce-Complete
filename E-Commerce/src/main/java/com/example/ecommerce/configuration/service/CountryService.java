package com.example.ecommerce.configuration.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.ecommerce.configuration.beans.CountryMasterBean;

public interface CountryService {

	public Long saveCountryMaster(CountryMasterBean bean);

	public List<CountryMasterBean> fetchAllCountries();

	public List<CountryMasterBean> getAllCountriesPagination(int page, int per_page);

	public CountryMasterBean getCountryById(Integer countryId);

	public Long deleteCountryId(Integer countryId);
}
