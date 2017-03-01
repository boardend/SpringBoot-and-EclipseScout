package org.eclipse.scout.boot.tasks.cloud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/hello")
	String hello() {
		return "hello world";
	}

}
