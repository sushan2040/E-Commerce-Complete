package com.example.ecommerce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.example.ecommerce.configuration.beans.PaginationResponse;
import com.example.ecommerce.configuration.config.RedisKey;
import com.example.ecommerce.usersrepo.BrandDaoImpl;

@Component
 public class GlobalFunctionalExecution {

	public static void setRedisDataAll(Executor taskExecutor,RedisTemplate<String,Object> redisTemplate,
			List<?> list,String redisKey) {
		ThreadPoolTaskExecutor executor=(ThreadPoolTaskExecutor)taskExecutor;
        
        Runnable refreshAllBrands=()->{
        	RedisUtils.refreshRedisDataAll(redisKey,list,redisTemplate);	
        };
        List<Runnable> runnableTasks=new ArrayList<Runnable>();
        runnableTasks.add(refreshAllBrands);
        List<Future> future=new ArrayList<Future>();
        future.add(executor.submit(refreshAllBrands));
        for(Runnable task:runnableTasks) {
        	future.add(executor.submit(task));
        }
        
        for(Future executableTask:future) {
        	try {
				executableTask.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
}
