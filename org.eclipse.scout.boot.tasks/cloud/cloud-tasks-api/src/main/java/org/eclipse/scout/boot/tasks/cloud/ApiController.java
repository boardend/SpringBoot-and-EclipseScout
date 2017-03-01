package org.eclipse.scout.boot.tasks.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private HelloClient helloClient;

	@RequestMapping("/hello")
	@HystrixCommand
	String hello() {
		return helloClient.hello();
	}

}
