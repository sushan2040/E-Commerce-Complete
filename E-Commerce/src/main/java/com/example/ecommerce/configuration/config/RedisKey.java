package com.example.ecommerce.configuration.config;

public enum RedisKey {
    // Define constants for each key, add any needed parameters to construct the key dynamically
    SUBMODULE_ALL("submodule:all"),
    SUBMODULE_PAGINATION("REDIS_PAGE_SUBMODULE_%d_%d"),
    MENU_TYPE_ALL("menu:all"),
    MENU_TYPE_PAGINATION("REDIS_PAGE_MENU_TYPE_%d_%d"),
    COUNTRY_ALL("country:all"),
    COUNTRY_PAGINATION("REDIS_PAGE_COUNTRY_%d_%d"),
    DEPARTMENT_ALL("department:all"),
    DEPARTMENT_PAGINATION("REDIS_PAGE_DEPARTMENT_%d_%d"),
    BRANDS_ALL("brands:all"),
    BRANDS_PAGINATION("REDIS_PAGE_BRANDS_%d_%d"),
    LOCATION_LEVELS_ALL("location_levels:all"),
    LOCATION_LEVELS_PAGINATION("REDIS_PAGE_LOCATION_LEVELS_%d_%d"),
    LOCATION_LEVEL2_ALL("location_level2:all"),
    LOCATION_LEVEL2_PAGINATION("REDIS_PAGE_LOCATION_LEVEL2_%d_%d"),
    LOCATION_LEVEL3_ALL("location_level3:all"),
    LOCATION_LEVEL3_PAGINATION("REDIS_PAGE_LOCATION_LEVEL3_%d_%d"),
    LOCATION_LEVEL4_ALL("location_level4:all"),
    LOCATION_LEVEL4_PAGINATION("REDIS_PAGE_LOCATION_LEVEL4_%d_%d"),
    LOCATION_LEVEL5_ALL("location_level5:all"),
    LOCATION_LEVEL5_PAGINATION("REDIS_PAGE_LOCATION_LEVEL5_%d_%d"),
    REDIS_USER_ACCESS_PREFIX("USER_ACCESS_CACHE_%s"),
    EMPLOYEE_ALL("employee:all"),
    EMPLOYEE_PAGINATION("REDIS_PAGE_EMPLOYEE_%d_%d"),
    PRODUCT_ALL("product:all"),
    PRODUCT_PAGINATION("REDIS_PAGE_PRODUCT_%d_%d"),
	ROLES_ALL("roles:all"),
	ROLES_PAGINATION("REDIS_PAGE_LOCATION_ROLES_%d_%d_%d");
    private final String keyPattern;

	// Add other keys as needed
    RedisKey(String keyPattern) {
        this.keyPattern = keyPattern;
    };

    public String getKey(Object... params) {
        // This will allow you to format the key dynamically using parameters
        return String.format(keyPattern, params);
    }
}
