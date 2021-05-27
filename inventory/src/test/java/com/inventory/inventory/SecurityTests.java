package com.inventory.inventory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.auth.Models.LoginRequest;
import com.inventory.inventory.auth.Models.LoginResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityTests {
	
	@Autowired
    private MockMvc mockMvc;	
	
	@Value("${app.password}")
	private String password;
	
	ObjectMapper objectMapper = new ObjectMapper();
	

	@Test
	public void GetAllMethodsShouldReturnUnAuthorizedWhenUnAuthenticatedUsers() throws Exception {	
		
		mockMvc.perform(get("/api/inventory/products"))
        .andExpect(status().isUnauthorized());//.isOk());
		
  }
	
	@Test
	public void GetMethodsShouldReturnUnAuthorizedWhenUnAuthenticatedUsers() throws Exception {
		
		mockMvc.perform(get("/api/inventory/products/1"))
        .andExpect(status().isUnauthorized());//.isOk());
   
  }
	
	@Test
	public void PutMethodsShouldReturnUnAuthorizedWhenUnAuthenticatedUsers() throws Exception {
		
		EditVM model = new EditVM();
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/products/1", model))
        .andExpect(status().isUnauthorized());//.isOk());
    
  }
	
	@Test
	public void DeleteMethodsShouldReturnUnAuthorizedWhenUnAuthenticatedUsers() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/inventory/products/1"))
        .andExpect(status().isUnauthorized());//.isOk());
   
  }
	
	@Test
	//@WithMockUser(username = "Username", password = "Password")
	public void ShouldReturnUnAuthorizedWhenUnValidLogin() throws Exception {
		
		LoginRequest request = new LoginRequest();
		request.setPassword("Password");
		request.setUsername("Username");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
	   
		mockMvc.perform(post("/api/inventory/auth/signin")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON))
			   
			    
	        .andExpect(status().isUnauthorized());
	}
	
	@Test
	//@WithMockUser(username = "Username", password = "Password")
	public void ShouldReturnOkWhenValidLogin() throws Exception {
		
		LoginRequest request = new LoginRequest();
		request.setPassword(password);
		request.setUsername("admin");
		
		
        String json = objectMapper.writeValueAsString(request);
	   
		  MvcResult result = mockMvc.perform(post("/api/inventory/auth/signin")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON))	   
			    
	        .andExpect(status().isOk())
	        .andDo(MockMvcResultHandlers.print()).andReturn();
		  
		  String str = result.getResponse().getContentAsString();		  
		  LoginResponse response = objectMapper.readValue(str, LoginResponse.class);		  
		  String jwt = response.getToken();		  
		  assertThat(jwt).isNotNull();
		
	}
	
	@Test
	public void GetAllMethodsShouldReturnOkWhenAuthorizedUsers() throws Exception {
		
		LoginRequest request = new LoginRequest();
		request.setPassword(password);
		request.setUsername("admin");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
	   
		  MvcResult result1 = mockMvc.perform(post("/api/inventory/auth/signin")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON))
	      .andReturn();
		  
		  String str = result1.getResponse().getContentAsString();		  
		  LoginResponse response = objectMapper.readValue(str, LoginResponse.class);		  
		  String jwt = response.getToken();
		  
		 
	
		 mockMvc.perform(get("/api/inventory/users").header("Authorization", "Bearer "+ jwt))		
        .andExpect(status().isOk())
        .andExpect(result2 -> assertThat(result2.getResponse().getContentAsString().length()).isGreaterThan(0))
        .andDo(MockMvcResultHandlers.print());//.andReturn();      
    
  }
	
	

}
