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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.*;

@Controller
public class WelcomeController {

	@RequestMapping("/")
	public String index(Model model) {
		
		ArrayList<String> testlist = new ArrayList<String>();
		
		testlist.add("123456"); 
		testlist.add("567890");
		testlist.add("444555");
		testlist.add("222333");
		
		TanManagment tanMan = new TanManagment(testlist);
		
		tanMan.generateNewTan("777888");
		
		tanMan.generateNewTan("123456");
		
		
		model.addAttribute("welcome", tanMan.getAllTans());
		
		return "welcome";
	}
}
