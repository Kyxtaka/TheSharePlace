package com.accountplace.api.service;

import com.accountplace.api.entity.AccountEntity;
import com.accountplace.api.repositories.AccountRepository;
import com.accountplace.api.security.CryptoUtils;
import com.accountplace.api.security.SecurityConstants;
import com.accountplace.api.tools.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to manage account-related operations.
 * It provides methods for creating, updating, deleting, and retrieving account entities.
 */
@Service
public class AccountService {

    // Repositories
    private final AccountRepository accountRepository;
    private final GroupService groupService;
    private final PlateformService plateformService;
    private final CryptoUtils cryptoUtils;

    /**
     * Constructor injection for AccountService dependencies.
     *
     * @param accountRepository The repository for account-related database operations.
     * @param groupService The service for managing groups.
     * @param plateformService The service for managing platforms.
     * @param cryptoUtils The utility class for cryptographic operations.
     */
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

    /**
     * Creates a new account and saves it to the repository.
     *
     * @param credential The account entity to be created.
     * @return The saved account entity.
     */
    public AccountEntity createAccount(AccountEntity credential) {
        return accountRepository.save(credential);
    }

    /**
     * Retrieves all accounts and converts them to DTO format.
     *
     * @return A list of AccountDTO objects representing all accounts.
     */
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

    /**
     * Finds an account by its ID and returns it as a DTO.
     *
     * @param id The ID of the account to retrieve.
     * @return The AccountDTO representing the account.
     * @throws Exception if the account cannot be found.
     */
    public AccountDTO findById(Integer id) throws Exception {
        AccountEntity account = accountRepository.findById(id).orElseThrow( () -> new RuntimeException("Account not found"));
        return this.convertToDto(account);
    }

    /**
     * Retrieves an account entity by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The AccountEntity object representing the account.
     */
    public AccountEntity getEntity(Integer id) {
        return accountRepository.findById(id).orElseThrow( () -> new RuntimeException("Account not found"));
    }

    /**
     * Counts the total number of accounts in the repository.
     *
     * @return The total number of accounts.
     */
    public Long count() {
        return accountRepository.count();
    }

    /**
     * Searches for accounts based on a group ID and returns them as DTOs.
     *
     * @param groupId The group ID to search for.
     * @return A list of AccountDTOs associated with the given group ID.
     */
    public List<AccountDTO> searchByGroupId(Integer groupId) {
        List<AccountEntity> lst =  accountRepository.listByGroupId(groupId);
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

    /**
     * Searches for accounts based on both a group ID and a platform ID, and returns them as DTOs.
     *
     * @param groupId The group ID to search for.
     * @param plateformId The platform ID to search for.
     * @return A list of AccountDTOs associated with the given group and platform IDs.
     */
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
    }

    /**
     * Retrieves all accounts associated with a specific email and returns them as DTOs.
     *
     * @param mail The email to search for.
     * @return A list of AccountDTOs associated with the given email.
     */
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
    }

    /**
     * Updates an existing account by its ID with the provided account data.
     *
     * @param id The ID of the account to update.
     * @param accountEntity The account entity containing updated data.
     * @return The updated account entity.
     */
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

    /**
     * Deletes an account by its ID.
     *
     * @param accountDTO The ID of the account to delete.
     * @return A success message indicating the account was deleted.
     */
    public String deleteAccountById(AccountDTO accountDTO) {
        accountRepository.deleteById(accountDTO.getId());
        return "Account deleted with id: " + accountDTO.getId() + " has been deleted successfully";
    }

    /**
     * Converts an AccountEntity to an AccountDTO.
     *
     * @param accountEntity The AccountEntity to convert.
     * @return The AccountDTO representing the account.
     * @throws Exception if there are issues during conversion.
     */
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

    /**
     * Converts an accountDTO to an entity, if DTO reference to an existing entity this will return
     * the existing entity, create a new one if not
     * @param accountDTO actual DTO created from a controller
     * @return an account entity
     * @throws Exception
     */
    private AccountEntity convertToEntity(AccountDTO accountDTO) throws Exception {
        Integer a2f = (accountDTO.isA2f()) ? 1 : 0;
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountDTO.getId());
        return accountEntity.orElseGet(() -> new AccountEntity(
                accountDTO.getEmail().getMailAddress(),
                accountDTO.getUsername(),
                accountDTO.getUsername(),
                a2f,
                accountDTO.getPlatform().getPlateformId(),
                accountDTO.getGroup().getId()
        ));
    }
}
