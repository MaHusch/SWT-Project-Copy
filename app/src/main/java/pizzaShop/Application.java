/*
 * Copyright 2014.2015 the original author or authors.
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
/* useless commit to create new commit*/

package pizzaShop;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@EnableScheduling
@EnableSalespoint
public class Application {
	
	
/* fancy comment */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


    @Configuration
    static class WebSecurityConfiguration extends SalespointSecurityConfiguration {


        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.csrf().disable();

            http.authorizeRequests().antMatchers("/**").permitAll().and().//
            formLogin().loginPage("/login").loginProcessingUrl("/login").successHandler(new AuthSuccessHandler()).and(). //
            logout().logoutUrl("/logout").logoutSuccessUrl("/");
        }
	}
}

/*http.authorizeRequests().antMatchers("/**").permitAll().and().
formLogin().loginProcessingUrl("/login").and().
successHandler(new AuthSuccessHandler()).and(). //
logout().logoutUrl("/logout").logoutSuccessUrl("/");*/
