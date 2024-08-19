package com.accountplace.api.service;

import com.accountplace.api.entity.AccountEntity;
import com.accountplace.api.dto.review.AccountDTO;
import com.accountplace.api.repositories.AccountRepository;
import com.accountplace.api.security.CryptoUtils;
import com.accountplace.api.security.SecurityConstants;
import com.accountplace.api.tools.Email;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    // Repositories
    private final AccountRepository accountRepository;
    private final GroupService groupService;
    private final PlateformService plateformService;
    private final CryptoUtils cryptoUtils;

    // Constructor injection
    @Autowired
    private AccountService (
            AccountRepository accountRepository,
            GroupService groupService,
            PlateformService plateformService,
            CryptoUtils cryptoUtils
    ) {
        this.accountRepository = accountRepository;
        this.groupService = groupService;
        this.plateformService = plateformService;
        this.cryptoUtils = cryptoUtils;
    }

    // A refaire
    public AccountEntity createAccount(AccountEntity credential) {
        return accountRepository.save(credential);
    }

    public List<AccountDTO> listAll() {
        List<AccountEntity> lst =  accountRepository.findAll();
        return lst.stream()
                .map(entity -> {
                    try {
                        return convertToDto(entity);
                    } catch (Exception e) {
                        throw new RuntimeException(e); // Wrap checked exceptions
                    }
                })
                .collect(Collectors.toList());
    }

    public AccountDTO findById(Integer id) throws Exception {
        AccountEntity account = accountRepository.findById(id).orElseThrow( () -> new RuntimeException("Account not found"));
        return this.convertToDto(account);
    }

    public AccountEntity getEntity(Integer id) {
        return accountRepository.findById(id).orElseThrow( () -> new RuntimeException("Account not found"));
    }

    public Long count() {
        return accountRepository.count();
    }

    public List<AccountDTO> searchByGroupId(Integer groupId) {
        List<AccountEntity> lst =  accountRepository.listByGroupId(groupId);
        return lst.stream()
                .map(entity -> {try {
                    return convertToDto(entity);
                } catch (Exception e) {
                    throw new RuntimeException(e); // Wrap checked exceptions
                }

                })
                .collect(Collectors.toList());
//        return lst.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<AccountDTO> searchByGroupAndPlatformId(Integer groupId, Integer plateformId) {
        List<AccountEntity> lst = accountRepository.listByGroupAndPlateformId(groupId, plateformId);
        return lst.stream()
                .map(entity -> {
                    try {
                        return convertToDto(entity);
                    } catch (Exception e) {
                        throw new RuntimeException(e); // Wrap checked exceptions
                    }
                })
                .collect(Collectors.toList());
//        return lst.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    public List<AccountDTO> listAllByEmail(Email mail) {
        List<AccountEntity> lst = accountRepository.listByMail(mail.getMailAddress());
        return lst.stream()
                .map(entity -> {
                    try {
                        return convertToDto(entity);
                    } catch (Exception e) {
                        throw new RuntimeException(e); // Wrap checked exceptions
                    }
                })
                .collect(Collectors.toList());
//        return lst.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public AccountEntity update(Integer id, AccountEntity accountEntity) {
        return accountRepository.findById(id).map(credential -> {
           credential.setUsername(accountEntity.getUsername());
           credential.setMail(accountEntity.getMail());
           credential.setPassword(accountEntity.getPassword());
           credential.setA2f(accountEntity.getA2f());
           credential.setPlatform_id(accountEntity.getPlatform_id());
           return accountRepository.save(credential);
        }).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public String deleteAccountById(Integer id) {
        accountRepository.deleteById(id);
        return "Account deleted with id: " + id + " has been deleted successfully";
    }

    private AccountDTO convertToDto(AccountEntity accountEntity) throws Exception {
        boolean a2f = accountEntity.getA2f() == 1;
        Email mail = new Email(accountEntity.getMail());
        return new AccountDTO(
                accountEntity.getId(),
                accountEntity.getUsername(),
                this.cryptoUtils.decrypt(accountEntity.getPassword(), SecurityConstants.AES_SECRET_KEY),
                mail,
                a2f,
                groupService.findById(accountEntity.getGroup_id()),
                plateformService.findById(accountEntity.getPlatform_id())
        );
    }
}
