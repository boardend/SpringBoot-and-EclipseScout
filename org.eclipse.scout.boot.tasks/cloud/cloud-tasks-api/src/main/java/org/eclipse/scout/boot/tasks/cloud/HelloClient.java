package org.eclipse.scout.boot.tasks.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tasks-service")
public interface HelloClient {
	
	@RequestMapping(method = RequestMethod.GET, value = "/hello/hello")
	String hello();

}