package mana.doodleking.domain.user.service;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.user.domain.Character;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.dto.CreateUserReq;
import mana.doodleking.domain.user.repository.CharacterRepository;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;

    public void validUserName(String userName) throws Exception {
        if (userRepository.existsByName(userName))
            throw new Exception("이미 존재하는 유저명입니다. : " + userName);
    }

    public void validCharacter(Long characterId) throws Exception {
        if (!characterRepository.existsById(characterId))
            throw new Exception("존재하지 않는 캐릭터 ID입니다. : " + characterId);
    }

    public User createUser(CreateUserReq createUserReq) throws Exception {
        Character character = characterRepository.findCharacterById(createUserReq.getCharacterId());

        if (character == null)
            throw new Exception("유저 생성에 실패했습니다.");

        User createUser = User.of(createUserReq.getUserName(), character);
        return userRepository.save(createUser);
    }
}
