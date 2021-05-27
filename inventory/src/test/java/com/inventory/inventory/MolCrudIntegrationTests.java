package com.inventory.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.UserDAO;
import com.inventory.inventory.auth.Models.LoginRequest;
import com.inventory.inventory.auth.Models.LoginResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MolCrudIntegrationTests {	
	
		
	@Autowired
    private MockMvc mockMvc;	
	
	@Value("${app.password}")
	private String password;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	private String firstMolToken;
	private Long firstMolId;
	 List<UserDAO> users1;
	private String secondMolToken;
	private Long secondMolId;
	 List<UserDAO> users2;
	
	@BeforeAll
	private void setMolToken() throws Exception {
		
		LoginRequest request = new LoginRequest();
		request.setPassword("user000");
		request.setUsername("user0");		
		
        String json = objectMapper.writeValueAsString(request);
	   
		  MvcResult result = mockMvc.perform(post("/api/inventory/auth/signin")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON))
	      .andReturn();
		  
		  String str = result.getResponse().getContentAsString();		  
		  LoginResponse response = objectMapper.readValue(str, LoginResponse.class);		  
		  firstMolToken = response.getToken();
		  firstMolId = response.getId();
		  
		  
		  LoginRequest request2 = new LoginRequest();
			request2.setPassword("user111");
			request2.setUsername("user1");			
			
	        String json2 = objectMapper.writeValueAsString(request2);
		   
			  MvcResult result2 = mockMvc.perform(post("/api/inventory/auth/signin")
					.characterEncoding("utf-8")
					 .content(json2)
					.contentType(MediaType.APPLICATION_JSON))
		      .andReturn();
			  
			  String str2 = result2.getResponse().getContentAsString();		  
			  LoginResponse response2 = objectMapper.readValue(str2, LoginResponse.class);		  
			  secondMolToken = response2.getToken();
			  secondMolId = response2.getId();
			  
			  
			  MvcResult result1 =  mockMvc.perform(get("/api/inventory/users").header("Authorization", "Bearer "+ firstMolToken))		
				        .andExpect(status().isOk())
				       // .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString().length()).isGreaterThan(0))
				        .andDo(MockMvcResultHandlers.print()).andReturn(); 
						  
						  MvcResult result3 =  mockMvc.perform(get("/api/inventory/users").header("Authorization", "Bearer "+ secondMolToken))		
							        .andExpect(status().isOk())
							       // .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString().length()).isGreaterThan(0))
							        .andDo(MockMvcResultHandlers.print()).andReturn(); 
						  
						  String str1 = result1.getResponse().getContentAsString();		  
						  IndexVM vm1 = objectMapper.readValue(str1, IndexVM.class);		  
						  users1 = vm1.getDAOItems();
						  
						  String str3 = result3.getResponse().getContentAsString();		  
						  IndexVM vm2 = objectMapper.readValue(str3, IndexVM.class);		  
						  users2 = vm2.getDAOItems();						  
						 
		
	}
	
	@Test
	public void EveryMolShouldGetAllJustAuthorizedResources() throws Exception {
		 users1.stream().map(u -> assertThat(u.getFirstName().endsWith(""+firstMolId)));
		  users2.stream().map(u -> assertThat(u.getFirstName().endsWith(""+secondMolId)));  
    
  }
	
	@Test
	public void EveryMolShouldGetJustAuthorizedResources() throws Exception {
		
		Long idForMol1 = users1.get(0).getId();
		
		mockMvc.perform(get("/api/inventory/users/"+idForMol1).header("Authorization", "Bearer "+ firstMolToken))		
        .andExpect(status().isOk());
		
		mockMvc.perform(get("/api/inventory/users/"+idForMol1).header("Authorization", "Bearer "+ secondMolToken))		
        .andExpect(status().isBadRequest());
		
		mockMvc.perform(get("/api/inventory/users/"+secondMolId).header("Authorization", "Bearer "+ firstMolToken))		
        .andExpect(status().isBadRequest());
    
  }
	
	@Test
	public void EveryMolShouldPutJustAuthorizedResources() throws Exception {
		
		EditVM model = new EditVM();
		UserDAO userForMol1 = users1.get(0);
		model.setId(userForMol1.getId());
		model.setFirstName(userForMol1.getFirstName());
		model.setLastName(userForMol1.getLastName());
		model.setUserName(userForMol1.getUserName());
		model.setEmail(userForMol1.getEmail());
		
		 String json = objectMapper.writeValueAsString(model);
		
		mockMvc.perform(put("/api/inventory/users")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+ firstMolToken))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User updated successfully!"));
		
		mockMvc.perform(put("/api/inventory/users")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+ secondMolToken))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("item not found !!!"));
       
  }
	
	@Test
	public void EveryMolShouldDeleteJustAuthorizedResources() throws Exception {
		
		Long idForMol1 = users1.get(0).getId();
		
		mockMvc.perform(delete("/api/inventory/users/"+idForMol1).header("Authorization", "Bearer "+ secondMolToken))		
        .andExpect(status().isBadRequest()).andExpect(content().string("item not found !!!"));
    
  }
	
	@Test
	public void ShouldReturnForbiddenWhenMolAccessingAdminResources() throws Exception {
		
		mockMvc.perform(get("/api/inventory/categories").header("Authorization", "Bearer "+ firstMolToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/pendingusers").header("Authorization", "Bearer "+ secondMolToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/countries").header("Authorization", "Bearer "+ firstMolToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/cities").header("Authorization", "Bearer "+ secondMolToken))		
        .andExpect(status().isForbidden());
    
  }
	
	@Test
	public void ShouldReturnForbiddenWhenEmployeeAccessingMolOrAdminResources() throws Exception {
		
		LoginRequest empRequest = new LoginRequest();
		empRequest.setPassword("emp000");
		empRequest.setUsername("emp02 lName0");		
		
        String json = objectMapper.writeValueAsString(empRequest);
	   
		  MvcResult result = mockMvc.perform(post("/api/inventory/auth/signin")
				.characterEncoding("utf-8")
				 .content(json)
				.contentType(MediaType.APPLICATION_JSON))
	      .andReturn();
		  
		  String str = result.getResponse().getContentAsString();		  
		  LoginResponse response = objectMapper.readValue(str, LoginResponse.class);		  
		  String empToken = response.getToken();		 
		
		mockMvc.perform(get("/api/inventory/categories").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/pendingusers").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/countries").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/cities").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/users").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/products").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/deliveries").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());
		
		mockMvc.perform(get("/api/inventory/deliverydetails").header("Authorization", "Bearer "+ empToken))		
        .andExpect(status().isForbidden());		
		
    
  }

}
