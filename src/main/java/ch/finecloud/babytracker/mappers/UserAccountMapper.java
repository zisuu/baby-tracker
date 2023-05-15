package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.model.UserAccountDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserAccountMapper {
    UserAccount userDtoToUser(UserAccountDTO userAccountDTO);

    UserAccountDTO userToUserDto(UserAccount userAccount);
}
