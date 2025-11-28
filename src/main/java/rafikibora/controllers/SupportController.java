package rafikibora.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rafikibora.dto.SupportDto;
import rafikibora.model.support.Support;
import rafikibora.repository.SupportRepository;
import rafikibora.services.SupportService;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/api/support")
public class SupportController {
        @Autowired
        private SupportService supportService;
        private SupportRepository supportRepository;


    /**
     Create Support
     */


        @PostMapping
        public ResponseEntity<?> create(@RequestBody Support support) {
            System.out.println(support.toString());
            String msg = "";
             supportService.save(support);
            msg = "Support created successfully";
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        }

    /**
     List All Support
     */

        @GetMapping(produces = {"application/json"})
        public ResponseEntity<List<Support>> list() {
            List<Support> support = supportService.list();
            return new ResponseEntity<>(support, HttpStatus.OK);
        }


    /**
     List Support By ID
     */


        @GetMapping(value = "/{id}", produces = {"application/json"})
        public ResponseEntity<Support> listOne(@PathVariable("id") Long id) {
            System.out.println(id.toString());
            Support support = supportService.getById(id);
            return new ResponseEntity<>(support, HttpStatus.OK);
        }


    /**
     Update Support By ID
     */


        @PatchMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
        public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody SupportDto supportDto) {
            System.out.println(id.toString());
            supportService.update(id, supportDto);
            return new ResponseEntity<>("Support updated successfully", HttpStatus.OK);
        }



        //Delete support by Id
//
//        @DeleteMapping(value = "/{id}")
//        public ResponseEntity<String> delete(@PathVariable("id") Long id) {
//            supportService.deleteById(id);
//            return new ResponseEntity<>("Support deleted successful", HttpStatus.OK);
//        }


    }


