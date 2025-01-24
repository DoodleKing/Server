package mana.doodleking.domain.user.service;

import mana.doodleking.domain.user.domain.Character;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.dto.CreateUserReq;
import mana.doodleking.domain.user.repository.CharacterRepository;
import mana.doodleking.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CharacterRepository characterRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        characterRepository.deleteAll();
    }

    @Test
    void validUserName_invalid() {
        // Given
        when(userRepository.existsByName("test")).thenReturn(true);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            userService.validUserName("test"); // 이미 존재하는 이름
        });

        // Then
        assertEquals("이미 존재하는 유저명입니다. : test", exception.getMessage());
    }


    @Test
    void validUserName_valid() {
        // Given
        when(userRepository.existsByName("test")).thenReturn(false);

        // When & Then
        assertDoesNotThrow(() -> userService.validUserName("test")); // 예외 발생 안 함
    }

    @Test
    void validCharacter_invalid() {
        // Given
        when(characterRepository.existsById(1L)).thenReturn(false);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            userService.validCharacter(1L); // 이미 존재하는 이름
        });

        // Then
        assertEquals("존재하지 않는 캐릭터 ID입니다. : 1", exception.getMessage());    }


    @Test
    void validCharacter_valid() {
        // Given
        when(characterRepository.existsById(1L)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> userService.validCharacter(1L)); // 예외 발생 안 함
    }

    @Test
    void createUser_fail() {
        // When
        when(characterRepository.findCharacterById(1L)).thenReturn(null);

        // Then
        Exception exception = assertThrows(Exception.class, () -> {
            userService.createUser(new CreateUserReq(1L, "test"));
        });

        // Then
        assertEquals("유저 생성에 실패했습니다.", exception.getMessage());
    }

    @Test
    void createUser_success() throws Exception {
        // Given
        Character mockChar = Character.of("name1", "image1");
        when(characterRepository.findCharacterById(1L)).thenReturn(mockChar);
        when(userRepository.save(argThat(user ->
                user.getName().equals("test") && user.getCharacter().equals(mockChar)
        ))).thenAnswer(invocation -> invocation.getArgument(0)); // 조건 만족 시 전달된 객체 반환

        // When
        User user = userService.createUser(new CreateUserReq(1L, "test"));

        // Then
        assertNotNull(user);
        assertEquals(user.getName(), "test");
        assertEquals(user.getCharacter(), mockChar);
    }
}