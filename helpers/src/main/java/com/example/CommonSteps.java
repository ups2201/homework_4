/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.config.CitrusSpringConfig;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

@SuppressWarnings("SpringJavaAutowiringInspection")
@CucumberContextConfiguration
@ContextConfiguration(classes = CitrusSpringConfig.class)
public class CommonSteps {

    @CitrusResource
    private TestCaseRunner runner;

    @Autowired
    private HttpClient citrusHttpClient;

    @Given("^Todo list is empty$")
    public void empty_todos() {
        runner.given(http()
                .client(citrusHttpClient)
                .send()
                .delete("/api/todolist"));

        runner.then(http()
                .client(todoListClient)
                .receive()
                .response(HttpStatus.OK));
    }

    @Then("^(?:the )?todo list should be empty$")
    public void verify_empty_todos() {
        verify_todos(0);
    }

}
