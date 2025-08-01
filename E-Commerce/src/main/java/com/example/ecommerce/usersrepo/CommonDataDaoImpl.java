package com.example.ecommerce.usersrepo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.configuration.beans.CommonDataBean;
import com.example.ecommerce.configuration.masters.BridgeParameter;
import com.example.ecommerce.configuration.masters.CommonBridgedData;
import com.example.ecommerce.configuration.masters.CommonData;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.beans.ProductMasterBean;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CommonDataDaoImpl implements CommonDataDao{

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<BridgeParameter> getBridgeParameters() {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<BridgeParameter> parametersQuery=builder.createQuery(BridgeParameter.class);
		Root<BridgeParameter> root=parametersQuery.from(BridgeParameter.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		parametersQuery.where(where);
		parametersQuery.select(builder.construct(BridgeParameter.class,
				root.get("bridgeParameterId"),
				root.get("bridgeParameterName")
				));
		Query<BridgeParameter> paraQuery=session.createQuery(parametersQuery);
		paraQuery.setParameter("deleted",Constants.NOT_DELETED);
		paraQuery.setParameter("status",Constants.STATUS_ACTIVE);
		return paraQuery.getResultList();
	}

	@Override
	public List<CommonDataBean> getCommonDatas(Integer businessId) {
		Session session=sessionFactory.getCurrentSession();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<CommonDataBean> parametersQuery=builder.createQuery(CommonDataBean.class);
		Root<CommonData> root=parametersQuery.from(CommonData.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		parametersQuery.where(where);
		parametersQuery.select(builder.construct(CommonDataBean.class,
				root.get("commonDataId"),
				root.get("commonDataName")
				));
		Query<CommonDataBean> paraQuery=session.createQuery(parametersQuery);
		paraQuery.setParameter("deleted",Constants.NOT_DELETED);
		paraQuery.setParameter("status",Constants.STATUS_ACTIVE);
		paraQuery.setParameter("businessId",businessId);
		return paraQuery.getResultList();
	}

	@Override
	public Long saveCommonData(CommonDataBean bean) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CommonData data;
		if(bean.getCommonDataId()!=null) {
			data=session.get(CommonData.class,bean.getCommonDataId());
			data.setCommonDataDesc(data.getCommonDataDesc());
			data.setCommonDataName(data.getCommonDataName());
		}else {
			data=new CommonData();
			data.setCommonDataDesc(bean.getCommonDataDesc());
			data.setCommonDataName(bean.getCommonDataName());
		}
		data.setBusinessId(bean.getBusinessId());
		data.setStatus(Constants.STATUS_ACTIVE);
		data.setDeleted(Constants.NOT_DELETED);
		
		if(bean.getCommonDataId()!=null) {
		data.setCommonDataId(bean.getCommonDataId());
		}
		data=session.merge(data);
		
		CommonBridgedData bridgedData=new CommonBridgedData();
		bridgedData.setCommonDataId(data.getCommonDataId());
		if(bean.getBridgeParameterId()!=null) {
		BridgeParameter bridgeParameter=new BridgeParameter();
		bridgeParameter.setBridgeParameterId(bean.getBridgeParameterId());
		bridgedData.setBridgeParameterId(bridgeParameter);
		}
		if(bean.getParentCommonDataId()!=null) {
		CommonData commonData=new CommonData();
		commonData.setCommonDataId(bean.getParentCommonDataId());
		bridgedData.setParentCommonDataId(commonData);
		}
		bridgedData.setBusinessId(bean.getBusinessId());
		bridgedData.setDeleted(Constants.NOT_DELETED);
		bridgedData.setStatus(Constants.STATUS_ACTIVE);
		session.merge(bridgedData);
		session.getTransaction().commit();
		return 0L;
	}

	@Override
	public List<CommonDataBean> getAllCommonDataPagination(int page, int per_page,Integer businessId) {
		Session session = null;
		Transaction transaction = null;
		try {
		    session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
		    CriteriaBuilder builder = session.getCriteriaBuilder();
		    CriteriaQuery<CommonDataBean> commonDataQuery = builder.createQuery(CommonDataBean.class);
		    Root<CommonData> root = commonDataQuery.from(CommonData.class);
		    Root<CommonBridgedData> rootBridged=commonDataQuery.from(CommonBridgedData.class);
		    // Perform LEFT JOIN with the same entity (self-join)
		    Join<CommonData, CommonData> parentJoin = rootBridged.join("parentCommonDataId", JoinType.LEFT);
		    
		 // Perform LEFT JOIN with the same entity (self-join)
		    Join<BridgeParameter, CommonData> bridgeJoin = rootBridged.join("bridgeParameterId", JoinType.LEFT);

		    // Define the where clause (for filtering)
		    Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
		    where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
		    where=builder.and(where,builder.equal(rootBridged.get("deleted"),builder.parameter(String.class,"deleted")));
		    where=builder.and(where,builder.equal(rootBridged.get("status"),builder.parameter(String.class,"status")));
		    where=builder.and(where,builder.equal(rootBridged.get("commonDataId"),root.get("commonDataId")));
		    where=builder.and(where,builder.equal(root.get("businessId"),rootBridged.get("businessId")));
		    where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		    commonDataQuery.where(where);

		    // Select the relevant fields (including the parent name from the joined entity)
		    commonDataQuery.select(builder.construct(
		        CommonDataBean.class,
		        rootBridged.get("commonDataId"),
		        root.get("commonDataName"),
		        root.get("commonDataDesc"),
		        parentJoin.get("commonDataName"), // Get the parent's name from the joined entity
		        bridgeJoin.get("bridgeParameterName"),
		        root.get("status")
		    ));

		    // Create the query and set parameters
		    Query<CommonDataBean> countryquery = session.createQuery(commonDataQuery);
		    countryquery.setParameter("deleted", Constants.NOT_DELETED);
		    countryquery.setParameter("status", Constants.STATUS_ACTIVE);
		    countryquery.setParameter("businessId",businessId);
		    
		    // Handle pagination
		    if ((page - 1) >= 0) {
		        countryquery.setFirstResult((page - 1) * per_page);
		    } else {
		        countryquery.setFirstResult(0);
		    }
		    countryquery.setMaxResults(per_page);

		    // Execute the query and get the result list
		    List<CommonDataBean> commonDataList = countryquery.getResultList();
		 // Commit the transaction
		    transaction.commit();
		    // If records found, set the total record count
		    if (!commonDataList.isEmpty()) {
		        int totalRecords = getTotalCommonDataCount(page, per_page);
		        commonDataList.get(0).setTotalRecords(totalRecords);
		    }

		    

		    return commonDataList;
		} catch (Exception e) {
		    // Rollback the transaction in case of error
		    if (transaction != null) {
		        transaction.rollback();
		    }
		    e.printStackTrace();
		    return null;
		} finally {
		    // Close the session
		    if (session != null) {
		        session.close();
		    }
		}

	}

	private int getTotalCommonDataCount(int page, int per_page) {
		Session session = null;
        Transaction transaction = null;
        Long commonDataCount=0L;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countryQuery = builder.createQuery(Long.class);
            Root<CommonData> root = countryQuery.from(CommonData.class);
            Predicate where = builder.equal(root.get("deleted"), builder.parameter(String.class, "deleted"));
            where = builder.and(where, builder.equal(root.get("status"), builder.parameter(String.class, "status")));
            countryQuery.where(where);
            countryQuery.select(builder.count(root));
            Query<Long> countryquery = session.createQuery(countryQuery);
            countryquery.setParameter("deleted", Constants.NOT_DELETED);
            countryquery.setParameter("status", Constants.STATUS_ACTIVE);
            commonDataCount= countryquery.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if error occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
        return commonDataCount.intValue();
	}

	@Override
	public Long deleteById(Integer id) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CommonBridgedData data=session.get(CommonBridgedData.class, id);
		data.setStatus(Constants.STATUS_INACTIVE);
		data.setDeleted(Constants.DELETED);
		data=session.merge(data);
		session.getTransaction().commit();
		return data.getCommonDataId().longValue();
	}

	@Override
	public CommonDataBean getCommonDataById(Integer id) {
		Session session=sessionFactory.openSession();
		session.getTransaction();
		CommonData commonData=session.get(CommonData.class, id);
		CommonDataBean bean=new CommonDataBean();
		BeanUtils.copyProperties(commonData, bean);
		return bean;
	}

	@Override
	public List<CommonDataBean> fetchCommonDataSuggestions(String param,Integer businessId) {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<CommonDataBean> commonDataQuery=builder.createQuery(CommonDataBean.class);
		Root<CommonData> root=commonDataQuery.from(CommonData.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.like(builder.lower(root.get("commonDataName")),builder.parameter(String.class,"commonDataName")));
		where=builder.and(where,builder.equal(root.get("businessId"),builder.parameter(Integer.class,"businessId")));
		commonDataQuery.where(where);
		commonDataQuery.select(builder.construct(CommonDataBean.class,
				root.get("commonDataId"),
				root.get("commonDataName"),
				root.get("commonDataDesc")
				));
		Query<CommonDataBean> commonquery=session.createQuery(commonDataQuery);
		commonquery.setParameter("deleted",Constants.NOT_DELETED);
		commonquery.setParameter("status",Constants.STATUS_ACTIVE);
		commonquery.setParameter("commonDataName",param.toLowerCase()+"%");
		commonquery.setParameter("businessId",businessId);
		List<CommonDataBean> commonDataList=commonquery.getResultList();
		session.getTransaction().commit();
		return commonDataList;
	}

	@Override
	public List<CommonDataBean> fetchProductCategoryList() {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<CommonDataBean> productQuery=builder.createQuery(CommonDataBean.class);
		Root<CommonData> root=productQuery.from(CommonData.class);
		Root<CommonData> rootCurrent=productQuery.from(CommonData.class);
		Root<CommonBridgedData> rootBridged=productQuery.from(CommonBridgedData.class);
		Predicate where=builder.equal(root.get("deleted"),builder.parameter(String.class,"deleted"));
		where=builder.and(where,builder.equal(root.get("status"),builder.parameter(String.class,"status")));
		where=builder.and(where,builder.equal(rootBridged.get("parentCommonDataId").get("commonDataId"),root.get("commonDataId")));
		where=builder.and(where,builder.equal(rootBridged.get("commonDataId"),rootCurrent.get("commonDataId")));
		where=builder.and(where,builder.equal(rootBridged.get("bridgeParameterId").get("bridgeParameterName"),builder.parameter(String.class,"bridgeParameterName")));
		where=builder.and(where,builder.equal(root.get("commonDataName"),builder.parameter(String.class,"commonDataName")));
		productQuery.where(where);
		productQuery.select(builder.construct(CommonDataBean.class, 
				rootCurrent.get("commonDataId"),
				rootCurrent.get("commonDataName"),
				rootCurrent.get("commonDataDesc")
				));
		Query<CommonDataBean> commonQuery=session.createQuery(productQuery);
		commonQuery.setParameter("deleted",Constants.NOT_DELETED);
		commonQuery.setParameter("status",Constants.STATUS_ACTIVE);
		commonQuery.setParameter("bridgeParameterName",Constants.HAS_PARENT);
		commonQuery.setParameter("commonDataName",Constants.PRODUCT_CATEGORY);
		return commonQuery.getResultList();
	}
	
	
}
