package com.bookshop.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.business.OrderService;
import com.bookshop.to.OrderPutTO;
import com.bookshop.to.OrderSearchTO;
import com.bookshop.to.OrderTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

//TODO: Überlast (Bucket4J)
//TODO: Security @Authorized
@RestController
@RequestMapping(value="/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value="Order REST-Endpoint",  produces = MediaType.APPLICATION_JSON_VALUE )
public class OrderController {

	@Autowired
	private OrderService service;

	@PostMapping(value = "")
	@ApiOperation(value = "Retrieving all orders", notes = "Retrieving all orders within a given time period", response = OrderTO.class, responseContainer = "List")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of orders", response = OrderTO.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
	public ResponseEntity<List<OrderTO>> readAll(@RequestBody OrderSearchTO searchTO) 
	{		
        return ResponseEntity.ok(service.readAll(searchTO.getFrom(), searchTO.getTo()));
	}

	@PutMapping()
	@ApiOperation(value = "Placing an order", notes="Placing an order")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully insert a order", response = OrderTO.class),
            @ApiResponse(code = 400, message = "Invalid data"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	public ResponseEntity<OrderTO> insert(@RequestBody OrderPutTO bookPutTO) {
		return ResponseEntity.ok(service.insert(bookPutTO));
	}

}
