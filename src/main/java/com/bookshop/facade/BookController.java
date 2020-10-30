package com.bookshop.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop.business.BookService;
import com.bookshop.to.BookPutTO;
import com.bookshop.to.BookTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

//TODO: Überlast (Bucket4J)
//TODO: Security @Authorized + Swagger
@RestController
@RequestMapping(value="/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value="Book REST-Endpoint",  produces = MediaType.APPLICATION_JSON_VALUE )
public class BookController {
	

	@Autowired
	private BookService bookService;

	@GetMapping(value = "")
	@ApiOperation(value = "Retrieve a list of all products", notes = "Retrieve a list of all products", response = BookTO.class, responseContainer = "List")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a product list", response = BookTO.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	public ResponseEntity<List<BookTO>> readAll() {
        return ResponseEntity.ok(bookService.readAll());
	}

	
	@GetMapping("/{id}")
	@ApiOperation(value = "Read a product", notes="Read a product", response = BookTO.class)
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved product", response = BookTO.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	public ResponseEntity<BookTO> readBook(@PathVariable String id) {
        return ResponseEntity.ok(bookService.read(id));
	}

	@PutMapping()
	@ApiOperation(value = "Create a new product", notes="Create a new product")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully insert a product", response = BookTO.class),
            @ApiResponse(code = 400, message = "Invalid data"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
	public ResponseEntity<BookTO> insert(@RequestBody BookPutTO bookPutTO) {
		return ResponseEntity.ok(bookService.insert(bookPutTO));
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Update a product", notes="Update a product")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update a product", response = BookTO.class),
            @ApiResponse(code = 400, message = "Invalid data"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	public ResponseEntity<BookTO> update(
			@PathVariable String id, 
			@RequestBody BookPutTO bookPutTO) {
		return ResponseEntity.ok(bookService.update(id, bookPutTO));
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete a product", notes = "Delete a product (soft deletion")
	@ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully delete a product"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	public ResponseEntity<Void> delete(@PathVariable String id) {
		bookService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
