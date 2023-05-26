package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.model.UserAccountDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAccountMapper {
    UserAccount userDtoToUser(UserAccountDTO userAccountDTO);

    @AfterMapping
    default void linkBabies(@MappingTarget UserAccount userAccount) {
        userAccount.getBabies().forEach(baby -> baby.setUserAccount(userAccount));
    }

    UserAccountDTO userToUserDto(UserAccount userAccount);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserAccount partialUpdate(UserAccountDTO userAccountDTO, @MappingTarget UserAccount userAccount);
}
