package rafikibora.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.dto.CustomUserDetails;
import rafikibora.dto.SupportDto;
import rafikibora.model.support.Support;
import rafikibora.repository.SupportRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class SupportService  implements SupportInterface{
    @Autowired
    private SupportRepository supportRepository;

    /**
     Create Unique TID
     */


    public String createTID(){
        return UUID.randomUUID().toString().substring(0,16);
    }


    /**
     Create Support
     */


    @Transactional
    public Support save(Support support) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        support.setName(support.getName());
        support.setReason(( support.getReason()));
        support.setTid(createTID());
        support.setDate(new Date());
        return supportRepository.save(support);
    }


    /**
     List All Support
     */


    @Transactional
    public List<Support> list() {
        return supportRepository.findAll();

    }


    /**
     List Support By ID
     */

    @Transactional
    public Support getById(Long id) {
        Support support = supportRepository.findById(id).get();
        return support;

    }


    /**
     Update Support By ID
     */


    @Transactional
    public void update(Long id, SupportDto supportDto) {
        Support support = supportRepository.findById(id).get();
        if (supportDto.getName() != null) {
            support.setName(supportDto.getName());
        }
        if (supportDto.getReason() !=null) {
            support.setReason(supportDto.getReason());
        }
        supportRepository.save(support);

    }


    //Delete Support by Id
//    @Transactional
//    public void deleteById(Long id) {
//        try {
//            supportRepository.deleteById(id);
//        }catch (EmptyResultDataAccessException e){
//
//        }
//
//    }


}


