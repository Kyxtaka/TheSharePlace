package com.accountplace.api.controller.consulter;

import com.accountplace.api.dto.register.RegisterAccountBodyDTO;
import com.accountplace.api.entity.AccountEntity;
import com.accountplace.api.dto.review.AccountDTO;
import com.accountplace.api.repositories.AccountRepository;
import com.accountplace.api.security.CryptoUtils;
import com.accountplace.api.security.SecurityConstants;
import com.accountplace.api.service.AccountService;
import com.accountplace.api.tools.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/data/account")
public class AccountController {

    // dependencies
    private final AccountService accountService;
    private final CryptoUtils cryptoUtils;

    @Autowired
    private AccountController(AccountService accountService, CryptoUtils cryptoUtils) {
        this.accountService = accountService;
        this.cryptoUtils = cryptoUtils;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> listAllAccounts() {
        List<AccountDTO> accounts = null;
        try {
            accounts = accountService.listAll();
        } catch (Exception e) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllAccounts() {
        Long count = null;
        try {
            count = accountService.count();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(count);
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<AccountDTO> findById(@PathVariable("filter") String filter, @PathVariable("value") String value) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (value ==  null || value.isEmpty()) { return ResponseEntity.notFound().build();}
        AccountDTO account = null;
        try {
            if (filter.equals("id")) {
                Integer id = Integer.valueOf(value);
                account = accountService.findById(id);
                return ResponseEntity.ok(account);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/search/{filter}")
    public ResponseEntity<List<AccountDTO>> findByName(@PathVariable("filter") String filter, @RequestParam("search_query") String search_query) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (search_query ==  null || search_query.isEmpty()) { return ResponseEntity.notFound().build();}
        try {
            switch (filter) {
                case "group" -> {
                    Integer id = Integer.valueOf(search_query);
                    return ResponseEntity.ok(this.accountService.searchByGroupId(id));
                }
                case "email" -> {
                    return ResponseEntity.ok(this.accountService.listAllByEmail(new Email(search_query)));
                }
                case "groupPlatform" -> {
                    String[] ids = search_query.split(",");
                    Integer groupId = Integer.valueOf(ids[0]);
                    Integer platformId = Integer.valueOf(ids[1]);
                    return ResponseEntity.ok(this.accountService.searchByGroupAndPlatformId(groupId, platformId));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("register")
    public ResponseEntity<AccountDTO> createCredential(@RequestBody RegisterAccountBodyDTO bodyDTO) {
        try {
            AccountEntity accountEntity= new AccountEntity();
            accountEntity.setUsername(bodyDTO.getUsername());
            // Encrypt the password
            String encryptedPassword = this.cryptoUtils.encrypt(bodyDTO.getPassword(), SecurityConstants.AES_SECRET_KEY);
            accountEntity.setPassword(encryptedPassword);
            accountEntity.setMail(bodyDTO.getEmail());
            if (bodyDTO.getA2f()) {
                accountEntity.setA2f(1);
            }else {
                accountEntity.setA2f(0);
            }
            accountEntity.setPlatform_id(1);
            accountEntity.setGroup_id(4);
            AccountEntity result = accountService.createAccount(accountEntity);
            return ResponseEntity.ok(accountService.findById(result.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccountDTO> updateCredential(@RequestBody RegisterAccountBodyDTO bodyDTO, @PathVariable String id) {
        try {
            AccountEntity accountEntity = accountService.getEntity(Integer.valueOf(id));
            accountEntity.setUsername(bodyDTO.getUsername());
            accountEntity.setPassword(this.cryptoUtils.encrypt(bodyDTO.getPassword(), SecurityConstants.AES_SECRET_KEY));
            accountEntity.setMail(bodyDTO.getEmail());
            accountEntity.setMail(bodyDTO.getEmail());
            if (bodyDTO.getA2f()) {
                accountEntity.setA2f(1);
            }else {
                accountEntity.setA2f(0);
            }
            accountEntity = accountService.update(accountEntity.getId(), accountEntity);
            return ResponseEntity.ok(accountService.findById(accountEntity.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteCredential(@PathVariable("id") int id) {

        try {
            String result = accountService.deleteAccountById(id);;
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
