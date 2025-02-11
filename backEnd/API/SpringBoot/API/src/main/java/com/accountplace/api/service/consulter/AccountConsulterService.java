package com.accountplace.api.service.consulter;

import com.accountplace.api.dto.crud.pub.PublicAccountDTO;
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
public class AccountConsulterService {

    // Repositories
    private final AccountRepository accountRepository;
    private final GroupConsulterService groupConsulterService;
    private final PlatformConsulterService platformConsulterService;
    private final CryptoUtils cryptoUtils;

    /**
     * Constructor injection for AccountService dependencies.
     *
     * @param accountRepository The repository for account-related database operations.
     * @param groupConsulterService The service for managing groups.
     * @param platformConsulterService The service for managing platforms.
     * @param cryptoUtils The utility class for cryptographic operations.
     */
    @Autowired
    private AccountConsulterService(
            AccountRepository accountRepository,
            GroupConsulterService groupConsulterService,
            PlatformConsulterService platformConsulterService,
            CryptoUtils cryptoUtils
    ) {
        this.accountRepository = accountRepository;
        this.groupConsulterService = groupConsulterService;
        this.platformConsulterService = platformConsulterService;
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
     * @return A list of com.accountplace.api.dto.crud.pub.PublicAccountDTO objects representing all accounts.
     */
    public List<PublicAccountDTO> listAll() {
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
     * @return The PublicAccountDTO representing the account.
     * @throws Exception if the account cannot be found.
     */
    public PublicAccountDTO findById(Integer id) throws Exception {
        AccountEntity account = accountRepository.findById(id).orElseThrow( () -> new RuntimeException("Account not found"));
        return this.convertToDto(account);
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
     * @return A list of PublicAccountDTOs associated with the given group ID.
     */
    public List<PublicAccountDTO> searchByGroupId(Integer groupId) {
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
     * @return A list of PublicAccountDTOs associated with the given group and platform IDs.
     */
    public List<PublicAccountDTO> searchByGroupAndPlatformId(Integer groupId, Integer plateformId) {
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
     * @return A list of PublicAccountDTOs associated with the given email.
     */
    public List<PublicAccountDTO> listAllByEmail(Email mail) {
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
     * Converts an AccountEntity to an PublicAccountDTO.
     *
     * @param accountEntity The AccountEntity to convert.
     * @return The PublicAccountDTO representing the account.
     * @throws Exception if there are issues during conversion.
     */
    private PublicAccountDTO convertToDto(AccountEntity accountEntity) throws Exception {
        boolean a2f = accountEntity.getA2f() == 1;
        Email mail = new Email(accountEntity.getMail());
        return new PublicAccountDTO(
                accountEntity.getId(),
                accountEntity.getUsername(),
                this.cryptoUtils.decrypt(accountEntity.getPassword(), SecurityConstants.AES_SECRET_KEY),
                mail,
                a2f,
                groupConsulterService.findById(accountEntity.getGroup_id()),
                platformConsulterService.findById(accountEntity.getPlatform_id())
        );
    }


}
