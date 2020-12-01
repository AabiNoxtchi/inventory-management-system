package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.ProductsService;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;


@RestController
@RequestMapping(value = "${app.BASE_URL}/products")
public class ProductsController extends BaseController<Product, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	private ProductsService service;

	@Override
	protected BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public Boolean checkGetAuthorization() {
		return service.checkGetAuthorization();
	}

	

	/*
	 * ---get all products for one user---
	 * 
	 * @GetMapping("/{id}") public List<Product>
	 * getProductsByUser(@PathVariable("id") long id) {
	 * 
	 * List<Product> list = service.getProductsForUser(id); return list; }
	 */

	/*---get all products for one user by product type---*/

	/*
	 * @GetMapping("/{id}/{producttype}") public List<Product>
	 * getProductsByUser(@PathVariable("id") long id,
	 * 
	 * @PathVariable("producttype") ProductType productType) {
	 * 
	 * List<Product> list = service.getProductsForUser(id, productType); return
	 * list; }
	 */

	/*
	 * ---get all products for one user by is discarded---
	 * 
	 * @GetMapping("/{id}/discarded/{discarded}") public List<Product>
	 * getProductsByUser(@PathVariable("id") long id, @PathVariable("discarded")
	 * boolean discarded) {
	 * 
	 * List<Product> list = service.getProductsForUser(id, discarded); return list;
	 * }
	 */

	/*
	 * ---get all products for one user by is available---
	 * 
	 * @GetMapping("/{id}/available/{available}") public List<Product>
	 * getAvailableProductsByUser(@PathVariable("id") long id,
	 * 
	 * @PathVariable("available") boolean available) { List<Product> list =
	 * service.getAvailableProductsForUser(id, available); return list; }
	 */
	/*
	 * ---get all products for one user by employee---
	 * 
	 * @GetMapping("/{id}/employee/{employeeid}") public List<Product>
	 * getProductsByUser(@PathVariable("id") long id, @PathVariable("employeeid")
	 * Long employeeId) { List<Product> list =
	 * service.getProductsForEmployee(employeeId); return list; }
	 */
	/*
	 * ---get all products for one user by list Ids---
	 * 
	 * @GetMapping("/{id}/ids/{productsIds}") public List<Product>
	 * getProductsByProductsIds(@PathVariable("id") long id,
	 * 
	 * @PathVariable("productsIds") ArrayList<Long> Ids) {
	 * 
	 * List<Product> list = service.getProductsByIdIn(Ids); return list; }
	 */

	

	
	
	/*
	 * @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable
	 * List<Long> ids) { service. return service.delete(id); }
	 */
}
