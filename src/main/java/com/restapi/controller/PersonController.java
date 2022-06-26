package com.restapi.controller;

import com.restapi.model.Person;
import com.restapi.service.PersonService;
import com.restapi.util.PersonErrorResponse;
import com.restapi.util.PersonNotCreatedException;
import com.restapi.util.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public List<Person> getAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public Person onePerson(@PathVariable(name = "id") int id) {
        return personService.findOne(id);
    }


    @PostMapping()
    public ResponseEntity<Person> createPerson(@RequestBody @Valid Person person,
                                               BindingResult bindingResult) {
        personNotFound(bindingResult);
        personService.save(person);

        //return ResponseEntity.ok(HttpStatus.OK);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") int id,
                                               @RequestBody @Valid Person person,
                                               BindingResult bindingResult)
    {
        personNotFound(bindingResult);
        Person toUpdate = personService.findOne(id);
        toUpdate.setAge(person.getAge());
        toUpdate.setName(person.getName());
        toUpdate.setEmail(person.getEmail());
        personService.save(toUpdate);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable("id") int id) {
        Person toDelete = personService.findOne(id);
        personService.delete(toDelete);
        return new ResponseEntity<>(toDelete, HttpStatus.OK);
    }

    private void personNotFound(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errorList = bindingResult.getFieldErrors();
            errorList.forEach(
                    e -> errorMessage
                            .append(e.getField())
                            .append("-")
                            .append(e.getDefaultMessage())
                            .append(";")
            );
            throw new PersonNotCreatedException(errorMessage.toString());
        }
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person not found",
                new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(), new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
