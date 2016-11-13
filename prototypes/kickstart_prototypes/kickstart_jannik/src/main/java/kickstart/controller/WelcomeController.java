/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kickstart.controller;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.*;

@Controller
public class WelcomeController {

	@RequestMapping("/")
	public String index(Model model) {
		
		ArrayList<String> test = new ArrayList();
		
		test.add("123456");
		test.add("567890");
		test.add("454667");
		test.add("349578");
		
		TanManagment tanMan = new TanManagment(test);
		
		Tan testtan = tanMan.getTan("123456");
		
		String telenum = tanMan.getTelephoneNumber(testtan);
		//System.out.println(telenum);
		
		//System.out.println(testtan.getStatus());
		
		tanMan.invalidateTan(testtan);
		
		//System.out.println(testtan.getStatus());
		
		telenum = tanMan.getTelephoneNumber(testtan);
		//System.out.println(telenum);
		
		model.addAttribute("welcome", tanMan.getAllTans());
		
		return "welcome";
	}
}
