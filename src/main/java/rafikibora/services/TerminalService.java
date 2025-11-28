package rafikibora.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.dto.CustomUserDetails;
import rafikibora.dto.TerminalDto;
import rafikibora.exceptions.InvalidCheckerException;
import rafikibora.exceptions.ResourceNotFoundException;
import rafikibora.model.terminal.Terminal;
import rafikibora.model.users.User;
import rafikibora.repository.TerminalRepository;
import rafikibora.repository.UserRepository;
import rafikibora.security.util.exceptions.ExceptionUtilService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class TerminalService implements TerminalInterface {
    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     Generate A Unique TID
     */

    public String createTID(){
        return UUID.randomUUID().toString().substring(0,16);
    }

    /**
     Create Terminal
     */

    @Transactional
    public Terminal save(Terminal terminal) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//      Terminal terminal = new Terminal();
        terminal.setModelType(terminal.getModelType());
        terminal.setSerialNo(terminal.getSerialNo());
        terminal.setDeleted(false);
        terminal.setCreatedOn((LocalDateTime.now()));
        terminal.setUpdatedOn((LocalDateTime.now()));
        terminal.setTid(createTID());
        terminal.setMid(user.getUser());
//        terminal.setMid(user.getUser());
//        terminal.setMid(createMID());
        terminal.setTerminalMaker(user.getUser());
//        return terminalRepository.save(terminal);



        /**
         Catching Entry of Duplicate Values.
         */

        try {
            terminalRepository.save(terminal);
        } catch (JpaSystemException e) {
            if (ExceptionUtilService.isSqlDuplicatedModelType(e)) {
                throw new Exception("Duplicate Entry is not Allowed");
            } else {}
            }
        return terminal;
        }

        /**
     List All Terminal
     */
    @Transactional
    public List<Terminal> list() {
        return terminalRepository.findAll();

    }


    /**
     List all terminals not assigned to a merchant
     */

    @Transactional
    public List<Terminal> unassignedTerminals() {
      return  terminalRepository.findByMid_MidIsNull();
    }

    /**
     List all terminals for a given merchant not assigned to an agent
     */
    @Transactional
    public List<Terminal> agentUnassignedTerminals(String merchantID) {
        User merchant = userRepository.findByMid(merchantID).get();
        return  terminalRepository.findByMidAndAgentIsNull(merchant);
    }


    /**
     List Terminal by ID
     */

    @Transactional
    public Terminal getById(Long id) {
        Terminal terminal = terminalRepository.findById(id).get();
        return terminal;

    }


    /**
     Update Terminal by ID
     */

    @Transactional
    public void update(Long id, TerminalDto terminalDto) {
        Optional<Terminal> tx = terminalRepository.findById(id);
        if (!tx.isPresent())
            throw new ResourceNotFoundException("Terminal Does Not Exist");

        Terminal terminal = tx.get();
//        Terminal terminal = terminalRepository.findById(id).get();
        if (terminalDto.getModelType() != null) {
            terminal.setModelType(terminalDto.getModelType());
        }
        if (terminalDto.getSerialNo() !=null) {
            terminal.setSerialNo(terminalDto.getSerialNo());
        }
        terminalRepository.save(terminal);
    }


    /**
     Approve Terminal by ID
     */

    @Transactional
    public void approve(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//        Long id = Long.parseLong(terminalDto.getId());
        Optional<Terminal> tx = terminalRepository.findById(id);
        if (!tx.isPresent())
            throw new ResourceNotFoundException("Terminal Does Not Exist");

        Terminal terminal = tx.get();
        Long checkerId = user.getUser().getUserid();
        Long makerId = terminal.getTerminalMaker().getUserid();
//        System.out.println("******************************************");
//        System.out.println("Maker id: "+makerId);
//        System.out.println("Cehecker id: "+checkerId);
//        System.out.println("******************************************");

        if (checkerId.equals(makerId))
            throw new InvalidCheckerException("Creator of resource is not allowed to approve.");

        terminal.setTerminalChecker(user.getUser());
        terminal.setStatus(true);
        terminalRepository.save(terminal);
    }


    /**
     Delete Terminal by ID
     */

    @Transactional
    public void deleteById(Long id) {
        Optional<Terminal> tx = terminalRepository.findById(id);
        if (!tx.isPresent())
            throw new ResourceNotFoundException("Terminal Does Not Exist");

        Terminal terminal = tx.get();
        try {
            terminalRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){

        }

    }


}
