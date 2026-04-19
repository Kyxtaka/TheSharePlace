package com.accountplace.api.service.crud;

import com.accountplace.api.dto.crud.create.AccountCreateDTO;
import com.accountplace.api.dto.crud.pub.PublicAccountDTO;
import com.accountplace.api.dto.crud.update.AccountUpdateDTO;
import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.dto.crud.update.PublicPlatformDTO;
import com.accountplace.api.entity.AccountEntity;
import com.accountplace.api.repositories.AccountRepository;
import com.accountplace.api.security.CryptoUtils;
import com.accountplace.api.security.SecurityConstants;
import com.accountplace.api.service.consulter.GroupConsulterService;
import com.accountplace.api.service.consulter.PlatformConsulterService;
import com.accountplace.api.tools.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to manage account-related operations.
 * It provides methods for creating, updating, deleting, and retrieving account entities.
 */
@Service
public class AccountCrudService {

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
    private AccountCrudService (
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
    public PublicAccountDTO create(AccountCreateDTO credential) throws Exception {
        AccountEntity accountEntity = this.convertCreateDTOToEntity(credential);
        return this.convertEntityToPublicDTO(accountRepository.save(accountEntity));
    }

    /**
     * Updates an existing account by its ID with the provided account data.
     *
     * @param accountUpdateDTO The updateDTO for account.
     * @return The updated account entity.
     */
    public PublicAccountDTO update(AccountUpdateDTO accountUpdateDTO) {
        int a2f = (accountUpdateDTO.isA2f()) ? 1 : 0;
        return accountRepository.findById(accountUpdateDTO.getId())
                .map(credential ->
                    {
                        credential.setUsername(accountUpdateDTO.getUsername());
                        credential.setMail(accountUpdateDTO.getEmail().getMailAddress());
                        credential.setPassword(accountUpdateDTO.getPassword());
                        credential.setA2f(a2f);
                        credential.setPlatform_id(accountUpdateDTO.getPlatform().getPlateformId());
                        try {
                            return this.convertEntityToPublicDTO(accountRepository.save(credential));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                .orElseThrow(() ->  new RuntimeException("Account not found"));
    }

    /**
     * Deletes an account by its ID.
     *
     * @param id The ID of the account to delete.
     * @return A success message indicating the account was deleted.
     */
    public String delete(int id) {
        accountRepository.deleteById(id);
        return "Account deleted with id: " + id + " has been deleted successfully";
    }

    /**
     * Converts an accountCreateDTO to an entity, if DTO reference to an existing entity this will return
     * the existing entity, create a new one if not
     * @param accountCreateDTO actual DTO created from a controller
     * @return an account entity
     * @throws Exception
     */
    public AccountEntity convertCreateDTOToEntity(AccountCreateDTO accountCreateDTO) throws Exception {
        Integer a2f = (accountCreateDTO.isA2f()) ? 1 : 0;
        return new AccountEntity(
                accountCreateDTO.getEmail().getMailAddress(),
                accountCreateDTO.getUsername(),
                this.cryptoUtils.encrypt(accountCreateDTO.getPassword(), SecurityConstants.AES_SECRET_KEY),
                a2f,
                accountCreateDTO.getPlatform().getPlateformId(),
                accountCreateDTO.getGroup().getId()
        );
    }

    public PublicAccountDTO convertEntityToPublicDTO(AccountEntity accountEntity) throws Exception {
        PublicGroupDTO groupDTO = this.groupConsulterService.findById(accountEntity.getGroup_id());
        PublicPlatformDTO platformDTO = this.platformConsulterService.findById(accountEntity.getPlatform_id());
        boolean a2f = (accountEntity.getA2f() == 1);
        return new PublicAccountDTO(
                accountEntity.getId(),
                accountEntity.getUsername(),
                this.cryptoUtils.decrypt(accountEntity.getPassword(), SecurityConstants.AES_SECRET_KEY),
                new Email(accountEntity.getMail()),
                a2f,
                groupDTO,
                platformDTO
        );
    }
}
